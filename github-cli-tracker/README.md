# Spring Boot CLI GitHub User Activity Tracker

**Project Reference:** [roadmap.sh - GitHub User Activity](https://roadmap.sh/projects/github-user-activity)

GitHub User Activity Tracker is a project used to monitor the recent activity of any GitHub user. This project provides a simple command-line interface (CLI) application built with Spring Boot to fetch the recent activity using the GitHub API and display it directly in the terminal, helping you keep track of commits, repository creations, and other user actions. 

## Features

- Fetch the recent activity of the specified GitHub user using the GitHub API.
- Display the fetched activity smoothly in the terminal.
- Handles errors gracefully, such as invalid usernames or API limitations such as rate limits and event latency inherent to GitHub’s Events API.
- Executes operations directly via HTTP against the live `api.github.com` endpoint without the use of external HTTP wrapper frameworks.
- Parses and categorizes GitHub event types (e.g., PushEvent, CreateEvent) into human-readable output.
- Designed with a modular architecture separating CLI handling, service logic, and data models.

## Folder Structure

```text
github-cli-tracker
├── pom.xml
├── README.md
└── src/main/java/example
    ├── GithubActivity.java
    ├── cli
    │   └── ActivityCliRunner.java
    ├── model
    │   └── GithubEvent.java
    └── service
        └── ActivityService.java
```

## Usage

This is a Spring Boot CLI runner, so commands can be passed as arguments when executing the application.
Provide the GitHub username as an argument when running the CLI to fetch the user's recent activity using the following endpoint:
`https://api.github.com/users/<username>/events`

### Commands

**Fetch User Activity**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="ShravyaDsouza"
```

### Example Output
Based on the present endpoint, it displays:
- Pushed to ShravyaDsouza/SpringBoot_Sandbox
- Pushed to ShravyaDsouza/Python
- Pushed to ShravyaDsouza/The-Kafka-Suite
- Created a new branch in ShravyaDsouza/SpringBoot_Sandbox
- Created a new branch in ShravyaDsouza/The-Kafka-Suite

## Setup and Run

To run the application using Maven:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="ShravyaDsouza"
```
Or you can package and run:
```bash
mvn clean package
java -jar target/github-cli-tracker-0.0.1-SNAPSHOT.jar ShravyaDsouza
```

## Future Enhancements
- Develop a v2 GraphQL-powered edition to support contribution calendars, repository-level analytics, and enhanced activity tracking.
