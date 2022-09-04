package TasksTests;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.NEW;

public class SubtaskTest extends TaskAbstactTest{

    @BeforeEach
    void initData(){
        task1 = new Subtask("a",Status.NEW,2,"b",1, 25,"01:00 01.01.2015");
        task1_duplicate = new Subtask("a",Status.NEW,2,"b",1, 25,"01:00 01.01.2015");
        task2 = new Subtask("b",Status.NEW,2,"b",1, 25,"01:00 01.01.2015");
        task1toCsvString = "2,SUBTASK,a,NEW,b,1,25,01:00 01.01.2015,\n";
        task1toString = "Subtask{identifierEpic=1, name='a', status=NEW, identifier=2, description='b', duration=25min, startTime=01:00 01.01.2015, endTime= 01:25 01.01.2015}";
    }

    @Test
    public void getEndTimeTest(){
       assertAll(
               () -> assertEquals(LocalDateTime.parse("01:25 01.01.2015", DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")),task1.getEndTime(),"по времени какая-то ху*ня(не забыть стереть)"),
               () -> assertNotEquals(LocalDateTime.parse("01:24 01.01.2015", DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")),task1.getEndTime(),"по времени какая-то ху*ня(не забыть стереть)")
       );
    }




}
