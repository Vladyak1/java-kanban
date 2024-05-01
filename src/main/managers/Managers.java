package main.managers;

import java.io.File;

public class Managers {
    private final static InMemoryTaskManager taskManager = new InMemoryTaskManager();
    private final static InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    private final static FileBackedTaskManager fileManager = new FileBackedTaskManager(new File("src\\service\\data.csv"));
    public static InMemoryTaskManager getDefault() {
        return taskManager;
    }
    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
    public static TaskManager loadFromFile(File file) {
        return FileBackedTaskManager.loadFromFile(file);
    }
}
