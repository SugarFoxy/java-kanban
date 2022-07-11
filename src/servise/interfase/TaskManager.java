package servise.interfase;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

//    HashMap<Integer, Task> tasks = new HashMap<>();
//    HashMap<Integer, Epic> epics = new HashMap<>();
//    HashMap<Integer, Subtask> subtasks = new HashMap<>();



    void addNewTask(Task task); //{

    void addNewEpic(Epic epic);

    void addNewSubtask(Subtask subtask);

    /**
     * Вывод всех задач
     */

    List<Task> getListTasks();

    List<Epic> getListEpic();

    List<Subtask> getListSubtasks();

    /**
     * Удаление всех задач
     */

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    /**
     * Получение по идентификатору
     */

    Task getTaskByIdentifier(int identifier);

    Epic getEpicByIdentifier(int identifier);

    Subtask getSubtaskByIdentifier(int identifier);

    /**
     * Получение списка всех подзадач эпика
     */

    List<Subtask> getListSubtaskFromEpic(int epicId);

    /**
     * Обновление данных
     */

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    /**
     * Удаление по идентификатору
     */

    void deleteTaskByIdentifier(int identifier);

    void deleteEpicByIdentifier(int identifier);

    void deleteSubtaskByIdentifier(int identifier);

    /**
     * Получение задачи
     */
    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

}

