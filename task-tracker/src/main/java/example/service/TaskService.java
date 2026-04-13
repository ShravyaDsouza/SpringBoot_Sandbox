package example.service;

import example.model.Task;
import example.repository.TaskRepository;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    private static final List<String> VALID_STATUSES = List.of("todo", "in-progress","done");
    private static final List<String> VALID_PRIORITIES = List.of("low", "medium", "high");

    public Task addTask(String description, String priority) {
        List<Task> tasks = taskRepository.findAll();

        int newId = tasks.stream()
                .mapToInt(Task::getId)
                .max()
                .orElse(0) + 1;

        LocalDateTime now = LocalDateTime.now();

        Task task = new Task();
        task.setId(newId);
        task.setDescription(description);
        task.setStatus("todo");
        String finalPriority = (priority == null || priority.isBlank()) ? "medium" : priority.toLowerCase();
        if (!VALID_PRIORITIES.contains(finalPriority)) {
            throw new RuntimeException("Invalid priority. Use: low, medium, high");
        }
        task.setPriority(finalPriority);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);

        tasks.add(task);
        taskRepository.saveAll(tasks);

        return task;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByStatus(String status){
        if (!VALID_STATUSES.contains(status.toLowerCase())) {
            throw new RuntimeException("Invalid status. Use: todo, in-progress, done");
        }

        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    public List<Task> getTasksByPriority(String priority) {
        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getPriority().equalsIgnoreCase(priority))
                .toList();
    }

    public List<Task> filterTasks(String status, String priority) {
        return taskRepository.findAll().stream()
            .filter(t -> status == null || (t.getStatus()!=null && t.getStatus().equalsIgnoreCase(status)))
            .filter(t -> priority == null || (t.getPriority()!= null && t.getPriority().equalsIgnoreCase(priority)))
            .toList();
    }

    public Task updateTask(int id, String newDescription, String newPriority) {
        List<Task> tasks = taskRepository.findAll();

        for (Task task : tasks) {
            if (task.getId() == id) {

                if (newDescription != null && !newDescription.isBlank()) {
                    task.setDescription(newDescription);
                }

                if (newPriority != null && !newPriority.isBlank()) {
                    String priority = newPriority.toLowerCase();

                    List<String> valid = List.of("low", "medium", "high");
                    if (!valid.contains(priority)) {
                        throw new RuntimeException("Invalid priority. Use: low, medium, high");
                    }

                    task.setPriority(priority);
                }

                task.setUpdatedAt(LocalDateTime.now());
                taskRepository.saveAll(tasks);
                return task;
            }
        }

        throw new RuntimeException("Task not found with ID: " + id);
    }

    public void deleteTask(int id){
        List<Task> tasks = taskRepository.findAll();

        boolean removed = tasks.removeIf(task -> task.getId() == id);

        if(!removed){
            throw new RuntimeException("Task not found with ID :"+id);
        }
        taskRepository.saveAll(tasks);
    }
    private Task updateTaskStatus(int id, String status) {
            List<Task> tasks = taskRepository.findAll();

            for (Task task : tasks) {
                if (task.getId() == id) {
                    task.setStatus(status);
                    task.setUpdatedAt(LocalDateTime.now());
                    taskRepository.saveAll(tasks);
                    return task;
                }
            }

            throw new RuntimeException("Task not found with ID: " + id);
        }
    public Task markTaskInProgress(int id){
        return updateTaskStatus(id,"in-progress");
    }

    public Task markTaskDone(int id){
        return updateTaskStatus(id,"done");
    }
}