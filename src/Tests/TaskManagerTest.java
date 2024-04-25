package Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Epic;
import model.SubTask;
import model.Task;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager = (T) new InMemoryTaskManager();
    Task task1 = new Task("Задача 1", "Описание задачи 1", "01.03.2024 19:00", 45);
    Task task2 = new Task("Задача 2", "Описание задачи 2", "01.03.2024 20:00", 45);
    Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
    Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
    SubTask subtask1 = new SubTask("Подзадача 1", "Описание подзадачи 1",
            "02.03.2024 11:00", 45);
    SubTask subtask2 = new SubTask("Подзадача 2", "Описание подзадачи 2",
            "02.04.2024 12:00", 45);

    @BeforeEach
    abstract void setUp();

    @BeforeEach
    void clearFieldsAfterTest() {
        taskManager.clearAllTasks();
        taskManager.clearAllSubTasks();
        taskManager.clearAllEpics();
    }

    @Test
    void testAddTask() {
        taskManager.addTask(task1);
        assertEquals(task1, taskManager.getTaskById(task1.getId()));
    }

    @Test
    void testAddEpic() {
        taskManager.addEpic(epic1);
        assertEquals(epic1, taskManager.getEpicById(epic1.getId()));
    }

    @Test
    void testAddSubtask() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subtask1, epic1);
        assertEquals(subtask1, taskManager.getSubTaskById(subtask1.getId()));
    }

    @Test
    void testDeleteAllTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.clearAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void testDeleteAllEpics() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.clearAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    void testDeleteAllSubtasks() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subtask1, epic1);
        taskManager.addSubTask(subtask2, epic1);
        taskManager.clearAllSubTasks();
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test
    void testDeleteTaskByID() {
        taskManager.addTask(task1);
        taskManager.removeTask(task1.getId());
        assertNull(taskManager.getTaskById(task1.getId()));
    }

    @Test
    void testDeleteEpicByID() {
        taskManager.addEpic(epic1);
        taskManager.removeEpic(epic1.getId());
        assertNull(taskManager.getEpicById(epic1.getId()));
    }

    @Test
    void testDeleteSubtaskByID() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subtask1, epic1);
        taskManager.removeSubTask(subtask1.getId());
        assertNull(taskManager.getSubTaskById(subtask1.getId()));
    }

    @Test
    void testGetTaskByID() {
        taskManager.addTask(task1);
        assertEquals(task1, taskManager.getTaskById(task1.getId()));
    }

    @Test
    void testGetEpicByID() {
        taskManager.addEpic(epic1);
        assertEquals(epic1, taskManager.getEpicById(epic1.getId()));
    }

    @Test
    void testGetSubtaskByID() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subtask1, epic1);
        assertEquals(subtask1, taskManager.getSubTaskById(subtask1.getId()));
    }

    @Test
    void testGetAllTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void testGetAllEpics() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        List<Epic> epics = taskManager.getAllEpics();
        assertEquals(2, epics.size());
        assertTrue(epics.contains(epic1));
        assertTrue(epics.contains(epic2));
    }

    @Test
    void testGetAllSubtasks() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subtask1, epic1);
        taskManager.addSubTask(subtask2, epic1);
        List<SubTask> subtasks = taskManager.getAllSubTasks();
        assertEquals(2, subtasks.size());
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask2));
    }

    @Test
    void testGetPrioritizedTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subtask1, epic1);
        taskManager.addSubTask(subtask2, epic1);

        List<Task> prioritizedTasks = new ArrayList<>(taskManager.getPrioritizedTasks());

        assertEquals(4, prioritizedTasks.size());
        assertEquals(task1, prioritizedTasks.get(0));
        assertEquals(task2, prioritizedTasks.get(1));
        assertEquals(epic1, prioritizedTasks.get(2));
        assertEquals(subtask2, prioritizedTasks.get(3));

    }


}
