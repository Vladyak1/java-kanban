import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Status;
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

        SubTask subTask1 = new SubTask(6, "Подзадача 1", "Описание подзадачи 1", epic1.getId());
        SubTask subTask2 = new SubTask(7, "Подзадача 2", "Описание подзадачи 2", epic1.getId());
        SubTask subTask3 = new SubTask(8, "Подзадача 3", "Описание подзадачи 3", epic2.getId());

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(epic1);
        taskManager.addTask(epic2);
        taskManager.addTask(subTask1);
        taskManager.addTask(subTask2);
        taskManager.addTask(subTask3);
        System.out.println("Сначала вывели все задачи по возрастанию id");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println();
        System.out.println("Далее вывели каждый Эпик с его подзадачей");
        for (Task task :  taskManager.getAllTasks()) {
            if (task instanceof Epic) {
                System.out.println(task);
                for (SubTask allSubTask : taskManager.getAllSubTasks((Epic) task)) {
                    System.out.println(allSubTask);
                }

            }
        }
        System.out.println();
        System.out.println("Вывели обновленную задачу 1");
        taskManager.updateTask(1, "Новое описание задачи 1");
        System.out.println(taskManager.getTaskById(1));
        System.out.println();
        System.out.println("Вывели удаленные задачи 2 и 4");
        taskManager.removeTask(2);
        taskManager.removeTask(4);
        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getTaskById(4));
	}

}