package example.cli;

import example.model.Task;
import example.service.TaskService;
import example.util.CommandParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskCliRunner implements CommandLineRunner {

    private final TaskService taskService;
    private final CommandParser commandParser;

    public TaskCliRunner(TaskService taskService, CommandParser commandParser) {
        this.taskService = taskService;
        this.commandParser = commandParser;
    }

    @Override
    public void run(String... args) {
        try {
            ParsedCommand parsedCommand = commandParser.parse(args);
            String command = parsedCommand.getCommandName();

            switch (command) {
                case "add" -> handleAdd(parsedCommand);
                case "update" -> handleUpdate(parsedCommand);
                case "delete" -> handleDelete(parsedCommand);
                case "mark-in-progress" -> handleMarkInProgress(parsedCommand);
                case "mark-done" -> handleMarkDone(parsedCommand);
                case "list" -> handleList(parsedCommand);
                default -> {
                    System.out.println("Unknown command: " + command);
                    printUsage();
                }
            }
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            printUsage();
        }
    }

    private void handleAdd(ParsedCommand parsedCommand) {
        String description = parsedCommand.getOption("desc");

        if (description == null || description.isBlank()) {
            throw new RuntimeException("Missing required option: --desc");
        }

        String priority = parsedCommand.getOption("priority");

        Task task = taskService.addTask(description, priority);
        System.out.println("Task added successfully (ID: " + task.getId() + ")");
    }

    private void handleUpdate(ParsedCommand parsedCommand) {
        String idValue = parsedCommand.getOption("id");
        String newDescription = parsedCommand.getOption("desc");
        String newPriority = parsedCommand.getOption("priority");

        if (idValue == null || idValue.isBlank()) {
            throw new RuntimeException("Missing required option: --id");
        }

        if ((newDescription == null || newDescription.isBlank()) &&
            (newPriority == null || newPriority.isBlank())) {
            throw new RuntimeException("Provide at least one field: --desc or --priority");
        }

        int id = Integer.parseInt(idValue);

        Task updatedTask = taskService.updateTask(id, newDescription, newPriority);
        System.out.println("Task updated successfully (ID: " + updatedTask.getId() + ")");
    }

    private void handleDelete(ParsedCommand parsedCommand) {
        String idValue = parsedCommand.getOption("id");

        if (idValue == null || idValue.isBlank()) {
            throw new RuntimeException("Missing required option: --id");
        }

        int id = Integer.parseInt(idValue);
        taskService.deleteTask(id);

        System.out.println("Task deleted successfully (ID: " + id + ")");
    }

    private void handleMarkInProgress(ParsedCommand parsedCommand) {
        String idValue = parsedCommand.getOption("id");

        if (idValue == null || idValue.isBlank()) {
            throw new RuntimeException("Missing required option: --id");
        }

        int id = Integer.parseInt(idValue);
        Task task = taskService.markTaskInProgress(id);

        System.out.println("Task marked as in-progress (ID: " + task.getId() + ")");
    }

    private void handleMarkDone(ParsedCommand parsedCommand) {
        String idValue = parsedCommand.getOption("id");

        if (idValue == null || idValue.isBlank()) {
            throw new RuntimeException("Missing required option: --id");
        }

        int id = Integer.parseInt(idValue);
        Task task = taskService.markTaskDone(id);

        System.out.println("Task marked as done (ID: " + task.getId() + ")");
    }

    private void handleList(ParsedCommand parsedCommand) {
        List<Task> tasks;

        String status = parsedCommand.getOption("status");
        String priority = parsedCommand.getOption("priority");
        if ((status == null || status.isBlank()) && (priority == null || priority.isBlank())) {
            tasks = taskService.getAllTasks();
        } else {
            tasks = taskService.filterTasks(status,priority);
        }

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        for (Task task : tasks) {
            System.out.println(
                    task.getId() + " | " +
                    task.getDescription() + " | " +
                    task.getStatus() + " | " +
                    task.getPriority() + " | " +
                    task.getCreatedAt() + " | " +
                    task.getUpdatedAt()
            );
        }
    }

    private void printUsage() {
        System.out.println("""
                Usage:
                  add --desc="task description" --priority=high
                  update --id=1 --desc="new description"
                  delete --id=1
                  mark-in-progress --id=1
                  mark-done --id=1
                  list
                  list --status=done
                  list --status=todo
                  list --status=in-progress
                """);
    }
}