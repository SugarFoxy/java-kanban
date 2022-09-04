package ManagersTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servise.interfase.HistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.IN_PROCESS;
import static tasks.Status.NEW;

abstract class HistoryManagerTest<T extends HistoryManager> {

    T historyManager;
    Epic epic;
    Task task;
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
    public void addHistoryTest() {
        final List<Task> tasksEmpty = historyManager.getHistory();
        historyManager.addHistory(task);
        final List<Task> tasks = historyManager.getHistory();
        historyManager.addHistory(task);
        final List<Task> tasksDouble = historyManager.getHistory();
        assertAll(
                () -> assertEquals(0, tasksEmpty.size(), "Список не пуст"),
                () -> assertEquals(1, tasks.size(), "Список не пуст"),
                () -> assertEquals(1, tasksDouble.size(), "Список не пуст")

        );

    }

    @Test
    void remove() {
        historyManager.remove(1);
        final List<Task> tasksEmpty = historyManager.getHistory();
        historyManager.addHistory(task);
        historyManager.addHistory(epic);
        historyManager.addHistory(subtask1);
        final List<Task> tasks = historyManager.getHistory();
        historyManager.remove(5); //середина
        historyManager.remove(4); //конец
        historyManager.remove(1); //начало
        final List<Task> tasksBeforeDelete = historyManager.getHistory();
        assertAll(
                () -> assertEquals(0, tasksEmpty.size(), "Список не пуст"),
                () -> assertEquals(3, tasks.size(), "Список пуст"),
                () -> assertEquals(0, tasksBeforeDelete.size(), "Список не пуст")

        );

    }

    @Test
    void getHistory() {
        final List<Task> tasksEmpty = historyManager.getHistory();
        historyManager.addHistory(task);
        final List<Task> tasks = historyManager.getHistory();
        assertAll(
                () -> assertEquals(0, tasksEmpty.size(), "Список не пуст"),
                () -> assertNotNull(tasksEmpty, "передается пустой список а не нал"),
                () -> assertEquals(1, tasks.size(), "Список пуст")

        );
    }
}