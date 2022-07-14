package servise;

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
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int identifier = 0;

    private int getId() {
        return ++identifier;
    }

    /**
     * Создание задач
     */

    @Override
    public void addNewTask(Task task) {
        task.setIdentifier(getId());
        tasks.put(task.getIdentifier(), task);
    }

    @Override
    public void addNewEpic(Epic epic) {
        epic.setIdentifier(getId());
        epics.put(epic.getIdentifier(), epic);
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        int epicId = subtask.getIdentifierEpic();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        int subtaskId = getId();
        subtask.setIdentifier(subtaskId);
        subtasks.put(subtaskId, subtask);
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
        if (tasks.isEmpty()) {
            tasks.clear();
        }
    }

    @Override
    public void deleteAllEpics() {
        if (epics.isEmpty()) {
            epics.clear();
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
            tasks.remove(identifier);
        }
    }

    @Override
    public void deleteEpicByIdentifier(int identifier) {
        if (epics.containsKey(identifier)) {
            for (Subtask subtask : epics.get(identifier).getSubtasks()) {
                subtasks.remove(subtask.getIdentifier());
            }
            epics.get(identifier).deleteSubtasks();
            epics.remove(identifier);
        }
    }

    @Override
    public void deleteSubtaskByIdentifier(int identifier) {
        if (subtasks.containsKey(identifier)
                && epics.containsKey(subtasks.get(identifier).getIdentifierEpic())) {
            int epicId = subtasks.get(identifier).getIdentifierEpic();
            epics.get(epicId).deleteSubtask(subtasks.get(identifier));
            subtasks.remove(identifier);
        }
    }

    /**
     * Просмотр задачи
     */
    @Override
    public Task getTask(int id) {
        if (tasks.containsKey(id)) {
            Managers.getDefaultHistory().addHistory(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpic(int id) {
        if (epics.containsKey(id)) {
            Managers.getDefaultHistory().addHistory(epics.get(id));
            return epics.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Managers.getDefaultHistory().addHistory(subtasks.get(id));
            return subtasks.get(id);
        }
        return null;
    }


    @Override
    public List<Task> getHistory() {
        return Managers.getDefaultHistory().getHistory();
    }


}



