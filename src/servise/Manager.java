package servise;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import tasks.*;

public class Manager {
    private int identifier = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    /**
     * Создание задач
     */

    public void addNewTask(Task task) {
        task.setIdentifier(++identifier);
        tasks.put(task.getIdentifier(), task);
    }

    public void addNewEpic(Epic epic) {
        epic.setIdentifier(++identifier);
        epics.put(epic.getIdentifier(), epic);
    }


    public void addNewSubtask(Subtask subtask) {
        int epicId = subtask.getIdentifierEpic();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        int subtaskId = ++identifier;
        subtask.setIdentifier(subtaskId);
        subtasks.put(subtaskId, subtask);
        epic.addSubtasks(subtask);
        epic.recountEpicStatus();
    }

    /**
     * Вывод всех задач
     */

    public List<Task> getListTasks() {
        Collection<Task> values = tasks.values();
        return new ArrayList<>(values);
    }

    public List<Epic> getListEpic() {
        Collection<Epic> values = epics.values();
        return new ArrayList<>(values);
    }

    public List<Subtask> getListSubtasks() {
        Collection<Subtask> values = subtasks.values();
        return new ArrayList<>(values);
    }

    /**
     * Удаление всех задач
     */

    public void deleteAllTasks() {
        if (tasks.size() != 0) {
            tasks.clear();
        }
    }

    public void deleteAllEpics() {
        if (epics.size() != 0) {
            epics.clear();
            subtasks.clear();
        }
    }

    public void deleteAllSubtasks() {
        if (subtasks.size() != 0) {
            subtasks.clear();
        }
    }

    /**
     * Получение по идентификатору
     */

    public Task getTaskByIdentifier(int identifier) {
        return tasks.get(identifier);
    }

    public Epic getEpicByIdentifier(int identifier) {
        return epics.get(identifier);
    }

    public Subtask getSubtaskByIdentifier(int identifier) {
        return subtasks.get(identifier);
    }


    /**
     * Получение списка всех подзадач эпика
     */

    public List<Subtask> getListSubtaskFromEpic(int epicId) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Subtask subtask : epics.get(epicId).getSubtasks()) {
            subtasks.add(this.subtasks.get(subtask.getIdentifier()));
        }
        return subtasks;
    }

    /**
     * Обновление данных
     */

    public void updateTask(Task task) {
        if (getTaskByIdentifier(task.getIdentifier()) != null ) {
            tasks.put(task.getIdentifier(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (getEpicByIdentifier(epic.getIdentifier()) != null) {
            epic.setSubtasks(epics.get(epic.getIdentifier()).getSubtasks());
            epics.put(epic.getIdentifier(), epic);
            epic.recountEpicStatus();
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (getSubtaskByIdentifier(subtask.getIdentifier()) != null) {
            subtasks.put(subtask.getIdentifier(), subtask);
            epics.get(subtask.getIdentifierEpic()).recountEpicStatus();
        }
    }

    /**
     * Удаление по идентификатору
     */

    public void deleteTaskByIdentifier(int identifier) {
        if (getTaskByIdentifier(identifier) != null) {
            tasks.remove(identifier);
        }
    }

    public void deleteEpicByIdentifier(int identifier) {
        if (getEpicByIdentifier(identifier) != null) {
            for (Subtask subtask: epics.get(identifier).getSubtasks()){
                subtasks.remove(subtask.getIdentifier());
            }
            epics.get(identifier).deleteSubtasks();
            epics.remove(identifier);
        }
    }

    public void deleteSubtaskByIdentifier(int identifier) {
        if (getSubtaskByIdentifier(identifier) != null
                && getEpicByIdentifier(subtasks.get(identifier).getIdentifierEpic()) != null) {
            int epicId = subtasks.get(identifier).getIdentifierEpic();
            subtasks.remove(identifier);
            epics.get(epicId).recountEpicStatus();
        }
    }
}



