package servise;

import servise.interfase.HistoryManager;
import servise.interfase.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) throws ManagerSaveException {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("Tasks.csv"));
        Task task1 = new Task("Ужаснуться", "Поседеть от ТЗ третьего спринта");
        Task task2 = new Task("Расшифровка", "Расшифровать задуманное задание");

        fileBackedTasksManager.addNewTask(task1);
        fileBackedTasksManager.addNewTask(task2);

        Epic epic1 = new Epic("Сделать Финальный проект", "Переписать прогу в 4ый раз");

        fileBackedTasksManager.addNewEpic(epic1);

        Subtask subtask3_1 = new Subtask("Удалить", "Удалить уже готовый консольный шедевр", epic1.getIdentifier());
        Subtask subtask3_2 = new Subtask("Осознать суть", "Понять что она должна передавать объект", epic1.getIdentifier());
        Subtask subtask3_3 = new Subtask("Превозмочь", "Проломить все стены своей головой", epic1.getIdentifier());
        Subtask subtask3_4 = new Subtask("Закончить", "Написать очередной шедевр", epic1.getIdentifier());
        Subtask subtask3_5 = new Subtask("Ревью", "Сдать на ревью", epic1.getIdentifier());

        fileBackedTasksManager.addNewSubtask(subtask3_1);
        fileBackedTasksManager.addNewSubtask(subtask3_2);
        fileBackedTasksManager.addNewSubtask(subtask3_3);
        fileBackedTasksManager.addNewSubtask(subtask3_4);
        fileBackedTasksManager.addNewSubtask(subtask3_5);

        Epic epic2 = new Epic("Благодарность", "Поблагодарить Сергея за прекрасное ревью");

        fileBackedTasksManager.addNewEpic(epic2);

        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getTask(2);
        fileBackedTasksManager.getEpic(3);
        fileBackedTasksManager.getEpic(9);
        fileBackedTasksManager.getSubtask(4);

        fileBackedTasksManager.save();
    }

    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        try {
            String fileContents = Files.readString(Path.of(file.getName()), StandardCharsets.UTF_8);
            String[] line = fileContents.split("\n");
            if (!line[0].equals("")) {
                manager.readTasks(line, manager);
                boolean isEmptiness = line[line.length - 1].equals("Пусто");
                manager.readHistory(isEmptiness, line, manager);
            }
            return manager;
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> idTasks = new ArrayList<>();
        String[] idTasksHistory = value.split(",");
        for (String id : idTasksHistory) {
            idTasks.add(Integer.parseInt(id));
        }
        return idTasks;
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder idTasks = new StringBuilder("\n");
        for (Task task : manager.getHistory()) {
            idTasks.append(task.getIdentifier()).append(",");
        }
        return idTasks.toString();
    }

    public Task fromString(String value) {
        String[] dataTask = value.split(",");
        if (dataTask[1].equals(Tasks.TASK.getTaskType())) {
            return new Task(dataTask[2], Status.valueOf(dataTask[3]), Integer.parseInt(dataTask[0]), dataTask[4]);
        } else if (dataTask[1].equals(Tasks.EPIC.getTaskType())) {
            return new Epic(dataTask[2], Status.valueOf(dataTask[3]), Integer.parseInt(dataTask[0]), dataTask[4]);
        } else {
            return new Subtask(dataTask[2], Status.valueOf(dataTask[3]), Integer.parseInt(dataTask[0]), dataTask[4], Integer.parseInt(dataTask[5]));
        }

    }

    private void readTasks(String[] line, FileBackedTasksManager manager) {
        for (int i = 1; i < (line.length - 2); i++) {
            if (manager.fromString(line[i]) instanceof Epic) {
                manager.addNewEpic((Epic) manager.fromString(line[i]));
            } else if (manager.fromString(line[i]) instanceof Subtask) {
                manager.addNewSubtask((Subtask) manager.fromString(line[i]));
            } else {
                manager.addNewTask(manager.fromString(line[i]));
            }
        }
    }

    private void readHistory(boolean isEmptiness, String[] line, FileBackedTasksManager manager) {
        if (!isEmptiness) {
            for (Integer id : historyFromString(line[line.length - 1])) {
                manager.getTask(id);
                manager.getEpic(id);
                manager.getSubtask(id);
            }
        }
    }

    private String[] createArrayRecord(Task task) {
        String[] taskData = new String[6];
        taskData[0] = Integer.toString(task.getIdentifier());
        taskData[1] = Tasks.TASK.getTaskType();
        taskData[2] = task.getName();
        taskData[3] = task.getStatus().getStatus();
        taskData[4] = task.getDescription();
        taskData[5] = "\n";
        if (task instanceof Epic) {
            taskData[1] = Tasks.EPIC.getTaskType();
        } else if (task instanceof Subtask) {
            taskData[1] = Tasks.SUBTASK.getTaskType();
            taskData[5] = ((Subtask) task).getIdentifierEpic() + "\n";
        }
        return taskData;
    }

    private String toStringTasks(String[] record) {
        return String.join(",", record);
    }

    public void save() throws ManagerSaveException {
        try (Writer writer = new FileWriter("Tasks.csv", StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTasks()) {
                writer.write(toStringTasks(createArrayRecord(task)));
            }
            if (history.getHistory().isEmpty()) {
                writer.write("\nПусто");
            } else {
                writer.write(historyToString(history));
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }

    }

    private List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.addAll(getListTasks());
        tasks.addAll(getListEpic());
        tasks.addAll(getListSubtasks());
        IdComparator comparator = new IdComparator();
        tasks.sort(comparator);
        return tasks;
    }

    private static class IdComparator implements Comparator<Task> {

        @Override
        public int compare(Task o1, Task o2) {
            return Integer.compare(o1.getIdentifier(), o2.getIdentifier());
        }
    }
}
