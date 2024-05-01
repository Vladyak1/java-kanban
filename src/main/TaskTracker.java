package main;

import main.model.*;
import main.utils.Status;
import main.managers.InMemoryTaskManager;

class TaskTracker {

        public static void main(String[] args) {
                InMemoryTaskManager taskManager = new InMemoryTaskManager();
                Task task1 = new Task("Задача 1", "Описание задачи 1", "01.03.2024 19:00", 45);
                Task task2 = new Task("Задача 2", "Описание задачи 2", "02.03.2024 20:00", 45);

                Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
                Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

                Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", "01.06.2024 11:00", 45);
                Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", "01.06.2024 12:00", 45);
                Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", "01.06.2024 13:00", 45);
                Subtask subtask4 = new Subtask("Подзадача 4", "Описание подзадачи 4", "01.06.2024 14:00", 45);


                taskManager.addTask(task1);
                taskManager.addTask(task2);
                taskManager.addEpic(epic1);
                taskManager.addEpic(epic2);
                taskManager.addSubtask(subtask1, epic1);
                taskManager.addSubtask(subtask2, epic1);
                taskManager.addSubtask(subtask3, epic2);
                taskManager.addSubtask(subtask4, epic2);
                System.out.println("Сначала вывели все задачи по возрастанию id");
                System.out.println(taskManager.getAllTasks());
                System.out.println();
                System.out.println("Далее вывели каждый Эпик");
                System.out.println(taskManager.getAllEpics());
                System.out.println();
                System.out.println("Далее вывели каждую подзадачу");
                System.out.println(taskManager.getAllSubtasks());
                System.out.println();
                System.out.println("Вывели обновленную задачу 1");
                taskManager.updateTask(1, "Новое название задачи 1",
                        "Новое описание задачи 1", Status.IN_PROGRESS);
                System.out.println(taskManager.getTaskById(1));
                System.out.println();
                System.out.println("Выводим 1 Эпик, обновляем подзадачу и выводим 1 Эпик с подзадачами");
                System.out.println(epic1);
                taskManager.updateSubtask(5, "Новое название подзадачи 1",
                        "Новое описание подзадачи 1", Status.IN_PROGRESS);
                System.out.println(epic1);
                System.out.println(taskManager.getEpicsSubtasks(epic1));
                System.out.println();
                System.out.println("Изменили статус всех задач во 2 Эпике на DONE");
                taskManager.updateSubtask(7, "Новое название подзадачи 3",
                        "Статус поменяется на IN_PROGRESS", Status.IN_PROGRESS);
                taskManager.updateSubtask(8, "Новое название подзадачи 4",
                        "Статус поменяется на IN_PROGRESS", Status.IN_PROGRESS);
                System.out.println(epic2);
                System.out.println(taskManager.getEpicsSubtasks(epic2));
                taskManager.updateSubtask(7, "Новое название подзадачи 3",
                        "Статус поменяется на DONE", Status.DONE);
                taskManager.updateSubtask(8, "Новое название подзадачи 4",
                        "Статус поменяется на DONE", Status.DONE);
                System.out.println(epic2);
                System.out.println(taskManager.getEpicsSubtasks(epic2));
                System.out.println();
                System.out.println("Вывели удаленные задачу 2 и Эпик 4");
                taskManager.removeTask(2);
                taskManager.removeEpic(4);
                taskManager.removeSubtask(5);
                System.out.println("Задача 2: " + taskManager.getTaskById(2));
                System.out.println("Эпик 2: " + taskManager.getEpicById(4));
                System.out.println("Подзадачи Эпика 2: " + taskManager.getEpicsSubtasks(epic2));
                System.out.println();
                System.out.println("Выводим 1 Эпик (у которого уже удалили одну подзадачу): ");
                System.out.println(epic1);
                System.out.println(taskManager.getEpicsSubtasks(epic1));
                System.out.println();

                System.out.println("Удаляем все его подзадачи и видим изменившееся значения его подзадач");
                taskManager.clearAllSubtasks();
                System.out.println(taskManager.getAllEpics());
                System.out.println(taskManager.getAllSubtasks());
        }

}