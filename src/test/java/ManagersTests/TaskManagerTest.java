package ManagersTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import servise.InMemoryTaskManager;
import servise.interfase.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tasks.Status.*;

abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;
    Task task;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;


    @BeforeEach
    void initTasks() {

        epic = new Epic("Test addNewTask", NEW, 5, "Test addNewTask description");
        subtask2 = new Subtask("a", NEW, 2, "b", 5, 25, "01:00 01.01.2015");
        subtask3 = new Subtask("a", IN_PROCESS, 3, "b", 5, 25, "03:00 01.01.2015");
        subtask1 = new Subtask("a", NEW, 4, "b", 5, 25, "02:00 01.01.2015");
        task = new Task("Test addNewTask", NEW, 1, "Test addNewTask description", 25, "00:00 01.01.1001");
    }

    @Test
    @DisplayName("Проверка добавления")
    void addNewTasTest() {
        taskManager.addNewTask(task);
        final int taskId = task.getIdentifier();
        final Task savedTask = taskManager.getTask(taskId);
        assertAll(
                () -> assertNotNull(savedTask, "Задача не найдена."),
                () -> assertEquals(task, savedTask, "Задачи не совпадают.")
        );

        final List<Task> tasks = taskManager.getListTasks();
        assertAll(
                () -> assertNotNull(tasks, "Задачи не возвращаются."),
                () -> assertEquals(1, tasks.size(), "Неверное количество задач."),
                () -> assertEquals(task, tasks.get(0), "Задачи не совпадают.")
        );
        Task task1 = new Task("Test addNewTask", NEW, 2, "Test addNewTask description", 25, "00:00 01.01.1001");
        taskManager.addNewTask(task1);
        final List<Task> tasks1 = taskManager.getListTasks();
        assertAll(
                () -> assertEquals(1, tasks1.size(), "Пересечение не должно быть записано")
        );

    }

    @Test
    void addNewEpicTest() {
        taskManager.addNewEpic(epic);
        final int taskId = epic.getIdentifier();

        final Epic savedTask = taskManager.getEpic(taskId);
        assertAll(
                () -> assertNotNull(savedTask, "Задача не найдена."),
                () -> assertEquals(epic, savedTask, "Задачи не совпадают.")
        );

        final List<Epic> epics = taskManager.getListEpic();
        assertAll(
                () -> assertNotNull(epics, "Задачи не возвращаются."),
                () -> assertEquals(1, epics.size(), "Неверное количество задач."),
                () -> assertEquals(epic, epics.get(0), "Задачи не совпадают.")
        );
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask3);
        assertAll(
                () -> assertEquals(IN_PROCESS, epics.get(0).getStatus(), "Статус изменяется не корректно")
        );
        final List<Subtask> subtasksFromEpic = taskManager.getListSubtaskFromEpic(taskId);
        assertAll(
                () -> assertNotNull(subtasksFromEpic, "Задачи не возвращаются."),
                () -> assertEquals(2, subtasksFromEpic.size(), "Неверное количество задач.")
        );

    }

    @Test
    void addNewSubtaskTest() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        final int taskId = subtask1.getIdentifier();

        final Subtask savedTask = taskManager.getSubtask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(subtask1, savedTask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getListSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask1, subtasks.get(0), "Задачи не совпадают.");

        assertEquals(epic, taskManager.getEpic(subtask1.getIdentifierEpic()), "Id эпика в подзадаче не совпадает.");
        taskManager.addNewSubtask(new Subtask("a", Status.NEW, 6, "b", 1, 25, "01:00 01.01.2015"));
        taskManager.addNewSubtask(new Subtask("a", Status.NEW, 8, "b", 5, 25, "02:00 01.01.2015"));
        final List<Subtask> subtasks1 = taskManager.getListSubtasks();
        assertEquals(1, subtasks1.size(), "Пересечение не должно быть записано");
    }

    @Test
    void getListTasksTest() {
        List<Task> tasksEmpty = taskManager.getListTasks();

        taskManager.addNewTask(task);
        List<Task> tasksStandard = taskManager.getListTasks();

        assertAll(
                () -> assertEquals(0, tasksEmpty.size(), "Должно быть пусто"),
                () -> assertEquals(1, tasksStandard.size(), "Неверное количество задач.")
        );
    }

    @Test
    void getListEpicsTest() {
        List<Epic> epicsEmpty = taskManager.getListEpic();

        taskManager.addNewEpic(epic);
        List<Epic> epicsStandard = taskManager.getListEpic();

        assertAll(
                () -> assertEquals(0, epicsEmpty.size(), "Должно быть пусто"),
                () -> assertEquals(1, epicsStandard.size(), "Неверное количество задач.")
        );
    }

    @Test
    void getListSubtasksTest() {
        List<Subtask> subtasksEmpty = taskManager.getListSubtasks();


        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        List<Subtask> subtasksStandard = taskManager.getListSubtasks();


        assertAll(
                () -> assertEquals(0, subtasksEmpty.size(), "Должно быть пусто"),
                () -> assertEquals(1, subtasksStandard.size(), "Неверное количество задач.")
        );
    }

    @Test
    void deleteAllTasksTest() {
        taskManager.addNewTask(task);
        List<Task> tasks = taskManager.getListTasks();
        String message = "Неверное количество задач.";
        assertEquals(1, tasks.size(), message);
        taskManager.deleteAllTasks();
        List<Task> tasksAfter = taskManager.getListTasks();

        message = "Задачи не удалены";
        assertEquals(0, tasksAfter.size(), message);
    }

    @Test
    void deleteAllEpicsTest() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        final List<Epic> epics = taskManager.getListEpic();
        final List<Subtask> subtasks = taskManager.getListSubtasks();
        String message = "Неверное количество задач.";
        assertEquals(1, epics.size(), message);
        assertEquals(1, subtasks.size(), message);
        taskManager.deleteAllEpics();
        final List<Task> epicsAfterDelete = taskManager.getListTasks();
        final List<Subtask> subtasksAfterDelete = taskManager.getListSubtasks();

        assertEquals(0, epicsAfterDelete.size(), "Задачи эпика не удалены");
        assertEquals(0, subtasksAfterDelete.size(), "Подзадачи не удалены");
    }

    @Test
    void deleteAllSubtaskTest() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        List<Subtask> subtasks = taskManager.getListSubtasks();
        String message = "Неверное количество задач.";
        assertEquals(1, subtasks.size(), message);
        taskManager.deleteAllSubtasks();
        List<Task> subtasksAfterDelete = taskManager.getListTasks();

        message = "Задачи не удалены";
        assertEquals(0, subtasksAfterDelete.size(), message);
    }

    @Test
    void getListSubtaskFromEpicStandardTest() {
        final List<Subtask> subtasksFromNonExistentEpic = taskManager.getListSubtaskFromEpic(1);

        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        final List<Subtask> subtasksFromEpic = taskManager.getListSubtaskFromEpic(epic.getIdentifier());

        assertAll(
                () -> assertNull(subtasksFromNonExistentEpic, "задачи не должно существовать "),
                () -> assertNotNull(subtasksFromEpic, "Задачи не возвращаются."),
                () -> assertEquals(1, subtasksFromEpic.size(), "Неверное количество задач.")
        );
    }

    @Test
    void updateTaskStandard() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        taskManager.addNewTask(task);
        taskManager.updateTask(new Task("t", DONE, 1, "t", 20, "00:01 01.01.1001"));
        assertAll(
                () -> assertNotEquals(task.getName(), "Test addNewTask", "Имя задачи не изменилось"),
                () -> assertNotEquals(task.getStatus(), NEW, "Статус задачи не изменился"),
                () -> assertNotEquals(task.getDescription(), "Test addNewTask description", "Описание задачи не изменилось"),
                () -> assertNotEquals(task.getDuration().toMinutes(), 25, "Продолжительность задачи не изменилась"),
                () -> assertNotEquals(task.getStartTime().format(formatter), "00:00 01.01.1001", "Время задачи не изменилось")
        );
    }

    @Test
    void updateNonExistentTask() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        taskManager.addNewTask(task);
        taskManager.updateTask(new Task("t", DONE, 8, "t", 20, "00:01 01.01.1001"));
        assertAll(
                () -> assertEquals(task.getName(), "Test addNewTask", "Имя задачи изменилось"),
                () -> assertEquals(task.getStatus(), NEW, "Статус задачи изменился"),
                () -> assertEquals(task.getDescription(), "Test addNewTask description", "Описание задачи изменилось"),
                () -> assertEquals(task.getDuration().toMinutes(), 25, "Продолжительность задачи изменилась"),
                () -> assertEquals(task.getStartTime().format(formatter), "00:00 01.01.1001", "Время задачи изменилось")
        );
    }

    @Test
    void updateEpicStandard() {
        taskManager.addNewEpic(epic);
        taskManager.updateEpic(new Epic("t", 5, "t"));
        assertAll(
                () -> assertNotEquals(epic.getName(), "Test addNewTask", "Имя задачи не изменилось"),
                () -> assertNotEquals(epic.getDescription(), "Test addNewTask description", "Описание задачи не изменилось"),
                () -> assertEquals(0, epic.getSubtasks().size(), "Количество подзадач поменялось")
        );
    }

    @Test
    void updateNonExistentEpic() {
        taskManager.addNewEpic(epic);
        taskManager.updateEpic(new Epic("t", 1, "t"));
        assertAll(
                () -> assertEquals(epic.getName(), "Test addNewTask", "Имя задачи не изменилось"),
                () -> assertEquals(epic.getDescription(), "Test addNewTask description", "Описание задачи не изменилось"),
                () -> assertEquals(0, epic.getSubtasks().size(), "Количество подзадач поменялось")
        );
    }

    @Test
    void updateSubtaskStandard() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.updateSubtask(new Subtask("t", DONE, 4, "t", 5, 20, "00:01 01.01.1001"));
        assertAll(
                () -> assertNotEquals(subtask1.getName(), "a", "Имя задачи не изменилось"),
                () -> assertNotEquals(subtask1.getStatus(), NEW, "Статус задачи не изменился"),
                () -> assertEquals(DONE, epic.getStatus(), "Статус эпика не изменился"),
                () -> assertNotEquals(subtask1.getDescription(), "b", "Описание задачи не изменилось"),
                () -> assertNotEquals(subtask1.getDuration().toMinutes(), 25, "Продолжительность задачи не изменилась"),
                () -> assertNotEquals(subtask1.getStartTime().format(formatter), "02:00 01.01.2015", "Время задачи не изменилось")
        );
    }

    @Test
    void updateNonExistentSubtask() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.updateSubtask(new Subtask("t", DONE, 8, "t", 5, 20, "00:01 01.01.1001"));
        assertAll(
                () -> assertEquals(subtask1.getName(), "a", "Имя задачи не изменилось"),
                () -> assertEquals(subtask1.getStatus(), NEW, "Статус задачи не изменился"),
                () -> assertEquals(NEW, epic.getStatus(), "Статус эпика не изменился"),
                () -> assertEquals(subtask1.getDescription(), "b", "Описание задачи не изменилось"),
                () -> assertEquals(subtask1.getDuration().toMinutes(), 25, "Продолжительность задачи не изменилась"),
                () -> assertEquals(subtask1.getStartTime().format(formatter), "02:00 01.01.2015", "Время задачи не изменилось")
        );
    }

    @Test
    void deleteTaskByIdentifier() {
        taskManager.addNewTask(task);
        taskManager.addNewTask(new Task("Test addNewTask", NEW, 2, "Test addNewTask description", 25, "01:00 01.01.1001"));
        final List<Task> tasks = taskManager.getListTasks();
        taskManager.deleteTaskByIdentifier(3);
        final List<Task> tasks1 = taskManager.getListTasks();
        taskManager.deleteTaskByIdentifier(2);
        final List<Task> tasks2 = taskManager.getListTasks();
        assertAll(
                () -> assertEquals(2, tasks.size(), "Задачи не сохранились"),
                () -> assertEquals(2, tasks1.size(), "Задача удалилась"),
                () -> assertEquals(1, tasks2.size(), "Задача не удалилась")
        );
    }

    @Test
    void deleteEpicByIdentifier() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewEpic(new Epic("Test addNewTask", 2, "Test addNewTask description"));
        final List<Epic> epics = taskManager.getListEpic();
        final List<Subtask> subtasks = taskManager.getListSubtasks();

        taskManager.deleteEpicByIdentifier(3);
        final List<Epic> epics1 = taskManager.getListEpic();

        taskManager.deleteEpicByIdentifier(5);
        final List<Epic> epics2 = taskManager.getListEpic();
        final List<Subtask> subtasks1 = taskManager.getListSubtasks();

        assertAll(
                () -> assertEquals(2, epics.size(), "Задачи не сохранились"),
                () -> assertEquals(2, subtasks.size(), "Подзадачи не сохранились"),
                () -> assertEquals(2, epics1.size(), "Задача удалилась"),
                () -> assertEquals(1, epics2.size(), "Задача не удалилась"),
                () -> assertEquals(0, subtasks1.size(), "Задачи эпика не удалились")
        );

    }

    @Test
    void deleteSubtaskByIdentifier() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask3);

        final List<Epic> epics = taskManager.getListEpic();
        final List<Subtask> subtasks = taskManager.getListSubtasks();

        taskManager.deleteSubtaskByIdentifier(8);
        final List<Subtask> subtasks1 = taskManager.getListSubtasks();
        Status statusEpicBefore = epic.getStatus();

        taskManager.deleteSubtaskByIdentifier(3);

        Status statusEpicAfter = epic.getStatus();
        final List<Subtask> subtasks2 = taskManager.getListSubtasks();

        assertAll(
                () -> assertEquals(1, epics.size(), "Задачи не сохранились"),
                () -> assertEquals(2, subtasks.size(), "Подзадачи не сохранились"),
                () -> assertEquals(2, subtasks1.size(), "Задача удалилась"),
                () -> assertNotEquals(statusEpicBefore,statusEpicAfter,"статус эпика не изменился"),
                () -> assertEquals(1, subtasks2.size(), "Подзадача не удалилась")
        );
    }

    @Test
    public void getTaskTest(){
        taskManager.addNewTask(task);
        int id = task.getIdentifier();
        assertAll(
                () -> assertEquals(task,taskManager.getTask(id),"Ошибка при получении таски"),
                () -> assertNull(taskManager.getTask(8),"Найдена фантомная таска")
        );
    }

    @Test
    public void getEpicTest(){
        taskManager.addNewEpic(epic);
        int id = epic.getIdentifier();
        assertAll(
                () -> assertEquals(epic,taskManager.getEpic(id),"Ошибка при получении эпика"),
                () -> assertNull(taskManager.getEpic(8),"Найден эпический фантом")
        );
    }

    @Test
    public void getSubtask(){
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        int id = subtask1.getIdentifier();
        assertAll(
                () -> assertEquals(subtask1,taskManager.getSubtask(id),"Ошибка при получении подзадачи"),
                () -> assertNull(taskManager.getSubtask(8),"Найден подзадаченный фантом")
        );
    }

    @Test
    public void getHistoryEmptyTest(){
        taskManager.addNewTask(task);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        final List<Task> historyEmpty = taskManager.getHistory();

        taskManager.getTask(1);
        taskManager.getEpic(5);
        taskManager.getSubtask(4);

        final List<Task> historyFilled = taskManager.getHistory();

        assertAll(
                () -> assertEquals(0,historyEmpty.size()),
                () -> assertEquals(3,historyFilled.size())
        );
    }

    @Test
    public void getPrioritizedTasks(){
        final List<Task> emptyTasks = taskManager.getPrioritizedTasks();

        taskManager.addNewTask(task);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewSubtask(subtask3);

        final List<Task> unsortedTasks = new ArrayList<>();
        unsortedTasks.addAll(taskManager.getListTasks());
        unsortedTasks.addAll(taskManager.getListSubtasks());


        final List<Task> sortedTasks = taskManager.getPrioritizedTasks();

        assertAll(
                () -> assertEquals(0,emptyTasks.size()),
                () -> assertNotEquals(unsortedTasks,sortedTasks,"very strange things")
        );
    }
}
