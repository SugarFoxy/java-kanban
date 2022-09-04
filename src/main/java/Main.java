import servise.*;
import tasks.*;


public class Main {


    public static void main(String[] args) throws ManagerSaveException {

        InMemoryTaskManager fileBackedTasksManager =(InMemoryTaskManager) Managers.getDefault();

        //Отправляем в хранилище
        Task task1 = new Task("Ужаснуться","Поседеть от ТЗ третьего спринта", 25, "01:00 01.01.2022");
        Task task2 = new Task("Расшифровка","Расшифровать задуманное задание", 25, "01:00 01.01.2023");

        fileBackedTasksManager.addNewTask(task1);
        fileBackedTasksManager.addNewTask(task2);

        Epic epic1 = new Epic("Сделать Финальный проект","Переписать прогу в 4ый раз");

        fileBackedTasksManager.addNewEpic(epic1);

        Subtask subtask3_1 = new Subtask("Удалить", "Удалить уже готовый консольный шедевр",epic1.getIdentifier(), 25, "03:00 01.01.2022");
        Subtask subtask3_2 = new Subtask("Осознать суть","Понять что она должна передавать объект", epic1.getIdentifier(), 25, "04:00 01.01.2022");
        Subtask subtask3_3 = new Subtask("Превозмочь","Проломить все стены своей головой", epic1.getIdentifier(), 25, "05:00 01.01.2026");
        Subtask subtask3_4 = new Subtask("Закончить","Написать очередной шедевр", epic1.getIdentifier(), 25, "06:00 01.01.2025");
        Subtask subtask3_5 = new Subtask("Ревью","Сдать на ревью", epic1.getIdentifier(), 25, "07:00 01.01.2021");

        fileBackedTasksManager.addNewSubtask( subtask3_1);
        fileBackedTasksManager.addNewSubtask( subtask3_2);
        fileBackedTasksManager.addNewSubtask( subtask3_3);
        fileBackedTasksManager.addNewSubtask( subtask3_4);
        fileBackedTasksManager.addNewSubtask( subtask3_5);

        Epic epic2 = new Epic("Благодарность","Поблагодарить Сергея за прекрасное ревью");

        fileBackedTasksManager.addNewEpic(epic2);

        PrintListValue.printSortTask(fileBackedTasksManager.getPrioritizedTasks());
        System.out.println("-------------------------------------");

     fileBackedTasksManager.getTask(task1.getIdentifier());
     fileBackedTasksManager.getTask(task2.getIdentifier());
     fileBackedTasksManager.getEpic(epic1.getIdentifier());
     fileBackedTasksManager.getEpic(epic2.getIdentifier());
     fileBackedTasksManager.getSubtask(subtask3_1.getIdentifier());
     fileBackedTasksManager.getSubtask(subtask3_2.getIdentifier());
     fileBackedTasksManager.getSubtask(subtask3_3.getIdentifier());

        PrintListValue.printHistory(fileBackedTasksManager.getHistory());
        System.out.println("-----------------------");
     fileBackedTasksManager.getTask(task1.getIdentifier());
     fileBackedTasksManager.getTask(task2.getIdentifier());
     fileBackedTasksManager.getEpic(epic1.getIdentifier());
     fileBackedTasksManager.getEpic(epic2.getIdentifier());

        PrintListValue.printHistory(fileBackedTasksManager.getHistory());
        System.out.println("-----------------------");
        Subtask subtask9_1 = new Subtask(":)", "Спасибо, Сергей :)", epic2.getIdentifier(), 25, "09:00 01.01.2022");

     fileBackedTasksManager.addNewSubtask( subtask9_1);

        //Получаем все задачи
        System.out.println();
        PrintListValue.printTasks(fileBackedTasksManager.getListTasks());
        PrintListValue.printEpic(fileBackedTasksManager.getListEpic());
        PrintListValue.printSubtask(fileBackedTasksManager.getListSubtasks());


        //Получаем задачу по индексу
        System.out.println();
        System.out.println(fileBackedTasksManager.getTask(1));
        System.out.println(fileBackedTasksManager.getEpic(3));
        System.out.println(fileBackedTasksManager.getSubtask(4));

        System.out.println();
        PrintListValue.printRequestTask(fileBackedTasksManager.getTask(1));
        PrintListValue.printRequestEpic(fileBackedTasksManager.getEpic(3));
        PrintListValue.printRequestSubtask(fileBackedTasksManager.getSubtask(4));
        PrintListValue.printRequestTask(fileBackedTasksManager.getTask(1));
        PrintListValue.printRequestEpic(fileBackedTasksManager.getEpic(3));
        PrintListValue.printRequestSubtask(fileBackedTasksManager.getSubtask(4));
        PrintListValue.printRequestTask(fileBackedTasksManager.getTask(1));
        PrintListValue.printRequestEpic(fileBackedTasksManager.getEpic(3));
        PrintListValue.printRequestSubtask(fileBackedTasksManager.getSubtask(4));
        PrintListValue.printRequestTask(fileBackedTasksManager.getTask(1));
        PrintListValue.printRequestEpic(fileBackedTasksManager.getEpic(3));
        PrintListValue.printRequestSubtask(fileBackedTasksManager.getSubtask(4));

        System.out.println();
        PrintListValue.printHistory(Managers.getDefaultHistory().getHistory());



        //Получение списка всех подзадач определённого эпика.
        System.out.println();
        PrintListValue.printListSubtaskFromEpic(fileBackedTasksManager.getListSubtaskFromEpic(1));


        //Обновление данных
        System.out.println();



        Task task3 = new Task("Утолить любопытство", Status.NEW, task1.getIdentifier(), "Сергей, как часто из ваших глаз идет кровь, при проверке моих работ?", 25, "01:00 01.01.2022");
        fileBackedTasksManager.updateTask( new Task("Утолить любопытство", Status.IN_PROCESS, task3.getIdentifier(), "Сергей, как часто из ваших глаз идет кровь, при проверке моих работ?",25, "02:00 01.01.2022" ));
        fileBackedTasksManager.updateTask(new Task("Утолить любопытство", Status.DONE, task3.getIdentifier(), "Сергей, как часто из ваших глаз идет кровь, при проверке моих работ?",25, "02:00 01.01.2022" ));
        PrintListValue.printTasks(fileBackedTasksManager.getListTasks());

        Epic epic3 = new Epic("Перезагрузить мозг", epic1.getIdentifier(), "Кончилась фантазия" );
        fileBackedTasksManager.updateEpic(epic3);


        Subtask newSubtask3_1 = new Subtask("Уверенность", Status.NEW, subtask3_1.getIdentifier(), "На это раз я уверена, что тз выполнено верно", epic1.getIdentifier(),25, "01:00 01.01.2022");
        Subtask newSubtask3_2 = new Subtask("Усомниться", Status.NEW, subtask3_2.getIdentifier(), "Ну почти",epic1.getIdentifier(),25, "01:00 01.01.2022");
        Subtask newSubtask3_3 = new Subtask("Заслужить похвалу", Status.NEW, subtask3_3.getIdentifier(), "Я очень люблю когда меня хвалят:))))",epic1.getIdentifier(),25, "01:00 01.01.2022");
        fileBackedTasksManager.updateSubtask(newSubtask3_1);
        fileBackedTasksManager.updateSubtask(newSubtask3_2);
        fileBackedTasksManager.updateSubtask(newSubtask3_3);
        fileBackedTasksManager.updateSubtask(new Subtask("Усомниться", Status.DONE, subtask3_2.getIdentifier(), "Ну почти",3,25, "01:00 01.01.2022"));




        fileBackedTasksManager.updateSubtask(new Subtask(":)", Status.DONE, subtask9_1.getIdentifier(),"Спасибо, Сергей :)", epic2.getIdentifier(),25, "01:00 01.01.2022"));
        PrintListValue.printSubtask(fileBackedTasksManager.getListSubtasks());
        fileBackedTasksManager.updateSubtask(new Subtask(":)", Status.NEW, subtask9_1.getIdentifier(),"Спасибо, Сергей :)", epic2.getIdentifier(),25, "01:00 01.01.2022"));
        PrintListValue.printSubtask(fileBackedTasksManager.getListSubtasks());
        PrintListValue.printTasks(fileBackedTasksManager.getListTasks());
        PrintListValue.printEpic(fileBackedTasksManager.getListEpic());
        PrintListValue.printSubtask(fileBackedTasksManager.getListSubtasks());

        Managers.getDefaultHistory().getHistory();
        //Удаление по индексу
        System.out.println();

        fileBackedTasksManager.deleteTaskByIdentifier(task1.getIdentifier());
        fileBackedTasksManager.deleteSubtaskByIdentifier(subtask3_5.getIdentifier());
        fileBackedTasksManager.deleteSubtaskByIdentifier(subtask3_4.getIdentifier());
        PrintListValue.printHistory(fileBackedTasksManager.getHistory());
        System.out.println("-----------------------");
        fileBackedTasksManager.deleteEpicByIdentifier(epic1.getIdentifier());
        PrintListValue.printHistory(fileBackedTasksManager.getHistory());
        System.out.println("-----------------------");

        PrintListValue.printTasks(fileBackedTasksManager.getListTasks());
        PrintListValue.printEpic(fileBackedTasksManager.getListEpic());
        PrintListValue.printSubtask(fileBackedTasksManager.getListSubtasks());


        //Удаление всех задач
        fileBackedTasksManager.deleteAllSubtasks();
        PrintListValue.printTasks(fileBackedTasksManager.getListTasks());
        PrintListValue.printEpic(fileBackedTasksManager.getListEpic());
        PrintListValue.printSubtask(fileBackedTasksManager.getListSubtasks());
//        int a = 1;
//        for (int i = 0; i < 10000000; i++){
//            fileBackedTasksManager.addNewTask(new Task("O", "P"));
//            fileBackedTasksManager.getTask(a++);
//        }


////        PrintListValue.printHistory(Managers.getDefault().getHistory());
//        final long startTime = System.nanoTime();
//        Managers.getDefaultHistory().getHistory();
//        final long endTime = System.nanoTime();
//        System.out.println("-----------------------");
////        PrintListValue.printHistory(Managers.getDefault().getHistory());
//        System.out.println(endTime-startTime);
  }
}
