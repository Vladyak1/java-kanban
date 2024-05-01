package test.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.utils.Status;
import main.model.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskHandlerTest extends BasicHandlerTest {
    private final String endpoint = "/tasks";
    private static Task task1;
    private static Task task2;
    private static Task task3 = new Task(33333, "Task 3", "Task 3", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(30));

    @BeforeEach
    void createEpicAndSubtask() {
        task1 = new Task(11111, "Task 1", "Task 1", Status.NEW, LocalDateTime.now().plusYears(1), Duration.ofMinutes(30));
        task2 = new Task(22222, "Task 2", "Task 2", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
    }

    @Test
    void testGetAllTasksSuccess() throws IOException, InterruptedException {
        HttpRequest request = createGetRequest(endpoint);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void testGetTaskByIdSuccess() throws IOException, InterruptedException {
        HttpRequest request = createGetRequest(endpoint + "/" + task1.getId());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void testGetTaskByIdNotFound() throws IOException, InterruptedException {
        HttpRequest request = createGetRequest(endpoint + "/" + invalidId);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    void testGetTaskByIdInvalidIdPath() throws IOException, InterruptedException {
        HttpRequest request = createGetRequest(endpoint + invalidPath);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    void testDeleteTaskInvalidId() throws IOException, InterruptedException {
        HttpRequest request = createDeleteRequest(endpoint, invalidId);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    void testHandleInvalidMethod() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .PUT(HttpRequest.BodyPublishers.ofString("test"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode());
    }
}