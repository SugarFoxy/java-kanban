package servise;

import servise.interfase.HistoryManager;
import servise.interfase.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    protected final HistoryManager history = Managers.getDefaultHistory();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int identifier = 0;

    private int getId() {
        return ++identifier;
    }

    /**
     * Создание задач
     */

    @Override
    public void addNewTask(Task task) {
        if (task.getIdentifier() == 0) {
            task.setIdentifier(getId());
        }
        if (task.getIdentifier() > identifier) {
            identifier = task.getIdentifier();
        }
        tasks.put(task.getIdentifier(), task);
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
        epic.addSubtasks(subtask);
        epic.updateStatus();
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
                if (epicId != subtask.getIdentifierEpic()) {
                    epicId = subtask.getIdentifierEpic();
                    history.remove(subtask.getIdentifier());
                    epicsId.add(epicId);
                }
            }
            subtasks.clear();
            for (Integer id : epicsId) {
                epics.get(id).getSubtasks().clear();
                epics.get(id).updateStatus();
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
            tasks.put(task.getIdentifier(), task);
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
            oldSubtask.setName(newName);
            oldSubtask.setDescription(newDescription);
            if (!subtask.getStatus().equals(oldSubtask.getStatus())) {
                Status newStatus = subtask.getStatus();
                oldSubtask.setStatus(newStatus);
                epics.get(subtask.getIdentifierEpic()).updateStatus();
            }
        }
    }

    /**
     * Удаление по идентификатору
     */

    @Override
    public void deleteTaskByIdentifier(int identifier) {
        if (tasks.containsKey(identifier)) {
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
}



