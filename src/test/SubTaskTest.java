package test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import main.model.SubTask;
import main.model.Epic;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {
    private static SubTask subtask1;

    @BeforeAll
    public static void createNewTasks() {
        subtask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", "01.06.2024 11:00", 45);
    }


    @Test
    public void subtaskShouldNotCreateLikeEpic() {
        assertFalse(subtask1 instanceof Epic);
    }
}