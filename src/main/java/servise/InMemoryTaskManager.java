package servise;

import servise.interfase.HistoryManager;
import servise.interfase.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HistoryManager history = Managers.getDefaultHistory();
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    protected TreeSet<Task> sorterTask = new TreeSet(Comparator.comparing(Task::getStartTime));
    protected int identifier = 0;

    private int getId() {
        return ++identifier;
    }

    /**
     * Создание задач
     */

    @Override
    public void addNewTask(Task task) {
        if (isNoIntersections(task)) {
            if (task.getIdentifier() == 0) {
                task.setIdentifier(getId());
            }
            if (task.getIdentifier() > identifier) {
                identifier = task.getIdentifier();
            }
            tasks.put(task.getIdentifier(), task);
            sorterTask.add(task);
        }
    }

    @Override
    public void addNewEpic(Epic epic) {
        if (epic.getIdentifier() == 0) {
            epic.setIdentifier(getId());
        }
        if (epic.getIdentifier() > identifier) {
            identifier = epic.getIdentifier();
        }
        epics.put(epic.getIdentifier(), epic);
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        if (isNoIntersections(subtask)) {
            int epicId = subtask.getIdentifierEpic();
            Epic epic = epics.get(epicId);
            if (epic == null) {
                return;
            }
            if (subtask.getIdentifier() == 0) {
                subtask.setIdentifier(getId());
            }
            if (subtask.getIdentifier() > identifier) {
                identifier = subtask.getIdentifier();
            }
            subtasks.put(subtask.getIdentifier(), subtask);
            sorterTask.add(subtask);
            epic.addSubtasks(subtask);
        }
    }

    /**
     * Вывод всех задач
     */

    @Override
    public List<Task> getListTasks() {
        Collection<Task> values = tasks.values();
        return new ArrayList<>(values);
    }

    @Override
    public List<Epic> getListEpic() {
        Collection<Epic> values = epics.values();
        return new ArrayList<>(values);
    }

    @Override
    public List<Subtask> getListSubtasks() {
        Collection<Subtask> values = subtasks.values();
        return new ArrayList<>(values);
    }

    /**
     * Удаление всех задач
     */

    @Override
    public void deleteAllTasks() {
        if (!tasks.isEmpty()) {
            for (Task task : tasks.values()) {
                sorterTask.remove(tasks.get(task.getIdentifier()));
                history.remove(task.getIdentifier());
            }
            tasks.clear();
        }
    }

    @Override
    public void deleteAllEpics() {
        if (!epics.isEmpty()) {
            for (Epic epic : epics.values()) {
                history.remove(epic.getIdentifier());
            }
            epics.clear();
            for (Subtask subtask : subtasks.values()) {
                history.remove(subtask.getIdentifier());
            }
            subtasks.clear();
        }
    }

    @Override
    public void deleteAllSubtasks() {
        if (!subtasks.isEmpty()) {
            List<Integer> epicsId = new ArrayList<>();
            int epicId = 0;
            for (Subtask subtask : subtasks.values()) {
                sorterTask.remove(subtask);
                if (epicId != subtask.getIdentifierEpic()) {
                    epicId = subtask.getIdentifierEpic();
                    history.remove(subtask.getIdentifier());
                    epicsId.add(epicId);
                }
            }
            subtasks.clear();
            for (Integer id : epicsId) {
                epics.get(id).deleteSubtasks();
            }
        }
    }

    /**
     * Получение списка всех подзадач эпика
     */

    @Override
    public List<Subtask> getListSubtaskFromEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<Subtask> subtasks = new ArrayList<>();
            for (Subtask subtask : epics.get(epicId).getSubtasks()) {
                subtasks.add(this.subtasks.get(subtask.getIdentifier()));
            }
            return subtasks;
        }
        return null;
    }

    /**
     * Обновление данных
     */

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getIdentifier())) {
            Task oldTask = tasks.get(task.getIdentifier());
            oldTask.setName(task.getName());
            oldTask.setDescription(task.getDescription());
            oldTask.setStatus(task.getStatus());
            oldTask.setDuration(task.getDuration());
            oldTask.setStartTime(task.getStartTime());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getIdentifier())) {
            Epic oldEpic = epics.get(epic.getIdentifier());
            String newName = epic.getName();
            String newDescription = epic.getDescription();
            oldEpic.setName(newName);
            oldEpic.setDescription(newDescription);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getIdentifier())) {
            Subtask oldSubtask = subtasks.get(subtask.getIdentifier());
            String newName = subtask.getName();
            String newDescription = subtask.getDescription();
            Status newStatus = subtask.getStatus();

            epics.get(subtask.getIdentifierEpic()).deleteSubtask(oldSubtask);
            oldSubtask.setName(newName);
            oldSubtask.setDescription(newDescription);
            oldSubtask.setDuration(subtask.getDuration());
            oldSubtask.setStartTime(subtask.getStartTime());
            oldSubtask.setStatus(newStatus);
            epics.get(subtask.getIdentifierEpic()).addSubtasks(oldSubtask);
        }
    }

    /**
     * Удаление по идентификатору
     */

    @Override
    public void deleteTaskByIdentifier(int identifier) {
        if (tasks.containsKey(identifier)) {
            sorterTask.remove(tasks.get(identifier));
            history.remove(identifier);
            tasks.remove(identifier);
        }
    }

    @Override
    public void deleteEpicByIdentifier(int identifier) {
        if (epics.containsKey(identifier)) {
            for (Subtask subtask : epics.get(identifier).getSubtasks()) {
                history.remove(subtask.getIdentifier());
                subtasks.remove(subtask.getIdentifier());
            }
            epics.get(identifier).deleteSubtasks();
            history.remove(identifier);
            epics.remove(identifier);
        }
    }

    @Override
    public void deleteSubtaskByIdentifier(int identifier) {
        if (subtasks.containsKey(identifier)
                && epics.containsKey(subtasks.get(identifier).getIdentifierEpic())) {
            int epicId = subtasks.get(identifier).getIdentifierEpic();
            sorterTask.remove(subtasks.get(identifier));
            epics.get(epicId).deleteSubtask(subtasks.get(identifier));
            history.remove(identifier);
            subtasks.remove(identifier);

        }
    }

    /**
     * Просмотр задачи
     */
    @Override
    public Task getTask(int id) {
        if (tasks.containsKey(id)) {
            history.addHistory(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpic(int id) {
        if (epics.containsKey(id)) {
            history.addHistory(epics.get(id));
            return epics.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtask(int id) {
        if (subtasks.containsKey(id)) {
            history.addHistory(subtasks.get(id));
            return subtasks.get(id);
        }
        return null;
    }


    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sorterTask);
    }

    private boolean isNoIntersections(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        boolean isNoIntersection = true;
        LocalDateTime startTask = task.getStartTime();
        LocalDateTime endTask = task.getEndTime();
        for (Task t : getPrioritizedTasks()) {
            if ((t.getStartTime().isBefore(startTask) && t.getEndTime().isAfter(startTask))
                    || (startTask.isBefore(t.getStartTime()) && endTask.isAfter(t.getStartTime()))
                    || (startTask.format(formatter).equals(t.getStartTime().format(formatter)))) {
                isNoIntersection = false;
                break;
            }
        }
        return isNoIntersection;
    }
}