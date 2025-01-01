
# Messaging App - Spring Boot Project

## Overview

The **Messaging App** is a Spring Boot-based application designed to provide a user-friendly interface for managing and sending messages. This project integrates with GitHub for user authentication and features a modular architecture with a focus on scalability and maintainability.

## Features

- **GitHub OAuth Integration**: Secure user login using GitHub OAuth.
- **Inbox Management**: View emails and organize them into folders.
- **Compose and Send Emails**: Create and send emails to other registered users.
- **Reply Feature**: Automatically populate subject and body when replying to emails.
- **Unread Count**: Tracks the number of unread messages in each folder.
- **Template-Based UI**: HTML templates for a responsive user interface.

## Project Structure

The project follows the standard Maven directory structure:

```
├── .mvn                    # Maven Wrapper Files
├── src
│   ├── main
│   │   ├── java
│   │   │   └── io          # Root Java package
│   │   ├── resources
│   │   │   ├── application.yml       # Spring Boot Configuration
│   │   │   ├── secure-connect.zip    # Database connection details
│   │   │   └── templates             # HTML templates
│   └── test                          # Test cases
├── pom.xml                # Maven Configuration File
├── README.md              # Project Documentation
```

### Key Directories

- ``: Contains the Java source code, including controllers, services, and repositories.
- ``:
  - `application.yml`: Spring Boot configuration file.
  - `templates`: Thymeleaf HTML templates for frontend pages.

## Setup Instructions

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- A GitHub account for OAuth integration
- (Optional) Docker for containerized deployment

### Steps

1. **Clone the Repository**:

   ```bash
   git clone <repository_url>
   cd messaging-app
   ```

2. **Configure Database**:

   - Place the `secure-connect.zip` file for your Cassandra/AstraDB instance in the `src/main/resources` folder.
   - Update the `application.yml` file with your database credentials.

3. **Build the Project**:

   ```bash
   mvn clean install
   ```

4. **Run the Application**:

   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application**: Open your browser and navigate to [http://localhost:8080](http://localhost:8080).

## Usage

- Log in using your GitHub account.
- Compose, reply to, and manage your messages directly in the web interface.
- Organize emails into folders for efficient management.

## Technologies Used

- **Spring Boot**: Backend framework
- **Thymeleaf**: Template engine for dynamic HTML pages
- **Cassandra/AstraDB**: Database for storing messages and user data
- **GitHub OAuth**: Authentication provider
- **Maven**: Build tool

## Future Improvements

- Add support for attachments.
- Implement a search functionality for emails.
- Improve responsiveness for mobile devices.

## Author

Developed by Farabi Seilbek.
