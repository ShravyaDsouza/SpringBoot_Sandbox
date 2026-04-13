package example.service;

import example.model.Task;
import example.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void shouldAddTaskWithGivenPriority() {
        when(taskRepository.findAll()).thenReturn(new ArrayList<>());

        Task task = taskService.addTask("Buy milk", "high");

        assertNotNull(task);
        assertEquals(1, task.getId());
        assertEquals("Buy milk", task.getDescription());
        assertEquals("todo", task.getStatus());
        assertEquals("high", task.getPriority());
        assertNotNull(task.getCreatedAt());
        assertNotNull(task.getUpdatedAt());

        verify(taskRepository, times(1)).saveAll(anyList());
    }

    @Test
    void shouldAddTaskWithDefaultMediumPriorityWhenPriorityMissing() {
        when(taskRepository.findAll()).thenReturn(new ArrayList<>());

        Task task = taskService.addTask("Finish report", null);

        assertEquals("medium", task.getPriority());
        verify(taskRepository).saveAll(anyList());
    }

    @Test
    void shouldGenerateNextIdBasedOnExistingTasks() {
        List<Task> existingTasks = new ArrayList<>();

        Task t1 = new Task(1, "Task 1", "todo", "low",
                LocalDateTime.now(), LocalDateTime.now());
        Task t2 = new Task(4, "Task 2", "done", "high",
                LocalDateTime.now(), LocalDateTime.now());

        existingTasks.add(t1);
        existingTasks.add(t2);

        when(taskRepository.findAll()).thenReturn(existingTasks);

        Task newTask = taskService.addTask("New Task", "medium");

        assertEquals(5, newTask.getId());
        verify(taskRepository).saveAll(existingTasks);
    }

    @Test
    void shouldReturnAllTasks() {
        List<Task> tasks = List.of(
                new Task(1, "A", "todo", "high", LocalDateTime.now(), LocalDateTime.now()),
                new Task(2, "B", "done", "low", LocalDateTime.now(), LocalDateTime.now())
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        assertEquals(tasks, result);
    }

    @Test
    void shouldReturnTasksByStatus() {
        List<Task> tasks = List.of(
                new Task(1, "A", "todo", "high", LocalDateTime.now(), LocalDateTime.now()),
                new Task(2, "B", "done", "low", LocalDateTime.now(), LocalDateTime.now()),
                new Task(3, "C", "done", "medium", LocalDateTime.now(), LocalDateTime.now())
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getTasksByStatus("done");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(task -> task.getStatus().equalsIgnoreCase("done")));
    }

    @Test
    void shouldThrowForInvalidStatus() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                taskService.getTasksByStatus("completed")
        );

        assertEquals("Invalid status. Use: todo, in-progress, done", exception.getMessage());
    }

    @Test
    void shouldReturnTasksByPriority() {
        List<Task> tasks = List.of(
                new Task(1, "A", "todo", "high", LocalDateTime.now(), LocalDateTime.now()),
                new Task(2, "B", "done", "low", LocalDateTime.now(), LocalDateTime.now()),
                new Task(3, "C", "todo", "high", LocalDateTime.now(), LocalDateTime.now())
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getTasksByPriority("high");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(task -> task.getPriority().equalsIgnoreCase("high")));
    }

    @Test
    void shouldFilterByStatusOnly() {
        List<Task> tasks = List.of(
                new Task(1, "A", "todo", "high", LocalDateTime.now(), LocalDateTime.now()),
                new Task(2, "B", "done", "low", LocalDateTime.now(), LocalDateTime.now()),
                new Task(3, "C", "todo", "medium", LocalDateTime.now(), LocalDateTime.now())
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.filterTasks("todo", null);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(task -> task.getStatus().equalsIgnoreCase("todo")));
    }

    @Test
    void shouldFilterByPriorityOnly() {
        List<Task> tasks = List.of(
                new Task(1, "A", "todo", "high", LocalDateTime.now(), LocalDateTime.now()),
                new Task(2, "B", "done", "low", LocalDateTime.now(), LocalDateTime.now()),
                new Task(3, "C", "todo", "high", LocalDateTime.now(), LocalDateTime.now())
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.filterTasks(null, "high");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(task -> task.getPriority().equalsIgnoreCase("high")));
    }

    @Test
    void shouldFilterByStatusAndPriority() {
        List<Task> tasks = List.of(
                new Task(1, "A", "todo", "high", LocalDateTime.now(), LocalDateTime.now()),
                new Task(2, "B", "todo", "low", LocalDateTime.now(), LocalDateTime.now()),
                new Task(3, "C", "done", "high", LocalDateTime.now(), LocalDateTime.now())
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.filterTasks("todo", "high");

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void shouldUpdateTaskDescriptionOnly() {
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now().minusHours(1);

        Task task = new Task(1, "Old desc", "todo", "medium", created, updated);
        List<Task> tasks = new ArrayList<>(List.of(task));

        when(taskRepository.findAll()).thenReturn(tasks);

        Task updatedTask = taskService.updateTask(1, "New desc", null);

        assertEquals("New desc", updatedTask.getDescription());
        assertEquals("medium", updatedTask.getPriority());
        assertTrue(updatedTask.getUpdatedAt().isAfter(updated));

        verify(taskRepository).saveAll(tasks);
    }

    @Test
    void shouldUpdateTaskPriorityOnly() {
        Task task = new Task(
                1,
                "Old desc",
                "todo",
                "medium",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusHours(1)
        );

        List<Task> tasks = new ArrayList<>(List.of(task));
        when(taskRepository.findAll()).thenReturn(tasks);

        Task updatedTask = taskService.updateTask(1, null, "high");

        assertEquals("Old desc", updatedTask.getDescription());
        assertEquals("high", updatedTask.getPriority());

        verify(taskRepository).saveAll(tasks);
    }

    @Test
    void shouldUpdateTaskDescriptionAndPriority() {
        Task task = new Task(
                1,
                "Old desc",
                "todo",
                "medium",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusHours(1)
        );

        List<Task> tasks = new ArrayList<>(List.of(task));
        when(taskRepository.findAll()).thenReturn(tasks);

        Task updatedTask = taskService.updateTask(1, "New desc", "low");

        assertEquals("New desc", updatedTask.getDescription());
        assertEquals("low", updatedTask.getPriority());

        verify(taskRepository).saveAll(tasks);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingTask() {
        when(taskRepository.findAll()).thenReturn(new ArrayList<>());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                taskService.updateTask(99, "New desc", null)
        );

        assertEquals("Task not found with ID: 99", exception.getMessage());
    }

    @Test
    void shouldDeleteTask() {
        Task task1 = new Task(1, "A", "todo", "high", LocalDateTime.now(), LocalDateTime.now());
        Task task2 = new Task(2, "B", "done", "low", LocalDateTime.now(), LocalDateTime.now());

        List<Task> tasks = new ArrayList<>(List.of(task1, task2));

        when(taskRepository.findAll()).thenReturn(tasks);

        taskService.deleteTask(1);

        assertEquals(1, tasks.size());
        assertEquals(2, tasks.get(0).getId());
        verify(taskRepository).saveAll(tasks);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingTask() {
        when(taskRepository.findAll()).thenReturn(new ArrayList<>());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                taskService.deleteTask(10)
        );

        assertEquals("Task not found with ID :10", exception.getMessage());
    }

    @Test
    void shouldMarkTaskInProgress() {
        Task task = new Task(
                1,
                "A",
                "todo",
                "medium",
                LocalDateTime.now(),
                LocalDateTime.now().minusHours(1)
        );
        List<Task> tasks = new ArrayList<>(List.of(task));

        when(taskRepository.findAll()).thenReturn(tasks);

        Task updatedTask = taskService.markTaskInProgress(1);

        assertEquals("in-progress", updatedTask.getStatus());
        verify(taskRepository).saveAll(tasks);
    }

    @Test
    void shouldMarkTaskDone() {
        Task task = new Task(
                1,
                "A",
                "in-progress",
                "medium",
                LocalDateTime.now(),
                LocalDateTime.now().minusHours(1)
        );
        List<Task> tasks = new ArrayList<>(List.of(task));

        when(taskRepository.findAll()).thenReturn(tasks);

        Task updatedTask = taskService.markTaskDone(1);

        assertEquals("done", updatedTask.getStatus());
        verify(taskRepository).saveAll(tasks);
    }

    @Test
    void shouldThrowWhenMarkingNonExistingTask() {
        when(taskRepository.findAll()).thenReturn(new ArrayList<>());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                taskService.markTaskDone(42)
        );

        assertEquals("Task not found with ID: 42", exception.getMessage());
    }
}