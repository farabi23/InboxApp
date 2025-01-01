package io.javabrains.inbox.controllers;

import io.javabrains.inbox.email.Email;
import io.javabrains.inbox.email.EmailRepository;
import io.javabrains.inbox.email.EmailService;
import io.javabrains.inbox.emaillist.EmailListItemRepository;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;
import io.javabrains.inbox.folders.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ComposeController {

    @Autowired private FolderRepository folderRepository;
    @Autowired private FolderService folderService;
    @Autowired private EmailRepository emailRepository;
    @Autowired private EmailService emailService;

    @GetMapping(value = "/compose")
    public String getComposePage(
            @RequestParam(required = false) String to,
            @RequestParam(required = false) UUID id,
            @AuthenticationPrincipal OAuth2User principal
            , Model model){

            if((principal == null) || !StringUtils.hasText(principal.getAttribute("login"))){
                return "index";
            }

            //FEtch folders
            String userId = principal.getAttribute("login");
            List<Folder> userFolders =folderRepository.findAllById(userId);
            model.addAttribute("userFolders", userFolders);
            List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
            model.addAttribute("defaultFolders", defaultFolders);

        String userName = principal.getAttribute("name");
        if(!StringUtils.hasText(userName)){
            userName = principal.getAttribute("login"); //fall back to login, if the user has no name on github
        }
        model.addAttribute("userName", userName);

            model.addAttribute("stats", folderService.mapCountToLabels(userId));

            List<String> uniqueToIds = splitIds(to);
            model.addAttribute("toIds", String.join(", ", uniqueToIds));


        if (id != null) { // Ensure 'id' is not null
            Optional<Email> optionalEmail = emailRepository.findById(id);
            if (optionalEmail.isPresent()) {
                Email email = optionalEmail.get();
                if (emailService.doesHaveAccess(email, userId)) {
                    model.addAttribute("subject", emailService.getReplySubject(email.getSubject()));
                    model.addAttribute("body", emailService.getReplyBody(email));
                }
                System.out.println("Email Subject: " + emailService.getReplySubject(email.getSubject()));
                System.out.println("Email Body: " + emailService.getReplyBody(email));
                System.out.println("Email To: " + email.getTo());

            }

        }



        return "compose-page";
    }


    private static List<String> splitIds(String to) {
        if(!StringUtils.hasText(to)) {
        return new ArrayList<String>();
        }
        String[] splitIds = to.split(",");
        List<String> uniqueToIds =  Arrays.asList(splitIds).
                 stream().
                 map(id -> StringUtils.trimWhitespace(id)).
                 filter(id ->StringUtils.hasText(id)).
                 distinct()
                 .collect(Collectors.toList());
        return uniqueToIds;
    }




    @PostMapping("/sendEmail")
    public ModelAndView sendEmail(
            @RequestBody MultiValueMap<String, String> formData,
            @AuthenticationPrincipal OAuth2User principal
            ){
        if((principal == null) || !StringUtils.hasText(principal.getAttribute("login"))){
            return new ModelAndView("redirect:/");
        }
        String from = principal.getAttribute("login");

        List<String> toIds = splitIds(formData.getFirst("toIds"));
        String subject = formData.getFirst("subject");
        String body = formData.getFirst("body");

        emailService.sendEmail(from, toIds, subject, body);
        return new ModelAndView("redirect:/");
    }
}
