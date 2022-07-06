import servise.*;
import tasks.*;

public class Main {


    public static void main(String[] args) {

        //Отправляем в хранилище
        Manager manager = new Manager();
        Task task1 = new Task("Ужаснуться","Поседеть от ТЗ 3его спринта");
        Task task2 = new Task("Расшифровка","Расшифровать задуманное задание");

        manager.addNewTask(task1);
        manager.addNewTask(task2);

        Epic epic1 = new Epic("Сделать Финальный проект","Переписать прогу в 4ый раз");

        manager.addNewEpic(epic1);

        Subtask subtask3_1 = new Subtask("Удалить", "Удалить уже готовый консольный шедевр",epic1.getIdentifier());
        Subtask subtask3_2 = new Subtask("Осознать суть","Понять что она должна передавать объект", epic1.getIdentifier());
        Subtask subtask3_3 = new Subtask("анить трудности","Проломить все стены своей головой", epic1.getIdentifier());
        Subtask subtask3_4 = new Subtask("Закончить","Написать очередной шедевр", epic1.getIdentifier());
        Subtask subtask3_5 = new Subtask("Ревью","Сдать на ревью", epic1.getIdentifier());

        manager.addNewSubtask( subtask3_1);
        manager.addNewSubtask( subtask3_2);
        manager.addNewSubtask( subtask3_3);
        manager.addNewSubtask( subtask3_4);
        manager.addNewSubtask( subtask3_5);

        Epic epic2 = new Epic("Благодарность","Поблагодарить Сергея за прекрасное ревью");

        manager.addNewEpic(epic2);

        Subtask subtask9_1 = new Subtask(":)", "Спасибо, Сергей :)", epic2.getIdentifier());

        manager.addNewSubtask( subtask9_1);

        //Получаем все задачи
        System.out.println();
        PrintListValue.printTasks(manager.getListTasks());
        PrintListValue.printEpic(manager.getListEpic());
        PrintListValue.printSubtask(manager.getListSubtasks());


        //Получаем задачу по индексу
        System.out.println();
        System.out.println(manager.getTaskByIdentifier(1));
        System.out.println(manager.getEpicByIdentifier(3));
        System.out.println(manager.getSubtaskByIdentifier(4));

        //Получение списка всех подзадач определённого эпика.
        System.out.println();
        PrintListValue.printListSubtaskFromEpic(manager.getListSubtaskFromEpic(epic1.getIdentifier()));

        //Обновление данных
        System.out.println();

        Task task3 = new Task("Утолить любопытство", Epic.Status.NEW, task1.getIdentifier(), "Сергей, как часто из ваших глаз идет кровь, при проверке моих работ?" );
        manager.updateTask( new Task("Утолить любопытство", Epic.Status.IN_PROCESS, task3.getIdentifier(), "Сергей, как часто из ваших глаз идет кровь, при проверке моих работ?" ));
        manager.updateTask(new Task("Утолить любопытство", Epic.Status.DONE, task3.getIdentifier(), "Сергей, как часто из ваших глаз идет кровь, при проверке моих работ?" ));
        PrintListValue.printTasks(manager.getListTasks());

        Epic epic3 = new Epic("Перезагрузить мозг", epic1.getIdentifier(), "Кончилась фантазия" );
        manager.updateEpic(epic3);


        Subtask newSubtask3_1 = new Subtask("Уверенность", Epic.Status.NEW, subtask3_1.getIdentifier(), "На это раз я уверена, что тз выполнено верно", epic1.getIdentifier());
        Subtask newSubtask3_2 = new Subtask("Усомниться", Epic.Status.NEW, subtask3_2.getIdentifier(), "Ну почти",epic1.getIdentifier());
        Subtask newSubtask3_3 = new Subtask("Заслужить похвалу", Epic.Status.NEW, subtask3_3.getIdentifier(), "Я очень люблю когда меня хвалят:))))",epic1.getIdentifier());
        manager.updateSubtask(newSubtask3_1);
        manager.updateSubtask(newSubtask3_2);
        manager.updateSubtask(newSubtask3_3);
        manager.updateSubtask(new Subtask("Усомниться", Epic.Status.DONE, subtask3_2.getIdentifier(), "Ну почти",epic1.getIdentifier()));

        Subtask newSubtask9_1 = new Subtask(":)", Epic.Status.NEW, subtask9_1.getIdentifier(),"Спасибо, Сергей :)", epic2.getIdentifier());
        manager.updateSubtask(newSubtask9_1);
        manager.updateSubtask(new Subtask(":)", Epic.Status.DONE, subtask9_1.getIdentifier(),"Спасибо, Сергей :)", epic2.getIdentifier()));

        PrintListValue.printTasks(manager.getListTasks());
        PrintListValue.printEpic(manager.getListEpic());
        PrintListValue.printSubtask(manager.getListSubtasks());

        //Удаление по индексу
        System.out.println();

        manager.deleteTaskByIdentifier(task1.getIdentifier());
        manager.deleteEpicByIdentifier(epic1.getIdentifier());
        manager.deleteSubtaskByIdentifier(subtask3_5.getIdentifier());
        manager.deleteSubtaskByIdentifier(subtask3_4.getIdentifier());

        PrintListValue.printTasks(manager.getListTasks());
        PrintListValue.printEpic(manager.getListEpic());
        PrintListValue.printSubtask(manager.getListSubtasks());


        //Удаление всех задач
        manager.deleteAllTasks();
        PrintListValue.printTasks(manager.getListTasks());
        PrintListValue.printEpic(manager.getListEpic());
        PrintListValue.printSubtask(manager.getListSubtasks());
    }
}
