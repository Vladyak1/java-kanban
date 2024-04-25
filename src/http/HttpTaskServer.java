package http;

import com.sun.net.httpserver.HttpServer;
import service.Managers;
import service.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer httpServer;

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.loadFromFile(new File("src\\service\\data.csv"));
        startHttpTaskServer(manager);
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        stopHttpTaskServer();
    }

    public static void startHttpTaskServer(TaskManager manager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(manager));
        httpServer.createContext("/epics", new EpicHandler(manager));
        httpServer.createContext("/subtasks", new SubtaskHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
        httpServer.start();
        System.out.println("Сервер запущен на порту: " + PORT);
    }

    public static void stopHttpTaskServer() {
        httpServer.stop(0);
        System.out.println("Сервер завершил работу!");
    }
}
