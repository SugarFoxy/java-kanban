package ManagersTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servise.FileBackedTasksManager;
import servise.InMemoryTaskManager;
import servise.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;


class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    File file;

    @BeforeEach
    void init() {
        file = new File("src/test/resources/Tasks.csv");
        taskManager = FileBackedTasksManager.loadFromFile(file);
    }

    @AfterEach
    void clearFile(){
        file.delete();
    }

    @Test
    public void loadEmptyTest(){
        File fileEmpty = new File("src/test/resources/TasksEmpty.csv");
        taskManager = FileBackedTasksManager.loadFromFile(fileEmpty);
        int tasksCount = taskManager.getListTasks().size()+taskManager.getListSubtasks().size()+taskManager.getListEpic().size();
                assertAll(
                ()-> assertEquals(0,tasksCount)
        );
    }

    @Test
    public void loadTest(){
        File fileFilled = new File("src/test/resources/TasksFilled.csv");
        taskManager = FileBackedTasksManager.loadFromFile(fileFilled);
        int tasksCount = taskManager.getListTasks().size()+taskManager.getListSubtasks().size()+taskManager.getListEpic().size();
        int historyCount = taskManager.getHistory().size();
        assertAll(
                ()-> assertEquals(9,tasksCount,"Некорректная загрузка тасок"),
                ()-> assertEquals(5,historyCount,"Некорректная загрузка истории")
        );
    }

    @Test
    public void saveTest(){
        File fileSaveTest = new File("src/test/resources/TasksSaveTest.csv");
        fileSaveTest.delete();
        fileSaveTest = new File("src/test/resources/TasksSaveTest.csv");
        FileBackedTasksManager taskManager = FileBackedTasksManager.loadFromFile(fileSaveTest);
        Task task1 = new Task("Ужаснуться", "Поседеть от ТЗ третьего спринта", 25, "01:00 01.01.2022");
        Task task2 = new Task("Расшифровка", "Расшифровать задуманное задание", 25, "02:00 01.01.2022");
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        Epic epic1 = new Epic("Сделать Финальный проект", "Переписать прогу в 4ый раз");
        taskManager.addNewEpic(epic1);
        Subtask subtask3_1 = new Subtask("Удалить", "Удалить уже готовый консольный шедевр", epic1.getIdentifier(), 25, "03:00 01.01.2022");
        Subtask subtask3_2 = new Subtask("Осознать суть", "Понять что она должна передавать объект", epic1.getIdentifier(), 25, "04:00 01.01.2022");
        Subtask subtask3_3 = new Subtask("Превозмочь", "Проломить все стены своей головой", epic1.getIdentifier(), 25, "05:00 01.01.2022");
        Subtask subtask3_4 = new Subtask("Закончить", "Написать очередной шедевр", epic1.getIdentifier(), 25, "06:00 01.01.2022");
        Subtask subtask3_5 = new Subtask("Ревью", "Сдать на ревью", epic1.getIdentifier(), 25, "07:00 01.01.2022");
        taskManager.addNewSubtask(subtask3_1);
        taskManager.addNewSubtask(subtask3_2);
        taskManager.addNewSubtask(subtask3_3);
        taskManager.addNewSubtask(subtask3_4);
        taskManager.addNewSubtask(subtask3_5);
        Epic epic2 = new Epic("Благодарность", "Поблагодарить Сергея за прекрасное ревью");
        taskManager.addNewEpic(epic2);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getEpic(9);
        taskManager.getSubtask(4);
        File fileSample = new File("src/test/resources/TasksSaveSample.csv");
        try {
            File fileEmpty = new File("src/test/resources/TasksEmpty.csv");
            String fromFileEmpty = Files.readString(fileEmpty.toPath());
            String fromFile =Files.readString(fileSaveTest.toPath());
            String[] line = fromFile.split("\n");
            assertEquals("",fromFileEmpty, "что там есть");
            assertEquals(Files.readString(fileSample.toPath()), fromFile,"некорректное сохранение");
            assertEquals("9,EPIC,Благодарность,NEW,Поблагодарить Сергея за прекрасное ревью,,0,0,",line[9],"не верное сохранение эпика без подзадач");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}