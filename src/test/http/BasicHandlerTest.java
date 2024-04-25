package test.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import main.http.HttpTaskServer;
import main.http.adapters.TaskDeserializer;
import main.http.adapters.TaskSerializer;
import main.service.TaskManager;
import main.model.Task;
import main.service.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

abstract class BasicHandlerTest {
    private final int PORT = 8080;
    protected final String BASE_URL = "http://localhost:" + PORT;
    protected TaskManager taskManager = Managers.getDefault();
    protected HttpClient client;
    protected int invalidId = 9999999;
    protected String invalidPath = "/invalid/path";
    protected Gson gson = new GsonBuilder().serializeNulls()
            .setPrettyPrinting()
            .registerTypeHierarchyAdapter(Task.class, new TaskSerializer<>())
            .registerTypeHierarchyAdapter(Task.class, new TaskDeserializer())
            .create();

    protected HttpRequest createGetRequest(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .build();
    }

    protected HttpRequest createPostRequest(String endpoint, Object body) {
        String jsonBody = gson.toJson(body);
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
    }

    protected HttpRequest createDeleteRequest(String endpoint, int id) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint + "?id=" + id))
                .DELETE()
                .build();
    }

    @BeforeEach
    void startServer() throws IOException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void stopServer() {
        HttpTaskServer.stopHttpTaskServer();
    }
}
