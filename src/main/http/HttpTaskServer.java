package main.http;

import com.sun.net.httpserver.HttpServer;
import main.service.Managers;
import main.service.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer httpServer;

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.loadFromFile(new File("src\\main.service\\data.csv"));
        try {
            startHttpTaskServer(manager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stop(9999);
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

    public static void stop(int timeout) {
        httpServer.stop(timeout);
    }
}
