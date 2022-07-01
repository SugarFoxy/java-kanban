public class Main {


    public static void main(String[] args) {

            System.out.println(Task.Status.NEW);
        //Добавляем задачи

        Task task1 = new Task("Поседеть от ТЗ 3его спринта", Task.Status.NEW);
        Task task2 = new Task("Расшифровать задуманное задание", Task.Status.NEW);
        Epic epic1 = new Epic("Переписать прогу в 4ый раз", Task.Status.NEW);
        Subtask subtask3_1 = new Subtask("Удалить уже готовый консольный шедевр", Task.Status.NEW, epic1.getIdentifier());
        Subtask subtask3_2 = new Subtask("Понять что она должна передавать объект", Task.Status.NEW, epic1.getIdentifier());
        Subtask subtask3_3 = new Subtask("Проломить все стены своей головой", Task.Status.NEW, epic1.getIdentifier());
        Subtask subtask3_4 = new Subtask("Написать очередной шедевр", Task.Status.NEW, epic1.getIdentifier());
        Subtask subtask3_5 = new Subtask("Сдать на ревью", Task.Status.NEW, epic1.getIdentifier());
        Epic epic2 = new Epic("Поблагодарить Сергея за прекрасное ревью", Task.Status.NEW);
        Subtask subtask2_1 = new Subtask("Спасибо, Сергей :)", Task.Status.NEW, epic2.getIdentifier());

        //Отправляем в хранилище
        Manager manager = new Manager();

        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewEpic(epic1);
        manager.addNewSubtask(epic1, subtask3_1);
        manager.addNewSubtask(epic1, subtask3_2);
        manager.addNewSubtask(epic1, subtask3_3);
        manager.addNewSubtask(epic1, subtask3_4);
        manager.addNewSubtask(epic1, subtask3_5);
        manager.addNewEpic(epic2);
        manager.addNewSubtask(epic2, subtask2_1);

        //Получаем все задачи
        System.out.println();
        System.out.println(manager.getAllTasks().toString());
        System.out.println(manager.getAllEpics().toString());
        System.out.println(manager.getAllSubtasks().toString());

        //Получаем задачу по индексу
        System.out.println();
        System.out.println(manager.getTaskByIdentifierOrNull(1));
        System.out.println(manager.getEpicByIdentifierOrNull(3));
        System.out.println(manager.getSubtaskByIdentifierOrNull(4));

        //Получение списка всех подзадач определённого эпика.
        System.out.println();
        System.out.println(manager.getListSubtaskFromEpic(epic1).toString());

        //Обновление данных
        System.out.println();
        Task task3 = new Task("Перегрузить конструктор", Task.Status.IN_PROCESS, 1);
        Task task31 = new Task("Перегрузить конструктор", Task.Status.DONE, 1);
        Epic epic3 = new Epic("Реализовать метод обновления", Task.Status.NEW, 3, epic1.getSubtasksHashMap());
        Subtask newSubtask3_1 = new Subtask("Создать объекты", Task.Status.IN_PROCESS, epic3.getIdentifier(), 4);
        Subtask newSubtask3_2 = new Subtask("Прописать метод", Task.Status.NEW, epic3.getIdentifier(), 5);
        Subtask newSubtask3_3 = new Subtask("Вызвать метод", Task.Status.NEW, epic3.getIdentifier(), 6);

        manager.updateTask(task3);
        manager.updateTask(task31);
        System.out.println(manager.getAllTasks().toString());

        manager.updateEpic(epic3);
        System.out.println(manager.getAllEpics().toString());

        manager.updateSubtask(newSubtask3_1);
        manager.updateSubtask(newSubtask3_2);
        manager.updateSubtask(newSubtask3_3);
        System.out.println(manager.getAllSubtasks().toString());

        //Удаление по индексу
        System.out.println();

        manager.deleteTaskByIdentifier(1);
        manager.deleteEpicByIdentifier(3);
        manager.deleteSubtaskByIdentifier(7);
        manager.deleteSubtaskByIdentifier(8);

        System.out.println(manager.getAllTasks().toString());
        System.out.println(manager.getAllEpics().toString());
        System.out.println(manager.getAllSubtasks().toString());

        //Удаление всех задач
        manager.deleteAllTasks();
    }
}
