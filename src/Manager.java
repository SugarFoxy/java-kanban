import java.util.HashMap;

public class Manager {
    Integer identifier = 0;
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
        subtaskHashMap = new HashMap<>();
    }


    public void addNewSubtask(Epic epic, Subtask subtask) {
        subtask.setIdentifier(++identifier);
        subtaskHashMap.put(subtask.getIdentifier(), subtask);
        subtask.setIdentifierEpic(epic.getIdentifier());
        epic.setSubtasksHashMap(subtaskHashMap);
    }

    /**
     * Вывод всех задач
     */

    public HashMap<Integer, Task> getAllTasks() {
        return taskHashMap;
    }

    public HashMap<Integer, Epic> getAllEpics() {
        return epicHashMap;
    }

    public HashMap<Integer, Subtask> getAllSubtasks() {
        HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
        for (Integer key : epicHashMap.keySet()) {
            if (epicHashMap.get(key).getSubtasksHashMap() != null) {
                for (Integer keySub : epicHashMap.get(key).getSubtasksHashMap().keySet()) {
                    subtaskHashMap.put(keySub, epicHashMap.get(key).getSubtasksHashMap().get(keySub));
                }
            }
        }
        return subtaskHashMap;
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
        HashMap<Integer, Subtask> subtasks = getAllSubtasks();
        if (subtasks.containsKey(identifier)) {
            return subtasks.get(identifier);
        }
        return null;
    }

    /**
     * Получение списка всех подзадач эпика
     */

    public HashMap<Integer, Subtask> getListSubtaskFromEpic(Epic epic) {
        return epic.getSubtasksHashMap();
    }

    /**
     * Обновление данных
     */

    public void updateTask(Task task) {
        if (getTaskByIdentifierOrNull(task.getIdentifier()) != null) {
            taskHashMap.put(task.getIdentifier(), task);
            changeStatusTask(task);
        }
    }

    public void updateEpic(Epic epic) {
        if (getEpicByIdentifierOrNull(epic.getIdentifier()) != null) {
            epicHashMap.put(epic.getIdentifier(), epic);
            setStatusEpic(epic);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (getSubtaskByIdentifierOrNull(subtask.getIdentifier()) != null) {
            HashMap<Integer, Subtask> subtasks = epicHashMap.get(subtask.getIdentifierEpic()).getSubtasksHashMap();
            subtasks.put(subtask.getIdentifier(), subtask);
            changeStatusSubtask(subtask);
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
            epicHashMap.remove(identifier);
        }
    }

    public void deleteSubtaskByIdentifier(int identifier) {
        if (getSubtaskByIdentifierOrNull(identifier) != null) {
            int identifierEpic = getAllSubtasks().get(identifier).getIdentifierEpic();
            epicHashMap.get(identifierEpic).getSubtasksHashMap().remove(identifier);
        }
    }

    /**
     * Смена статуса
     */

    private void changeStatusTask(Task task) {
        Task.Status status = getTaskByIdentifierOrNull(task.getIdentifier()).getStatus();
        if (!status.equals(Task.Status.IN_PROCESS)
                && !status.equals(Task.Status.DONE)
                && task.getStatus().equals(Task.Status.IN_PROCESS)) {
            taskHashMap.put(task.getIdentifier(), task);
        } else if (!status.equals(Task.Status.DONE) && status.equals(Task.Status.IN_PROCESS)
                && task.getStatus().equals(Task.Status.DONE)) {
            taskHashMap.put(task.getIdentifier(), task);
        }
    }

    private void changeStatusSubtask(Subtask subtask) {
        Subtask oldSubtask = getSubtaskByIdentifierOrNull(subtask.getIdentifier());
        Task.Status status = oldSubtask.getStatus();
        HashMap<Integer, Subtask> newSubtask = epicHashMap.get(oldSubtask.getIdentifierEpic()).getSubtasksHashMap();
        if (!status.equals(Task.Status.IN_PROCESS)
                && !status.equals(Task.Status.DONE)
                && subtask.getStatus().equals(Task.Status.IN_PROCESS)) {
            newSubtask.put(subtask.getIdentifier(), subtask);
            setStatusEpic(epicHashMap.get(oldSubtask.getIdentifierEpic()));
        } else if (!status.equals(Task.Status.DONE)
                && status.equals(Task.Status.IN_PROCESS)
                && subtask.getStatus().equals(Task.Status.DONE)) {
            newSubtask.put(subtask.getIdentifier(), subtask);
            setStatusEpic(epicHashMap.get(oldSubtask.getIdentifierEpic()));
        }
    }

    private void setStatusEpic(Epic epic) {
        HashMap<Integer, Subtask> subtask = epicHashMap.get(epic.getIdentifier()).getSubtasksHashMap();
        int sumTrueElement = 0;
        for (Integer key : subtask.keySet()) {
            if (subtask.get(key).getStatus().equals(Task.Status.IN_PROCESS)) {
                sumTrueElement++;
                if (sumTrueElement > 0
                        && !epic.getStatus().equals(Task.Status.IN_PROCESS)
                        && !epic.getStatus().equals(Task.Status.DONE)) {
                    epic.setStatus(Task.Status.IN_PROCESS);
                    epicHashMap.put(epic.getIdentifier(),
                            new Epic(epic.getName(), epic.getStatus(), epic.getIdentifier(), subtask));
                    break;
                }
            } else if (subtask.get(key).getStatus().equals(Task.Status.DONE)) {
                sumTrueElement++;
                if (sumTrueElement == subtask.size()
                        && !epic.getStatus().equals(Task.Status.DONE)
                        && epic.getStatus().equals(Task.Status.IN_PROCESS)) {
                    epic.setStatus(Task.Status.DONE);
                    epicHashMap.put(epic.getIdentifier(),
                            new Epic(epic.getName(), epic.getStatus(), epic.getIdentifier(), subtask));
                }

            }
        }
    }

}



