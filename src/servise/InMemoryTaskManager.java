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
    private int identifier = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
//    List<Task> browsingHistory = new ArrayList<>();
InMemoryHistoryManager historyManager= new InMemoryHistoryManager();

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
        if (tasks.size() != 0) {
            tasks.clear();
        }
    }

    @Override
    public void deleteAllEpics() {
        if (epics.size() != 0) {
            epics.clear();
            subtasks.clear();
        }
    }

    @Override
    public void deleteAllSubtasks() {
        if (!subtasks.isEmpty()) {
            int epicId = 0;
            for (Subtask subtask : subtasks.values()) {
                if (epicId != subtask.getIdentifierEpic()) {
                    epicId = subtask.getIdentifierEpic();
                    subtasks.clear();
                    epics.get(epicId).updateStatus();
                }
            }
        }
    }

    /**
     * Получение по идентификатору
     */

    @Override
    public Task getTaskByIdentifier(int identifier) {
        return tasks.get(identifier);
    }

    @Override
    public Epic getEpicByIdentifier(int identifier) {
        return epics.get(identifier);
    }

    @Override
    public Subtask getSubtaskByIdentifier(int identifier) {
        return subtasks.get(identifier);
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
        if (getTaskByIdentifier(task.getIdentifier()) != null) {
            tasks.put(task.getIdentifier(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (getEpicByIdentifier(epic.getIdentifier()) != null) {
            Epic oldEpic = epics.get(epic.getIdentifier());
            String newName = epic.getName();
            String newDescription = epic.getDescription();
            oldEpic.setName(newName);
            oldEpic.setDescription(newDescription);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (getSubtaskByIdentifier(subtask.getIdentifier()) != null) {
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
        if (getTaskByIdentifier(identifier) != null) {
            tasks.remove(identifier);
        }
    }

    @Override
    public void deleteEpicByIdentifier(int identifier) {
        if (getEpicByIdentifier(identifier) != null) {
            for (Subtask subtask : epics.get(identifier).getSubtasks()) {
                subtasks.remove(subtask.getIdentifier());
            }
            epics.get(identifier).deleteSubtasks();
            epics.remove(identifier);
        }
    }

    @Override
    public void deleteSubtaskByIdentifier(int identifier) {
        if (getSubtaskByIdentifier(identifier) != null
                && getEpicByIdentifier(subtasks.get(identifier).getIdentifierEpic()) != null) {
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
            historyManager.addHistory(tasks.get(id));
            deleteExtraElement();
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpic(int id) {
        if (epics.containsKey(id)) {
            historyManager.addHistory(epics.get(id));
            deleteExtraElement();
            return epics.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtask(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.addHistory(subtasks.get(id));
            deleteExtraElement();
            return subtasks.get(id);
        }
        return null;
    }
    private void deleteExtraElement(){
        if(historyManager.browsingHistory.size() > historyManager.AMOUNT_OF_ELEMENTS_IN_HISTORY){
            historyManager.browsingHistory.remove(historyManager.AMOUNT_OF_ELEMENTS_IN_HISTORY);
        }
    }

    /**
     * История просмотров задач
     */

}



