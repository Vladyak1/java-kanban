package main.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.exceptions.IntersectDurationTaskException;
import main.exceptions.ManagerSaveException;
import main.service.TaskManager;
import main.model.SubTask;

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
                Optional<SubTask> subtaskOptional = Optional.ofNullable(taskManager.getSubTaskById(id));
                if (subtaskOptional.isEmpty()) {
                    responseCode = 404;
                    response = "Подзадача с id: " + id + " не найдена";
                } else {
                    responseCode = 200;
                    response = gson.toJson(subtaskOptional.get());
                }
            } else if (path.equals("/subtasks")) {
                responseCode = 200;
                response = gson.toJson(taskManager.getAllSubTasks());
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
            SubTask subtaskFromJson = gson.fromJson(bodyString, SubTask.class);
            if (taskManager.getSubTaskById(subtaskFromJson.getId()) != null) {
                responseCode = 201;
                taskManager.updateSubTask(subtaskFromJson);
                response = "Подзадача с id: " + subtaskFromJson.getId() + " обновлена";
            } else {
                responseCode = 201;
                taskManager.addSubTask(subtaskFromJson, taskManager.getEpicBySubTask(subtaskFromJson));
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
            if (taskManager.getSubTaskById(id) == null) {
                responseCode = 404;
                response = "Подзадача c id: " + id + " не существует";
            } else {
                taskManager.removeSubTask(id);
                responseCode = 200;
                response = "Подзадача c id: " + id + " удалена";
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
