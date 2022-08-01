import servise.*;
import tasks.*;

public class Main {


    public static void main(String[] args) {

        //Отправляем в хранилище
        Task task1 = new Task("Ужаснуться","Поседеть от ТЗ 3его спринта");
        Task task2 = new Task("Расшифровка","Расшифровать задуманное задание");

        Managers.getDefault().addNewTask(task1);
        Managers.getDefault().addNewTask(task2);

        Epic epic1 = new Epic("Сделать Финальный проект","Переписать прогу в 4ый раз");

        Managers.getDefault().addNewEpic(epic1);

        Subtask subtask3_1 = new Subtask("Удалить", "Удалить уже готовый консольный шедевр",epic1.getIdentifier());
        Subtask subtask3_2 = new Subtask("Осознать суть","Понять что она должна передавать объект", epic1.getIdentifier());
        Subtask subtask3_3 = new Subtask("Превозмочь","Проломить все стены своей головой", epic1.getIdentifier());
        Subtask subtask3_4 = new Subtask("Закончить","Написать очередной шедевр", epic1.getIdentifier());
        Subtask subtask3_5 = new Subtask("Ревью","Сдать на ревью", epic1.getIdentifier());

        Managers.getDefault().addNewSubtask( subtask3_1);
        Managers.getDefault().addNewSubtask( subtask3_2);
        Managers.getDefault().addNewSubtask( subtask3_3);
        Managers.getDefault().addNewSubtask( subtask3_4);
        Managers.getDefault().addNewSubtask( subtask3_5);

        Epic epic2 = new Epic("Благодарность","Поблагодарить Сергея за прекрасное ревью");

        Managers.getDefault().addNewEpic(epic2);
        Managers.getDefault().getTask(task1.getIdentifier());
        Managers.getDefault().getTask(task2.getIdentifier());
        Managers.getDefault().getEpic(epic1.getIdentifier());
        Managers.getDefault().getEpic(epic2.getIdentifier());
        Managers.getDefault().getSubtask(subtask3_1.getIdentifier());
        Managers.getDefault().getSubtask(subtask3_2.getIdentifier());
        Managers.getDefault().getSubtask(subtask3_3.getIdentifier());

        PrintListValue.printHistory(Managers.getDefault().getHistory());
        System.out.println("-----------------------");
        Managers.getDefault().getTask(task1.getIdentifier());
        Managers.getDefault().getTask(task2.getIdentifier());
        Managers.getDefault().getEpic(epic1.getIdentifier());
        Managers.getDefault().getEpic(epic2.getIdentifier());

        PrintListValue.printHistory(Managers.getDefault().getHistory());
        System.out.println("-----------------------");
//        Subtask subtask9_1 = new Subtask(":)", "Спасибо, Сергей :)", epic2.getIdentifier());
//
//        Managers.getDefault().addNewSubtask( subtask9_1);
//
//        //Получаем все задачи
//        System.out.println();
//        PrintListValue.printTasks(Managers.getDefault().getListTasks());
//        PrintListValue.printEpic(Managers.getDefault().getListEpic());
//        PrintListValue.printSubtask(Managers.getDefault().getListSubtasks());
//
//
//        //Получаем задачу по индексу
//        System.out.println();
//        System.out.println(Managers.getDefault().getTask(1));
//        System.out.println(Managers.getDefault().getEpic(3));
//        System.out.println(Managers.getDefault().getSubtask(4));
//
//        System.out.println();
//        PrintListValue.printRequestTask(Managers.getDefault().getTask(1));
//        PrintListValue.printRequestEpic(Managers.getDefault().getEpic(3));
//        PrintListValue.printRequestSubtask(Managers.getDefault().getSubtask(4));
//        PrintListValue.printRequestTask(Managers.getDefault().getTask(1));
//        PrintListValue.printRequestEpic(Managers.getDefault().getEpic(3));
//        PrintListValue.printRequestSubtask(Managers.getDefault().getSubtask(4));
//        PrintListValue.printRequestTask(Managers.getDefault().getTask(1));
//        PrintListValue.printRequestEpic(Managers.getDefault().getEpic(3));
//        PrintListValue.printRequestSubtask(Managers.getDefault().getSubtask(4));
//        PrintListValue.printRequestTask(Managers.getDefault().getTask(1));
//        PrintListValue.printRequestEpic(Managers.getDefault().getEpic(3));
//        PrintListValue.printRequestSubtask(Managers.getDefault().getSubtask(4));
//
//        System.out.println();
//        PrintListValue.printHistory(Managers.getDefaultHistory().getHistory());
//
//
//
//        //Получение списка всех подзадач определённого эпика.
//        System.out.println();
//        PrintListValue.printListSubtaskFromEpic(Managers.getDefault().getListSubtaskFromEpic(1));
//
//        //Обновление данных
//        System.out.println();
//
//        Task task3 = new Task("Утолить любопытство", Status.NEW, task1.getIdentifier(), "Сергей, как часто из ваших глаз идет кровь, при проверке моих работ?" );
//        Managers.getDefault().updateTask( new Task("Утолить любопытство", Status.IN_PROCESS, task3.getIdentifier(), "Сергей, как часто из ваших глаз идет кровь, при проверке моих работ?" ));
//        Managers.getDefault().updateTask(new Task("Утолить любопытство", Status.DONE, task3.getIdentifier(), "Сергей, как часто из ваших глаз идет кровь, при проверке моих работ?" ));
//        PrintListValue.printTasks(Managers.getDefault().getListTasks());
//
//        Epic epic3 = new Epic("Перезагрузить мозг", epic1.getIdentifier(), "Кончилась фантазия" );
//        Managers.getDefault().updateEpic(epic3);
//
//
//        Subtask newSubtask3_1 = new Subtask("Уверенность", Status.NEW, subtask3_1.getIdentifier(), "На это раз я уверена, что тз выполнено верно", epic1.getIdentifier());
//        Subtask newSubtask3_2 = new Subtask("Усомниться", Status.NEW, subtask3_2.getIdentifier(), "Ну почти",epic1.getIdentifier());
//        Subtask newSubtask3_3 = new Subtask("Заслужить похвалу", Status.NEW, subtask3_3.getIdentifier(), "Я очень люблю когда меня хвалят:))))",epic1.getIdentifier());
//        Managers.getDefault().updateSubtask(newSubtask3_1);
//        Managers.getDefault().updateSubtask(newSubtask3_2);
//        Managers.getDefault().updateSubtask(newSubtask3_3);
//        Managers.getDefault().updateSubtask(new Subtask("Усомниться", Status.DONE, subtask3_2.getIdentifier(), "Ну почти",3));
//
//
//
//
//        Managers.getDefault().updateSubtask(new Subtask(":)", Status.DONE, subtask9_1.getIdentifier(),"Спасибо, Сергей :)", epic2.getIdentifier()));
//        PrintListValue.printSubtask(Managers.getDefault().getListSubtasks());
//        Managers.getDefault().updateSubtask(new Subtask(":)", Status.NEW, subtask9_1.getIdentifier(),"Спасибо, Сергей :)", epic2.getIdentifier()));
//        PrintListValue.printSubtask(Managers.getDefault().getListSubtasks());
//        PrintListValue.printTasks(Managers.getDefault().getListTasks());
//        PrintListValue.printEpic(Managers.getDefault().getListEpic());
//        PrintListValue.printSubtask(Managers.getDefault().getListSubtasks());
//
//        Managers.getDefaultHistory().getHistory();
//        //Удаление по индексу
//        System.out.println();
//
        Managers.getDefault().deleteTaskByIdentifier(task1.getIdentifier());
        Managers.getDefault().deleteSubtaskByIdentifier(subtask3_5.getIdentifier());
        Managers.getDefault().deleteSubtaskByIdentifier(subtask3_4.getIdentifier());
        PrintListValue.printHistory(Managers.getDefault().getHistory());
        System.out.println("-----------------------");
        Managers.getDefault().deleteEpicByIdentifier(epic1.getIdentifier());
        PrintListValue.printHistory(Managers.getDefault().getHistory());
        System.out.println("-----------------------");
//
//        PrintListValue.printTasks(Managers.getDefault().getListTasks());
//        PrintListValue.printEpic(Managers.getDefault().getListEpic());
//        PrintListValue.printSubtask(Managers.getDefault().getListSubtasks());
//
//
//        //Удаление всех задач
//        Managers.getDefault().deleteAllSubtasks();
//        PrintListValue.printTasks(Managers.getDefault().getListTasks());
//        PrintListValue.printEpic(Managers.getDefault().getListEpic());
//        PrintListValue.printSubtask(Managers.getDefault().getListSubtasks());
//        int a = 1;
//        for (int i = 0; i < 10000000; i++){
//            Managers.getDefault().addNewTask(new Task("O", "P"));
//            Managers.getDefault().getTask(a++);
//        }
//
//
////        PrintListValue.printHistory(Managers.getDefault().getHistory());
//        final long startTime = System.nanoTime();
//        Managers.getDefaultHistory().getHistory();
//        final long endTime = System.nanoTime();
//        System.out.println("-----------------------");
////        PrintListValue.printHistory(Managers.getDefault().getHistory());
//        System.out.println(endTime-startTime);
  }
}
