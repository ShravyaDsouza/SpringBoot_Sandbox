# Spring Boot CLI Task Tracker

**Project Reference:** [roadmap.sh - Task Tracker](https://roadmap.sh/projects/task-tracker)

Task tracker is a project used to track and manage your tasks. This project provides a simple command-line interface (CLI) application built with Spring Boot to track what you need to do, what you have done, and what you are currently working on. Tasks are saved persistently in a local `tasks.json` file.

## Task Properties

Each task has the following properties:

- **id**: A unique identifier for the task
- **description**: A short description of the task
- **status**: The status of the task (`todo`, `in-progress`, `done`)
- **priority**: The priority of the task (`low`, `medium`, `high`) (Custom addition)
- **createdAt**: The date and time when the task was created
- **updatedAt**: The date and time when the task was last updated

## Folder Structure

```text
task-tracker
├── pom.xml
├── tasks.json
├── README.md
└── src/main/java/example
    ├── TaskTrackerApplication.java
    ├── cli
    │   ├── ParsedCommand.java
    │   └── TaskCliRunner.java
    ├── model
    │   └── Task.java
    ├── repository
    │   └── TaskRepository.java
    ├── service
    │   └── TaskService.java
    └── util
        └── CommandParser.java
```

## Features

- Add a new task (with optional priority).
- Update an existing task's description and/or priority.
- Delete a task.
- Mark a task as `in-progress` or `done`.
- List all tasks with optional filtering by status and/or priority.

## Usage

This is a Spring Boot CLI runner, so commands can be passed as arguments when executing the application.
Arguments are parsed using a custom `CommandParser` supporting `--key=value` format.

### Commands

**1. Add a Task**
```bash
add --desc="task description" [--priority=high|medium|low]
```
*(Default priority is `medium` if not specified)*

**2. Update a Task**
```bash
update --id=1 [--desc="new description"] [--priority=high|medium|low]
```

**3. Delete a Task**
```bash
delete --id=1
```

**4. Mark Task In-Progress**
```bash
mark-in-progress --id=1
```

**5. Mark Task Done**
```bash
mark-done --id=1
```

**6. List Tasks**
```bash
list
```
Filtering options:
```bash
list --status=done
list --status=todo
list --status=in-progress
list --priority=high
list --status=todo --priority=high
```

## Setup and Run

To run the application using Maven:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="list"
```
Or you can package and run:
```bash
mvn clean package
java -jar target/tasktracker-0.0.1-SNAPSHOT.jar list
```

## Storage
Data is saved persistently to `tasks.json` in the root directory.