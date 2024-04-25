package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.adapters.TaskDeserializer;
import http.adapters.TaskSerializer;
import service.TaskManager;
import model.Task;

import java.io.IOException;

public abstract class BasicHandler implements HttpHandler {
    protected TaskManager taskManager;
    protected Gson gson = new GsonBuilder().serializeNulls()
            .setPrettyPrinting()
            .registerTypeHierarchyAdapter(Task.class, new TaskSerializer<>())
            .registerTypeHierarchyAdapter(Task.class, new TaskDeserializer())
            .create();

    public BasicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
    }
}
