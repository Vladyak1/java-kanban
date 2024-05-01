package test.http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import main.managers.InMemoryTaskManager;
import main.managers.TaskManager;
import main.http.HttpTaskServer;
import main.model.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

class PrioritizedHandlerTest {

    private static final int PORT = 8080;
    private TaskManager taskManager = new InMemoryTaskManager();
    private HttpTaskServer server = new HttpTaskServer();

    @BeforeEach
    void setUp() {
        taskManager.clearAllTasks();
        taskManager.clearAllEpics();
        taskManager.clearAllSubtasks();
        HttpTaskServer.start(taskManager);
    }

    @AfterEach
    void shutDown() {
        server.stop();
    }

    @Test
    void handle() throws IOException, InterruptedException {
        taskManager.addTask(new Task("Задача 1", "Описание задачи 1", LocalDateTime.now(), Duration.ofMinutes(100)));
        URI uri = URI.create("http://localhost:" + PORT + "/prioritized");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.GET().uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1, response.body().split("},").length);
    }

}
