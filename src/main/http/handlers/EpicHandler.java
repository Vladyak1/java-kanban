package main.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.managers.IntersectDurationTaskException;
import main.managers.ManagerSaveException;
import main.managers.TaskManager;
import main.model.Epic;
import main.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicHandler extends BasicHandler implements HttpHandler {
    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();
        InputStream inputStream = httpExchange.getRequestBody();
        String bodyString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        String info = "Запрос: метод - " + method + "; путь - "
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
            if (path.contains("/epics/") && path.split("/").length == 3) {
                int id = getIdFromPath(path);
                Optional<Task> optionalEpic = Optional.ofNullable(taskManager.getEpicById(id));
                if (optionalEpic.isEmpty()) {
                    responseCode = 404;
                    response = "Эпик с id: " + id + " не найден";
                } else {
                    responseCode = 200;
                    response = gson.toJson(optionalEpic.get());
                }
            } else if (path.contains("/epics/") && path.split("/")[3].equals("subtasks")) {
                int id = getIdFromPath(path);
                responseCode = 200;
                response = gson.toJson(taskManager.getSubtasksByEpicId(id));
            } else if (path.equals("/epics")) {
                responseCode = 200;
                response = gson.toJson(taskManager.getAllEpics());
            } else {
                responseCode = 404;
                response = "Неправильный путь запроса";
            }
        } catch (NumberFormatException exception) {
            responseCode = 404;
            response = "Неправильный id эпика";
        }
        sendResponse(httpExchange, responseCode, response);
    }

    private void handlePost(HttpExchange httpExchange, String bodyString) throws IOException {
        int responseCode;
        String response;
        try {
            Epic epicFromJson = gson.fromJson(bodyString, Epic.class);
            if (taskManager.getAllEpics().stream()
                    .anyMatch(task -> task.getId() == epicFromJson.getId())) {
                responseCode = 201;
                taskManager.updateEpic(epicFromJson);
                response = "Эпик с id: " + epicFromJson.getId() + " обновлен";
            } else {
                responseCode = 201;
                taskManager.addEpic(epicFromJson);
                response = "Эпик с id: " + epicFromJson.getId() + " успешно добавлен";
            }
        } catch (IntersectDurationTaskException exception) {
            responseCode = 406;
            response = "Задача пересекается по времени выполнения";
        } catch (ManagerSaveException exception) {
            responseCode = 500;
            response = "Ошибка сохранения данных менеджера в файл";
        }
        sendResponse(httpExchange, responseCode, response);
    }

    private void handleDelete(HttpExchange httpExchange, String query) throws IOException {
        int responseCode;
        String response;
        try {
            int id = Integer.parseInt(query.substring(3));
            if (taskManager.getAllEpics().stream()
                    .anyMatch(epic -> epic.getId() == id)) {
                taskManager.removeEpic(id);
                responseCode = 200;
                response = "Эпик c id: " + id + " удален";
            } else {
                responseCode = 404;
                response = "Эпик c id: " + id + " не существует";
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException exception) {
            responseCode = 404;
            response = "Неправильный id эпика";
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

    private int getIdFromPath(String path) {
        return Integer.parseInt(path.split("/")[2]);
    }
}
