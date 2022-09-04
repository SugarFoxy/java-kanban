package servise;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public class PrintListValue {
    public static void printTasks(List<Task> tasks) {
        System.out.println("Таски: ");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    public static void printEpic(List<Epic> epics) {
        System.out.println("Эпики: ");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
    }

    public static void printSubtask(List<Subtask> subtasks) {
        System.out.println("Подзадачи: ");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
    }

    public static void printListSubtaskFromEpic(List<Subtask> subtasks) {
        if (subtasks != null) {
            for (Subtask subtask : subtasks) {
                System.out.println(subtask);
            }
        }
    }

        public static void printSortTask(List<Task> tasks) {
            if (tasks != null) {
                for (Task task : tasks) {
                    System.out.println(task);
                }
            }
    }

    public static void printRequestTask(Task task) {
        if (task != null) {
            System.out.println(task);
        }
    }

    public static void printRequestEpic(Epic epic) {
        if (epic != null) {
            System.out.println(epic);
        }
    }

    public static void printRequestSubtask(Subtask subtask) {
        if (subtask != null) {
            System.out.println(subtask);
        }
    }

    public static void printHistory(List<Task> historyTask) {
        if (historyTask != null) {
            for (Task task : historyTask) {
                if (task instanceof Epic) {
                    Epic epic = (Epic) task;
                    System.out.println(epic);
                } else if (task instanceof Subtask) {
                    Subtask subtask = (Subtask) task;
                    System.out.println(subtask);
                } else {
                    System.out.println(task);
                }
            }
        }
    }


}
