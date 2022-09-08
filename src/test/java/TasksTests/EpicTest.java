package TasksTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.*;

class EpicTest extends TaskAbstactTest {

    @BeforeEach
    void initData(){
        task1 = new Epic("a", NEW, 1, "b");
        task1_duplicate = new Epic("a", NEW, 1, "b");
        task2 = new Epic("b", NEW, 1, "b");
        task1toCsvString = "1,EPIC,a,NEW,b,,0,0,\n";
        task1toString = "Epic{amount subtasks=0, name='a', status=NEW, identifier=1, description='b', duration= nullmin, startTime= null, endTime= null}";
    }

    @Test
    @DisplayName("Расчет статуса")
    public void updateStatusTest() {
        Epic emptyEpic = new Epic("a", NEW, 1, "b");

        Epic epicAllSubtaskNew = new Epic("a", NEW, 1, "b");
        epicAllSubtaskNew.addSubtasks(new Subtask("a", NEW, 2, "b", 1, 25, "01:00 01.01.2015"));
        epicAllSubtaskNew.addSubtasks(new Subtask("a", NEW, 3, "b", 1, 25, "02:00 01.01.2015"));

        Epic epicAllSubtaskDone = new Epic("a", NEW, 1, "b");
        epicAllSubtaskDone.addSubtasks(new Subtask("a", DONE, 2, "b", 1, 25, "01:00 01.01.2015"));
        epicAllSubtaskDone.addSubtasks(new Subtask("a", DONE, 3, "b", 1, 25, "02:00 01.01.2015"));

        Epic epicSubtaskDoneAndNew = new Epic("a", NEW, 1, "b");
        epicSubtaskDoneAndNew.addSubtasks(new Subtask("a", DONE, 2, "b", 1, 25, "01:00 01.01.2015"));
        epicSubtaskDoneAndNew.addSubtasks(new Subtask("a", NEW, 3, "b", 1, 25, "02:00 01.01.2015"));

        Epic epicAllSubtasksInProcess = new Epic("a", NEW, 1, "b");
        epicAllSubtasksInProcess.addSubtasks(new Subtask("a", IN_PROCESS, 2, "b", 1, 25, "01:00 01.01.2015"));
        epicAllSubtasksInProcess.addSubtasks(new Subtask("a", IN_PROCESS, 3, "b", 1, 25, "02:00 01.01.2015"));

        assertAll(
                () -> assertEquals(NEW, emptyEpic.getStatus(), "Неправильный статус(сабтаски: не заполнены)"),
                () -> assertEquals(NEW, epicAllSubtaskNew.getStatus(), "Неправильный статус(сабтаски: NEW)"),
                () -> assertEquals(DONE, epicAllSubtaskDone.getStatus(), "Неправильный статус(сабтаски: DONE)"),
                () -> assertEquals(IN_PROCESS, epicSubtaskDoneAndNew.getStatus(), "Неправильный статус(сабтаски: DONE и NEW)"),
                () -> assertEquals(IN_PROCESS, epicAllSubtasksInProcess.getStatus(), "Неправильный статус(сабтаски: IN_PROCESS)")
        );
    }

    @Test
    @DisplayName("Удаление сабтасок")
    public void deleteSubtasksTest() {
        Epic epic = new Epic("a", NEW, 1, "b");
        Subtask subtask1 = new Subtask("a", NEW, 2, "b", 1, 25, "01:00 01.01.2015");
        Subtask subtask2 = new Subtask("a", NEW, 3, "b", 1, 25, "02:00 01.01.2015");
        epic.addSubtasks(subtask1);
        epic.addSubtasks(subtask2);
        epic.deleteSubtasks();

        assertEquals(0, epic.getSubtasks().size());
    }

    @Test
    @DisplayName("Удаление сабтаски")
    public void deleteSubtaskTest() {
        Epic epic = new Epic("a", NEW, 1, "b");
        Subtask subtask1 = new Subtask("a", NEW, 2, "b", 1, 25, "01:00 01.01.2015");
        Subtask subtask2 = new Subtask("a", NEW, 3, "b", 1, 25, "02:00 01.01.2015");
        epic.addSubtasks(subtask1);
        epic.addSubtasks(subtask2);
        epic.deleteSubtask(subtask1);

        assertEquals(1, epic.getSubtasks().size());
    }

    @Test
    @DisplayName("Добавление сабтаски")
    public void addSubtaskTest() {
        Epic epic = new Epic("a", NEW, 1, "b");
        Subtask subtask = new Subtask("a", NEW, 2, "b", 1, 25, "01:00 01.01.2015");
        epic.addSubtasks(subtask);

        assertTrue(epic.getSubtasks().contains(subtask));
    }

    @Test
    public void getEndTime() {
        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        Epic epic = new Epic("a", NEW, 1, "b");
        epic.addSubtasks(new Subtask("a", NEW, 2, "b", 1, 25, "01:00 01.01.2015"));
        epic.addSubtasks(new Subtask("a", NEW, 3, "b", 1, 25, "02:00 01.01.2015"));
        LocalDateTime end = LocalDateTime.parse("02:25 01.01.2015",DATE_TIME_FORMATTER);
        assertEquals(end,epic.getEndTime(),"Неправильная обработка времени окончания");
    }

    @Test
    public void updateTimeTest(){
        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

        Epic epicEmpty = new Epic("a", NEW, 1, "b");

        Epic epicFull = new Epic("a", NEW, 1, "b");
        epicFull.addSubtasks(new Subtask("a", NEW, 2, "b", 1, 25, "01:00 01.01.2015"));
        epicFull.addSubtasks(new Subtask("a", NEW, 3, "b", 1, 25, "02:00 01.01.2015"));
        LocalDateTime epicFullStart = LocalDateTime.parse("01:00 01.01.2015",DATE_TIME_FORMATTER);

        assertAll(
                () -> {assertNull(epicEmpty.getDuration(),"Некорректное обновление продолжительности");},
                () -> {assertEquals(Duration.ofMinutes(50L),epicFull.getDuration(),"Некорректное обновление продолжительности");},
                () -> {assertNull(epicEmpty.getStartTime(),"Некорректное обновление времени");},
                () -> {assertEquals(epicFullStart,epicFull.getStartTime(),"Некорректное обновление времени");}
        );
    }
}