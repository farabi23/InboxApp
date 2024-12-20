package io.javabrains.inbox;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import io.javabrains.inbox.emaillist.EmailListItem;
import io.javabrains.inbox.emaillist.EmailListItemKey;
import io.javabrains.inbox.emaillist.EmailListItemRepository;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;
import io.javabrains.inbox.folders.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.Arrays;

@SpringBootApplication
@RestController
public class InboxApp {


	@Autowired FolderRepository folderRepository;

	@Autowired EmailListItemRepository emailListItemRepository;

	public static void main(String[] args) {
		SpringApplication.run(InboxApp.class, args);
	}

    /*
    This is necessary to use the Astar DB secure with SPring Boot
    to connect
     */

    @Bean
    CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties){
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

	@PostConstruct
	public void init(){


		folderRepository.save( new Folder("farabi23", "Inbox", "blue"));

		folderRepository.save( new Folder("farabi23", "Sent", "green"));


		folderRepository.save( new Folder("farabi23", "Important", "yellow"));

		for (int i = 0; i <10 ; i++) {

			EmailListItemKey key = new EmailListItemKey();
			key.setId("farabi23");
			key.setLabel("Inbox");
			key.setTimeUUID(Uuids.timeBased());

			EmailListItem item = new EmailListItem();
			item.setKey(key);
			item.setTo(Arrays.asList("farabi23"));
			item.setSubject("Subject " + i);
			item.setUnread(true);

			emailListItemRepository.save(item);

		}
	}

	


}
