import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.SubTask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.service.TaskManager;

class TaskTracker {

	public static void main(String[] args) {
		TaskManager taskManager = new TaskManager();
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1");
        Task task2 = new Task(2, "Задача 2", "Описание задачи 2");

        Epic epic1 = new Epic(4, "Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic(5, "Эпик 2", "Описание эпика 2");

        SubTask subTask1 = new SubTask(6, "Подзадача 1", "Описание подзадачи 1");
        SubTask subTask2 = new SubTask(7, "Подзадача 2", "Описание подзадачи 2");
        SubTask subTask3 = new SubTask(8, "Подзадача 3", "Описание подзадачи 3");
        SubTask subTask4 = new SubTask(9, "Подзадача 4", "Описание подзадачи 4");


        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubTask(subTask1, epic1);
        taskManager.addSubTask(subTask2, epic1);
        taskManager.addSubTask(subTask3, epic2);
        taskManager.addSubTask(subTask4, epic2);
        System.out.println("Сначала вывели все задачи по возрастанию id");
        System.out.println(taskManager.getAllTasks());
        System.out.println();
        System.out.println("Далее вывели каждый Эпик");
        System.out.println(taskManager.getAllEpics());
        System.out.println();
        System.out.println("Далее вывели каждую подзадачу");
        System.out.println(taskManager.getAllSubTasks());
        System.out.println();
        System.out.println("Вывели обновленную задачу 1");
        taskManager.updateTask(1, "Новое описание задачи 1");
        System.out.println(taskManager.getTaskById(1));
        System.out.println();
        System.out.println("Выводим 1 Эпик, обновляем подзадачу и выводим 1 Эпик с подзадачами");
        System.out.println(epic1);
        taskManager.updateSubTask(6, "Новое описание подзадачи 1");
        System.out.println(epic1);
        System.out.println(taskManager.getSubTaskById(6));
        System.out.println(taskManager.getSubTaskById(7));
        System.out.println();
        System.out.println("Изменили статус всех задач во 2 Эпике на DONE");
        taskManager.updateSubTask(8, "Статус поменяется на IN_PROGRESS");
        taskManager.updateSubTask(9, "Статус поменяется на IN_PROGRESS");
        System.out.println(epic2);
        System.out.println(taskManager.getSubTaskById(8));
        System.out.println(taskManager.getSubTaskById(9));
        taskManager.updateSubTask(8, "Статус поменяется на DONE");
        taskManager.updateSubTask(9, "Статус поменяется на DONE");
        System.out.println(epic2);
        System.out.println(taskManager.getSubTaskById(8));
        System.out.println(taskManager.getSubTaskById(9));
        System.out.println();
        System.out.println("Вывели удаленные задачу 2 и Эпик 4");
        taskManager.removeTask(2);
        taskManager.removeEpic(4);
        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getEpicById(4));
	}

}