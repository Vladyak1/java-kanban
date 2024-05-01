package main.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.managers.IntersectDurationTaskException;
import main.managers.ManagerSaveException;
import main.managers.TaskManager;
import main.model.Subtask;
import main.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubtaskHandler extends BasicHandler implements HttpHandler {
    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();
        InputStream inputStream = httpExchange.getRequestBody();
        String bodyString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        String info = "Запрос: метод - " + method + "; путь -  "
                + path + "; параметры строки запроса - " + query + "; тело - " + bodyString;
        System.out.println(info);

        int responseCode;
        String response;

        switch (method) {
            case "GET":
                handleGet(httpExchange, path);
                break;
            case "POST":
                handlePost(httpExchange, bodyString);
                break;
            case "DELETE":
                handleDelete(httpExchange, query);
                break;
            default:
                responseCode = 405;
                response = "Метод не поддерживается";
                sendResponse(httpExchange, responseCode, response);
                break;
        }
    }

    private void handleGet(HttpExchange httpExchange, String path) throws IOException {
        int responseCode;
        String response;
        try {
            if (path.contains("/subtasks/") && path.split("/").length == 3) {
                int id = Integer.parseInt(path.split("/")[2]);
                Optional<Task> subtaskOptional = Optional.ofNullable(taskManager.getSubtaskById(id));
                if (subtaskOptional.isEmpty()) {
                    responseCode = 404;
                    response = "Подзадача с id: " + id + " не найдена";
                } else {
                    responseCode = 200;
                    response = gson.toJson(subtaskOptional.get());
                }
            } else if (path.equals("/subtasks")) {
                responseCode = 200;
                response = gson.toJson(taskManager.getAllSubtasks());
            } else {
                responseCode = 404;
                response = "Неправильный путь запроса";
            }
        } catch (NumberFormatException exception) {
            responseCode = 404;
            response = "Неправильный id подзадачи";
        }
        sendResponse(httpExchange, responseCode, response);
    }

    private void handlePost(HttpExchange httpExchange, String bodyString) throws IOException {
        int responseCode;
        String response;
        try {
            Subtask subtaskFromJson = gson.fromJson(bodyString, Subtask.class);
            if (taskManager.getAllSubtasks().stream()
                    .anyMatch(subtask -> subtask.getId() == subtaskFromJson.getId())) {
                responseCode = 201;
                taskManager.updateSubtask(subtaskFromJson);
                response = "Подзадача с id: " + subtaskFromJson.getId() + " обновлена";
            } else {
                responseCode = 201;
                taskManager.addSubtask(subtaskFromJson, taskManager.getEpicBySubtask(subtaskFromJson));
                response = "Подзадача с id: " + subtaskFromJson.getId() + " успешно добавлена";
            }
        } catch (IntersectDurationTaskException exception) {
            responseCode = 406;
            response = "Задача пересекается по времени выполнения";
        } catch (ManagerSaveException exception) {
            responseCode = 500;
            response = "Ошибка сохранения данных менеджера в файл";
        }
        System.out.println(responseCode);
        sendResponse(httpExchange, responseCode, response);
    }

    private void handleDelete(HttpExchange httpExchange, String query) throws IOException {
        int responseCode;
        String response;
        try {
            int id = Integer.parseInt(query.substring(3));
            if (taskManager.getAllSubtasks().stream()
                    .anyMatch(subtask -> subtask.getId() == id)) {
                taskManager.removeSubtask(id);
                responseCode = 200;
                response = "Подзадача c id: " + id + " удалена";
            } else {
                responseCode = 404;
                response = "Подзадача c id: " + id + " не существует";

            }
        } catch (NumberFormatException exception) {
            responseCode = 404;
            response = "Неправильный id подзадачи";
        }
        sendResponse(httpExchange, responseCode, response);
    }

    private void sendResponse(HttpExchange httpExchange, int responseCode, String response) throws IOException {
        httpExchange.sendResponseHeaders(responseCode, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("Ответ: Код - " + responseCode + "; тело ответа - " + response);
    }
}
