package example.repository;

import example.model.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {

    private static final String FILE_NAME = "tasks.json";
    private final ObjectMapper objectMapper;

    public TaskRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    public List<Task> findAll() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<Task>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read tasks from file.", e);
        }
    }

    public void saveAll(List<Task> tasks) {
        File file = new File(FILE_NAME);

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, tasks);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save tasks to file.", e);
        }
    }
}