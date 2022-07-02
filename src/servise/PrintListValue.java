package servise;

import tasks.*;

import java.util.List;

public class PrintListValue {
    public static void printTasks(List<Task> tasks){
        System.out.println("Таски: ");
        for (Task task : tasks){
            System.out.println(task);
        }
    }

    public static void printEpic(List<Epic> epics){
        System.out.println("Эпики: ");
        for (Epic epic : epics){
            System.out.println(epic);
        }
    }

    public static void printSubtask(List<Subtask> subtasks){
        System.out.println("Подзадачи: ");
        for (Subtask subtask : subtasks){
            System.out.println(subtask);
        }
    }

    public  static  void  printListSubtaskFromEpic(List<Subtask> subtasks){
        for (Subtask subtask : subtasks){
            System.out.println(subtask);
        }
    }
}
