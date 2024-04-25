package test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import main.model.Task;
import main.service.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static Managers managers = new Managers();
    static Task task1 = new Task("Задача 1", "Описание задачи 1", "01.03.2024 19:00", 45);
    static Task task2 = new Task("Задача 2", "Описание задачи 2", "02.03.2024 19:00", 45);
    static Task task3 = new Task("Задача 3", "Описание задачи 3", "03.03.2024 19:00", 45);

    @BeforeAll
    public static void beforeAll() {
        Managers.getDefault().addTask(task1);
        Managers.getDefaultHistory().add(task1);
    }


    @Test
    public void shouldRecordHistory() {
        List<Task> history = Managers.getDefaultHistory().getHistoryList();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void shouldFindAndReturnEqualTasksById() {
        assertEquals(task1, managers.getDefaultHistory().getHistoryList().getFirst(), "Задачи не равны");
    }

    @Test
    void testEmptyHistory() {
        managers.getDefaultHistory().clear();
        assertTrue(managers.getDefaultHistory().getHistory().isEmpty(), "История задач должна быть пустой");
    }

    @Test
    void testDuplication() {
        managers.getDefault().addTask(task1);
        managers.getDefault().addTask(task1);

        assertEquals(1, managers.getDefaultHistory().getHistoryList().size(),
                "Дублирование задач в истории не должно приводить к увеличению размера истории");
    }

    @Test
    void testRemoveFromBeginning() {
        managers.getDefaultHistory().add(task1);
        managers.getDefaultHistory().add(task2);

        System.out.println(Managers.getDefaultHistory().getHistory());

        Managers.getDefaultHistory().remove(task1.getId());

        assertEquals(1, Managers.getDefaultHistory().getHistory().size(),
                "Удаление задачи из начала истории должно уменьшать размер истории");
        assertEquals(task2, Managers.getDefaultHistory().getHistory().get(0),
                "Порядок задач в истории должен сохраняться после удаления");
    }

    @Test
    void testRemoveFromMiddle() {
        managers.getDefault().addTask(task1);
        managers.getDefault().addTask(task2);
        managers.getDefault().addTask(task3);
        managers.getDefault().removeTask(task2.getId());

        assertEquals(2, managers.getDefaultHistory().getHistoryList().size(),
                "Удаление задачи из середины истории должно уменьшать размер истории");
        assertEquals(task1, managers.getDefaultHistory().getHistoryList().get(0),
                "Порядок задач в истории должен сохраняться после удаления");
        assertEquals(task3, managers.getDefaultHistory().getHistoryList().get(1),
                "Порядок задач в истории должен сохраняться после удаления");
    }

    @Test
    void testRemoveFromEnd() {
        managers.getDefault().addTask(task1);
        managers.getDefault().addTask(task2);

        managers.getDefault().removeTask(task2.getId());

        assertEquals(1, managers.getDefaultHistory().getHistoryList().size(),
                "Удаление задачи из конца истории должно уменьшать размер истории");
        assertEquals(task1, managers.getDefaultHistory().getHistoryList().get(0),
                "Порядок задач в истории должен сохраняться после удаления");
    }

}