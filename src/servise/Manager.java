package servise;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import status.Status;
import tasks.*;

public class Manager {
    private int identifier = 0;
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();

    /**
     * Создание задач
     */

    public void addNewTask(Task task) {
        task.setIdentifier(++identifier);
        taskHashMap.put(task.getIdentifier(), task);
    }

    public void addNewEpic(Epic epic) {
        epic.setIdentifier(++identifier);
        epicHashMap.put(epic.getIdentifier(), epic);
    }


    public void addNewSubtask(Subtask subtask) {
        int epicId = subtask.getIdentifierEpic();
        Epic epic = epicHashMap.get(epicId);
        if (epic == null) {
            return;
        }
        int subtaskId = ++identifier;
        subtask.setIdentifier(subtaskId);
        subtaskHashMap.put(subtaskId, subtask);
        epic.putSubtasks(subtaskId);
    }

    /**
     * Вывод всех задач
     */

    public List<Task> getListTasks() {
        Collection<Task> values = taskHashMap.values();
        return new ArrayList<>(values);
    }

    public List<Epic> getListEpic() {
        Collection<Epic> values = epicHashMap.values();
        return new ArrayList<>(values);
    }

    public List<Subtask> getListSubtasks() {
        Collection<Subtask> values = subtaskHashMap.values();
        return new ArrayList<>(values);
    }

    /**
     * Удаление всех задач
     */

    public void deleteAllTasks() {
        if (taskHashMap.size() != 0 || epicHashMap.size() != 0 || subtaskHashMap.size() != 0) {
            taskHashMap.clear();
            epicHashMap.clear();
            subtaskHashMap.clear();
            identifier = 0;
        }
    }

    /**
     * Получение по идентификатору
     */

    public Task getTaskByIdentifierOrNull(int identifier) {
        if (taskHashMap.containsKey(identifier)) {
            return taskHashMap.get(identifier);
        }
        return null;
    }

    public Epic getEpicByIdentifierOrNull(int identifier) {
        if (epicHashMap.containsKey(identifier)) {
            return epicHashMap.get(identifier);
        }
        return null;
    }

    public Subtask getSubtaskByIdentifierOrNull(int identifier) {
        if (subtaskHashMap.containsKey(identifier)) {
            return subtaskHashMap.get(identifier);
        }
        return null;
    }


    /**
     * Получение списка всех подзадач эпика
     */

    public List<Subtask> getListSubtaskFromEpic(int epicId) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Integer subtaskId : epicHashMap.get(epicId).getSubtasksId()) {
            subtasks.add(subtaskHashMap.get(subtaskId));
        }
        return subtasks;
    }

    /**
     * Обновление данных
     */

    public void updateTask(int taskId, Status status, Task task) {
        if (getTaskByIdentifierOrNull(taskId) != null ) {
            task.setStatus(taskHashMap.get(taskId).getStatus());
            Status oldStatus = task.getStatus();
            if (!status.equals(Status.NEW)) {
                changeStatusTask(taskId, status, task);
            } else if (oldStatus.equals(Status.NEW) && status.equals(Status.NEW)) {
                taskHashMap.put(taskId, task);
            }
        }
    }

    public void updateEpic(int epicId, Epic epic) {
        if (getEpicByIdentifierOrNull(epicId) != null) {
            epic.setStatus(epicHashMap.get(epicId).getStatus());
            epic.setSubtasksId(epicHashMap.get(epicId).getSubtasksId());
            epicHashMap.put(epicId, epic);
        }
    }

    public void updateSubtask(int subtaskId, Status status, Subtask subtask) {
        if (getSubtaskByIdentifierOrNull(subtaskId) != null) {
            subtask.setStatus(subtaskHashMap.get(subtaskId).getStatus());
            Status oldStatus = subtask.getStatus();
            subtaskHashMap.put(subtaskId, subtask);
            if (!status.equals(Status.NEW)) {
                changeStatusSubtask(subtaskId, status, subtask);
            } else if (oldStatus.equals(Status.NEW) && status.equals(Status.NEW)) {
                subtaskHashMap.put(subtaskId, subtask);
            }
        }
    }

    /**
     * Удаление по идентификатору
     */

    public void deleteTaskByIdentifier(int identifier) {
        if (getTaskByIdentifierOrNull(identifier) != null) {
            taskHashMap.remove(identifier);
        }
    }

    public void deleteEpicByIdentifier(int identifier) {
        if (getEpicByIdentifierOrNull(identifier) != null) {
            for (Integer idSubtask:epicHashMap.get(identifier).getSubtasksId()){
                subtaskHashMap.remove(idSubtask);
            }
            epicHashMap.remove(identifier);
        }
    }

    public void deleteSubtaskByIdentifier(int identifier) {
        if (getSubtaskByIdentifierOrNull(identifier) != null) {
            subtaskHashMap.remove(identifier);
        }
    }

    /**
     * Смена статуса
     */

    private void changeStatusTask(int taskId, Status newStatus, Task task) {
        Status statusOld = task.getStatus();
        if (!statusOld.equals(Status.IN_PROCESS)
                && !statusOld.equals(Status.DONE)
                && newStatus.equals(Status.IN_PROCESS)) {
            setTaskStatus(taskId, newStatus, task);
        } else if (!statusOld.equals(Status.DONE)
                && statusOld.equals(Status.IN_PROCESS)
                && newStatus.equals(Status.DONE)) {
            setTaskStatus(taskId, newStatus, task);
        }
    }

    private void changeStatusSubtask(int subtaskId, Status newStatus, Subtask subtask) {
        Status statusOld = subtask.getStatus();
        if (!statusOld.equals(Status.IN_PROCESS)
                && !statusOld.equals(Status.DONE)
                && newStatus.equals(Status.IN_PROCESS)) {
            setSubtaskStatus(subtaskId, newStatus, subtask);
        } else if (!statusOld.equals(Status.DONE)
                && statusOld.equals(Status.IN_PROCESS)
                && newStatus.equals(Status.DONE)) {
            setSubtaskStatus(subtaskId, newStatus, subtask);
        }
    }

    private void setStatusEpic(int epicId) {
        int sumTrueElement = 0;
        Epic epic =epicHashMap.get(epicId);
        List<Subtask> subtasks = getListSubtaskFromEpic(epicId);
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus().equals(Status.IN_PROCESS)) {
                if (epic.getStatus().equals(Status.NEW) ) {
                    epic.setStatus(Status.IN_PROCESS);
                    break;
                }
            } else if (subtask.getStatus().equals(Status.DONE)) {
                sumTrueElement++;
                if (sumTrueElement == subtasks.size()
                        && epic.getStatus().equals(Status.IN_PROCESS)) {
                    epic.setStatus(Status.DONE);
                }
            }
        }
    }

    private void setSubtaskStatus(int subtaskId, Status newStatus, Subtask subtask) {
        subtaskHashMap.put(subtaskId, subtask);
        subtaskHashMap.get(subtaskId).setStatus(newStatus);
        setStatusEpic(subtask.getIdentifierEpic());
    }

    private void setTaskStatus(int taskId, Status newStatus, Task task) {
        taskHashMap.put(taskId, task);
        taskHashMap.get(taskId).setStatus(newStatus);
    }
}



