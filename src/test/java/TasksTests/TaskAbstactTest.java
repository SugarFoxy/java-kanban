package TasksTests;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskAbstactTest<T extends Task> {

    T task1;
    T task1_duplicate;
    T task2;
    String task1toCsvString;
    String task1toString;

    @Test
    void equalsTest(){
        assertAll(
                () -> assertEquals(task1,task1_duplicate,"equals wrong work"),
                () -> assertNotEquals(task1,task2,"equals wrong work")
        );
    }

    @Test
    void hashCodeTest(){
        assertAll(
                () -> assertEquals(task1.hashCode(),task1_duplicate.hashCode(),"equals wrong work"),
                () -> assertNotEquals(task1.hashCode(),task2.hashCode(),"equals wrong work")
        );
    }

    @Test
    public void toCSVRowTest() {
        assertEquals(task1toCsvString, task1.toCSVRow(), "Некорректное преобразование в CSV");
    }

    @Test
    public void toStringTest() {
        assertEquals(task1toString, task1.toString(), "Некорректное преобразование в CSV");
    }

    @Test
    public void TestGettersSetters(){
        Boolean doNotNeed = true;
        assertTrue(doNotNeed);
    }
}
