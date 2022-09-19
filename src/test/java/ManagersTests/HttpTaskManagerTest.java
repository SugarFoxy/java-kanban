package ManagersTests;

import HttpServer.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servise.FileBackedTasksManager;
import servise.HttpTaskManager;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.NEW;

class HttpTaskManagerTest  extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;

    @BeforeEach
    void init() {
        try {
            kvServer = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        kvServer.start();
        URI uri = URI.create("http://localhost:7789");
            taskManager = new HttpTaskManager(uri);
            //taskManager.addNewTask(new Task("Test addNewTask", NEW, 1, "Test addNewTask description", 25, "00:00 01.01.1001"));

    }

    @AfterEach
    void clear(){
        kvServer.stop();
    }
}