package http.Tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Status;
import model.Task;

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

    @BeforeEach
    void createEpicAndSubtask() {
        task1 = new Task(11111, "Task 1", "Task 1", Status.NEW, LocalDateTime.now().plusYears(1), Duration.ofMinutes(30));
        task2 = new Task(22222, "Task 2", "Task 2", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
    }

    @AfterEach
    void clear() {
        taskManager.clearAllTasks();
        taskManager.clearAllEpics();
        taskManager.clearAllSubTasks();
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
    void testAddTaskSuccess() throws IOException, InterruptedException {
        HttpRequest request = createPostRequest(endpoint, task1);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void testUpdateTaskSuccess() throws IOException, InterruptedException {
        HttpRequest request = createPostRequest(endpoint, task1);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void testDeleteTaskSuccess() throws IOException, InterruptedException {
        taskManager.addTask(task1);
        HttpRequest request = createDeleteRequest(endpoint, task1.getId());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
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

    @Test
    void testInterruptedTasks() throws IOException, InterruptedException {
        task2.setStartTime(task1.getStartTime());
        HttpRequest request = createPostRequest(endpoint, task2);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
    }
}
