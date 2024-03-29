package servise;

import servise.interfase.HistoryManager;
import servise.interfase.TaskManager;
import tasks.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private File file;
    private URI uri;
    public FileBackedTasksManager(File file) {
        this.file = file;
    }
    protected FileBackedTasksManager() {

    }

    public static void main(String[] args) {
        FileBackedTasksManager taskManager = FileBackedTasksManager.loadFromFile(new File("resources/Tasks.csv"));
        Task task1 = new Task("Ужаснуться", "Поседеть от ТЗ третьего спринта", 25, "01:00 01.01.2022");
        Task task2 = new Task("Расшифровка", "Расшифровать задуманное задание", 25, "02:00 01.01.2022");

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        Epic epic1 = new Epic("Сделать Финальный проект", "Переписать прогу в 4ый раз");

        taskManager.addNewEpic(epic1);

        Subtask subtask3_1 = new Subtask("Удалить", "Удалить уже готовый консольный шедевр", epic1.getIdentifier(), 25, "03:00 01.01.2022");
        Subtask subtask3_2 = new Subtask("Осознать суть", "Понять что она должна передавать объект", epic1.getIdentifier(), 25, "04:00 01.01.2022");
        Subtask subtask3_3 = new Subtask("Превозмочь", "Проломить все стены своей головой", epic1.getIdentifier(), 25, "05:00 01.01.2022");
        Subtask subtask3_4 = new Subtask("Закончить", "Написать очередной шедевр", epic1.getIdentifier(), 25, "06:00 01.01.2022");
        Subtask subtask3_5 = new Subtask("Ревью", "Сдать на ревью", epic1.getIdentifier(), 25, "07:00 01.01.2022");

        taskManager.addNewSubtask(subtask3_1);
        taskManager.addNewSubtask(subtask3_2);
        taskManager.addNewSubtask(subtask3_3);
        taskManager.addNewSubtask(subtask3_4);
        taskManager.addNewSubtask(subtask3_5);

        Epic epic2 = new Epic("Благодарность", "Поблагодарить Сергея за прекрасное ревью");

        taskManager.addNewEpic(epic2);

        //taskManager.updateSubtask(new Subtask("Желание все бросить", Status.IN_PROCESS, subtask3_1.getIdentifier(), "Это самое отвратительное тз", epic1.getIdentifier(),25, "01:00 01.01.2022"));

        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getEpic(9);
        taskManager.getSubtask(4);

    }

    public static FileBackedTasksManager loadFromFile(File file) {
        try {
            FileBackedTasksManager manager = new FileBackedTasksManager(file);
            if (file.exists()) {
                String fileContents = Files.readString(Path.of(file.getAbsolutePath()), StandardCharsets.UTF_8);
                String[] line = fileContents.split("\n");
                if (!line[0].equals("")) {
                    manager.readTasks(line, manager);
                    boolean isEmptiness = line[line.length - 1].equals("Пусто");
                    manager.readHistory(isEmptiness, line);
                }
            }
            return manager;
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> idTasks = new ArrayList<>();
        try {
            String[] idTasksHistory = value.split(",");
            for (String id : idTasksHistory) {
                idTasks.add(Integer.parseInt(id));
            }
        } catch (NumberFormatException e) {
            return new ArrayList<>();
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
        long duration = Long.parseLong(dataTask[6]);
        if (dataTask[1].equals(Tasks.TASK.getTaskType())) {
            return new Task(dataTask[2], Status.valueOf(dataTask[3]),
                    Integer.parseInt(dataTask[0]),
                    dataTask[4],
                    duration,
                    dataTask[7]);
        } else if (dataTask[1].equals(Tasks.EPIC.getTaskType())) {
            return new Epic(dataTask[2],
                    Status.valueOf(dataTask[3]),
                    Integer.parseInt(dataTask[0]),
                    dataTask[4]);
        } else {
            int epicId = Integer.parseInt(dataTask[5]);
            Subtask subtask = new Subtask(dataTask[2],
                    Status.valueOf(dataTask[3]),
                    Integer.parseInt(dataTask[0]),
                    dataTask[4],
                    epicId,
                    duration,
                    dataTask[7]);
            Epic epic = epics.get(epicId);
            epic.addSubtasks(subtask);
            return subtask;
        }

    }

    private void readTasks(String[] line, FileBackedTasksManager manager) {
        for (int i = 1; i < (line.length - 2); i++) {
            Task task = manager.fromString(line[i]);
            if (task instanceof Epic) {
                identifier = task.getIdentifier();
                epics.put(identifier, (Epic) task);
            } else if (task instanceof Subtask && !isIntersections(task)) {
                identifier = task.getIdentifier();
                subtasks.put(identifier, (Subtask) task);
                sorterTask.add(task);
            } else if(!isIntersections(task)){
                identifier = task.getIdentifier();
                tasks.put(identifier, task);
                sorterTask.add(task);
            }
        }
    }

    private void readHistory(boolean isEmptiness, String[] line) {
        if (!isEmptiness) {
            for (Integer id : historyFromString(line[line.length - 1])) {
                if (epics.containsKey(id)) {
                    history.addHistory(epics.get(id));
                } else if (subtasks.containsKey(id)) {
                    history.addHistory(subtasks.get(id));
                } else if (tasks.containsKey(id)) {
                    history.addHistory(tasks.get(id));
                }
            }
        }
    }

    protected void save(){
        try (Writer writer = new FileWriter(file.getAbsoluteFile(), StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic,duration,startTime\n");
            for (Task task : getAllTasks()) {
                writer.write(task.toCSVRow());
            }
            if (history.getHistory().isEmpty()) {
                writer.write("\nПусто");
            } else {
                writer.write(historyToString(history));
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.addAll(getListTasks());
        tasks.addAll(getListEpic());
        tasks.addAll(getListSubtasks());
        Comparator<Task> comparator = Comparator.comparingInt(Task::getIdentifier);
        tasks.sort(comparator);
        return tasks;
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        save();
    }


    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskByIdentifier(int identifier) {
        super.deleteTaskByIdentifier(identifier);
        save();
    }

    @Override
    public void deleteEpicByIdentifier(int identifier) {
        super.deleteEpicByIdentifier(identifier);
        save();
    }

    @Override
    public void deleteSubtaskByIdentifier(int identifier) {
        super.deleteSubtaskByIdentifier(identifier);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();

        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }
}
