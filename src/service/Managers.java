package service;

import java.io.File;

public class Managers {
    private final static InMemoryTaskManager taskManager = new InMemoryTaskManager();
    private final static InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    private final static FileBackedTaskManager fileManager = new FileBackedTaskManager();
    public static InMemoryTaskManager getDefault() {
        return taskManager;
    }
    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
    public static FileBackedTaskManager loadFromFile(File file) {
        return new FileBackedTaskManager(file);
    }
}
