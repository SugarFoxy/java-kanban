package TasksTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class TaskTest extends TaskAbstactTest {


    @BeforeEach
    public void initData(){
        task1 = new Task("a",Status.NEW,2,"b", 25,"01:00 01.01.2015");
        task1_duplicate = new Task("a",Status.NEW,2,"b", 25,"01:00 01.01.2015");
        task2 = new Task("b",Status.NEW,2,"b", 25,"01:00 01.01.2015");
        task1toCsvString = "2,TASK,a,NEW,b,,25,01:00 01.01.2015,\n";
        task1toString = "Task{name='a', status=NEW, identifier=2, description='b', duration=25min, startTime=01:00 01.01.2015, endTime= 01:25 01.01.2015}";
    }


}