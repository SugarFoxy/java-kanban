package HttpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.TaskAdapter;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tasks.Status.IN_PROCESS;
import static tasks.Status.NEW;

class HttpTaskServerTest {
    KVServer kvServer;
    Task task;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    HttpTaskServer httpTaskServer;
    @BeforeEach
     void init(){
        try {
            kvServer = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.loadServer();
        epic = new Epic("Test addNewTask", NEW, 5, "Test addNewTask description");
        subtask2 = new Subtask("a", NEW, 2, "b", 5, 25, "01:00 01.01.2015");
        subtask3 = new Subtask("a", IN_PROCESS, 3, "b", 5, 25, "03:00 01.01.2015");
        subtask1 = new Subtask("a", NEW, 4, "b", 5, 25, "02:00 01.01.2015");
        task = new Task("Test addNewTask", NEW, 1, "Test addNewTask description", 25, "00:00 01.01.1001");
    }

    @Test
    void testTaskEndpoints() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .create();
        String json = gson.toJson(task);
        String json1 = gson.toJson(new Task("Test addNewTask", NEW, 2, "Test addNewTask description", 25, "01:00 01.01.1001"));
        final HttpRequest.BodyPublisher bodyReq = HttpRequest.BodyPublishers.ofString(json);
        final HttpRequest.BodyPublisher bodyReq1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).POST(bodyReq).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpRequest request1_1 = HttpRequest.newBuilder().uri(url).POST(bodyReq1).build();
        HttpResponse<String> response1_1 = client.send(request1_1, HttpResponse.BodyHandlers.ofString());
        String  resp = response1.body() + response1_1.body();

        HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        String body2 = response2.body();

        URI url1 = URI.create("http://localhost:8080/tasks/task/1");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        String body3 = response3.body();

        HttpRequest request4 = HttpRequest.newBuilder().uri(url1).DELETE().build();
        client.send(request4, HttpResponse.BodyHandlers.ofString());

        HttpRequest request4_1 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response4_1 = client.send(request4_1, HttpResponse.BodyHandlers.ofString());
        String body4 = response4_1.body();

        HttpRequest request5 = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request5, HttpResponse.BodyHandlers.ofString());

        HttpRequest request5_1 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response5_1 = client.send(request5_1, HttpResponse.BodyHandlers.ofString());
        String body5 = response5_1.body();
        assertAll(
                () -> assertEquals("",body , "Элементы еще не добавлены"),
                () -> assertEquals("",resp , "В POST не должно быть тела"),
                () -> assertEquals("{\"identifier\":1,\"name\":\"Test addNewTask\",\"status\":\"NEW\",\"description\":\"Test addNewTask description\",\"duration\":25,\"startTime\":\"00:00 01.01.1001\"}, " +
                        "{\"identifier\":2,\"name\":\"Test addNewTask\",\"status\":\"NEW\",\"description\":\"Test addNewTask description\",\"duration\":25,\"startTime\":\"01:00 01.01.1001\"}, ",body2 , "Было добавлено две задачи"),
                () -> assertEquals("{\"identifier\":1,\"name\":\"Test addNewTask\",\"status\":\"NEW\",\"description\":\"Test addNewTask description\",\"duration\":25,\"startTime\":\"00:00 01.01.1001\"}",body3 , "Вызвана конкретная задача"),
                () -> assertEquals("{\"identifier\":2,\"name\":\"Test addNewTask\",\"status\":\"NEW\",\"description\":\"Test addNewTask description\",\"duration\":25,\"startTime\":\"01:00 01.01.1001\"}, ",body4 , "Одна задача была удалена, должна остаться одна"),
                () -> assertEquals("",body5 , "Все задачи удалены. Тело пустое")
        );
    }

    @Test
    void testSubtaskEndpoints() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic/");
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Subtask.class, new TaskAdapter())
                .create();
        String json = gson.toJson(subtask1);
        String json1 = gson.toJson(subtask2);
       Gson gsonEpic = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new TaskAdapter())
                .create();
        String jsonEpic = gsonEpic.toJson(epic);new Epic("Test addNewTask", NEW, 5, "Test addNewTask description");

        final HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        final HttpRequest.BodyPublisher bodyReq = HttpRequest.BodyPublishers.ofString(json);
        final HttpRequest.BodyPublisher bodyReq1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).POST(bodyReq).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpRequest request1_1 = HttpRequest.newBuilder().uri(url).POST(bodyReq1).build();
        HttpResponse<String> response1_1 = client.send(request1_1, HttpResponse.BodyHandlers.ofString());
        String  resp = response1.body() + response1_1.body();

        HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        String body2 = response2.body();

        URI url1 = URI.create("http://localhost:8080/tasks/subtask/4");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        String body3 = response3.body();

        URI urlSubtaskFromEpic = URI.create("http://localhost:8080/tasks/subtask/epic/5");
        HttpRequest requestSubtaskFromEpic = HttpRequest.newBuilder().uri(urlSubtaskFromEpic).GET().build();
        HttpResponse<String> responseSubtaskFromEpic = client.send(requestSubtaskFromEpic, HttpResponse.BodyHandlers.ofString());
        String bodySubtaskFromEpic = responseSubtaskFromEpic.body();

        HttpRequest request4 = HttpRequest.newBuilder().uri(url1).DELETE().build();
        client.send(request4, HttpResponse.BodyHandlers.ofString());

        HttpRequest request4_1 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response4_1 = client.send(request4_1, HttpResponse.BodyHandlers.ofString());
        String body4 = response4_1.body();

        HttpRequest request5 = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request5, HttpResponse.BodyHandlers.ofString());

        HttpRequest request5_1 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response5_1 = client.send(request5_1, HttpResponse.BodyHandlers.ofString());
        String body5 = response5_1.body();
        assertAll(
                () -> assertEquals("",body , "Элементы еще не добавлены"),
                () -> assertEquals("",resp , "В POST не должно быть тела"),
                () -> assertEquals("{\"identifier\":2,\"name\":\"a\",\"status\":\"NEW\",\"description\":\"b\",\"duration\":25,\"startTime\":\"01:00 01.01.2015\",\"epicId\":5}, {\"identifier\":4,\"name\":\"a\",\"status\":\"NEW\",\"description\":\"b\",\"duration\":25,\"startTime\":\"02:00 01.01.2015\",\"epicId\":5}, ",body2 , "Было добавлено две подзадачи"),
                () -> assertEquals("{\"identifier\":4,\"name\":\"a\",\"status\":\"NEW\",\"description\":\"b\",\"duration\":25,\"startTime\":\"02:00 01.01.2015\",\"epicId\":5}",body3 , "Вызвана конкретная подзадача"),
                () -> assertEquals("{\"identifier\":4,\"name\":\"a\",\"status\":\"NEW\",\"description\":\"b\",\"duration\":25,\"startTime\":\"02:00 01.01.2015\",\"epicId\":5}, {\"identifier\":2,\"name\":\"a\",\"status\":\"NEW\",\"description\":\"b\",\"duration\":25,\"startTime\":\"01:00 01.01.2015\",\"epicId\":5}, ",bodySubtaskFromEpic , "Вызваны 2 подзадачи конкретного Эпика"),
                () -> assertEquals("{\"identifier\":2,\"name\":\"a\",\"status\":\"NEW\",\"description\":\"b\",\"duration\":25,\"startTime\":\"01:00 01.01.2015\",\"epicId\":5}, ",body4 , "Одна подзадача была удалена, должна остаться одна"),
                () -> assertEquals("",body5 , "Все подзадачи удалены. Тело пустое")
        );
    }

    @Test
    void testEpicEndpoints() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic/");
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Subtask.class, new TaskAdapter())
                .create();
        String json = gson.toJson(subtask1);
        String json1 = gson.toJson(subtask2);
        Gson gsonEpic = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new TaskAdapter())
                .create();
        String jsonEpic = gsonEpic.toJson(epic);
        String jsonEpic1 = gsonEpic.toJson(new Epic("Test addNewTask", NEW, 10, "Test addNewTask description"));

        final HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        HttpResponse<String> responseEpic = client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        final HttpRequest.BodyPublisher bodyEpic1 = HttpRequest.BodyPublishers.ofString(jsonEpic1);
        HttpRequest requestEpic1 = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic1).build();
        HttpResponse<String> responseEpic1 = client.send(requestEpic1, HttpResponse.BodyHandlers.ofString());
        String  resp = responseEpic.body() + responseEpic1.body();

        final HttpRequest.BodyPublisher bodyReq = HttpRequest.BodyPublishers.ofString(json);
        final HttpRequest.BodyPublisher bodyReq1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).POST(bodyReq).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpRequest request1_1 = HttpRequest.newBuilder().uri(url).POST(bodyReq1).build();
        client.send(request1_1, HttpResponse.BodyHandlers.ofString());


        HttpRequest request2 = HttpRequest.newBuilder().uri(urlEpic).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        String body2 = response2.body();

        URI url1 = URI.create("http://localhost:8080/tasks/epic/5");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        String body3 = response3.body();

        URI urlSubtaskFromEpic = URI.create("http://localhost:8080/tasks/subtask/epic/5");
        HttpRequest requestSubtaskFromEpic = HttpRequest.newBuilder().uri(urlSubtaskFromEpic).GET().build();
        HttpResponse<String> responseSubtaskFromEpic = client.send(requestSubtaskFromEpic, HttpResponse.BodyHandlers.ofString());
        String bodySubtaskFromEpic = responseSubtaskFromEpic.body();

        HttpRequest request4 = HttpRequest.newBuilder().uri(url1).DELETE().build();
        client.send(request4, HttpResponse.BodyHandlers.ofString());

        HttpRequest request4_1 = HttpRequest.newBuilder().uri(urlEpic).GET().build();
        HttpResponse<String> response4_1 = client.send(request4_1, HttpResponse.BodyHandlers.ofString());
        String body4 = response4_1.body();

        HttpRequest request5 = HttpRequest.newBuilder().uri(urlEpic).DELETE().build();
        client.send(request5, HttpResponse.BodyHandlers.ofString());

        HttpRequest request5_1 = HttpRequest.newBuilder().uri(urlEpic).GET().build();
        HttpResponse<String> response5_1 = client.send(request5_1, HttpResponse.BodyHandlers.ofString());
        String body5 = response5_1.body();
        assertAll(
                () -> assertEquals("",body , "Элементы еще не добавлены"),
                () -> assertEquals("",resp , "В POST не должно быть тела"),
                () -> assertEquals("{\"identifier\":5,\"name\":\"Test addNewTask\",\"status\":\"NEW\",\"description\":\"Test addNewTask description\",\"duration\":50,\"startTime\":\"01:00 01.01.2015\",\"amount subtasks\":2}, {\"identifier\":10,\"name\":\"Test addNewTask\",\"status\":\"NEW\",\"description\":\"Test addNewTask description\",\"duration\":0,\"startTime\":\"null\",\"amount subtasks\":0}, ",body2 , "Было добавлено два Эпика"),
                () -> assertEquals("{\"identifier\":5,\"name\":\"Test addNewTask\",\"status\":\"NEW\",\"description\":\"Test addNewTask description\",\"duration\":50,\"startTime\":\"01:00 01.01.2015\",\"amount subtasks\":2}",body3 , "Вызван конкретный Эпик"),
                () -> assertEquals("{\"identifier\":4,\"name\":\"a\",\"status\":\"NEW\",\"description\":\"b\",\"duration\":25,\"startTime\":\"02:00 01.01.2015\",\"epicId\":5}, {\"identifier\":2,\"name\":\"a\",\"status\":\"NEW\",\"description\":\"b\",\"duration\":25,\"startTime\":\"01:00 01.01.2015\",\"epicId\":5}, ",bodySubtaskFromEpic , "Вызваны 2 подзадачи конкретного Эпика"),
                () -> assertEquals("{\"identifier\":10,\"name\":\"Test addNewTask\",\"status\":\"NEW\",\"description\":\"Test addNewTask description\",\"duration\":0,\"startTime\":\"null\",\"amount subtasks\":0}, ",body4 , "Один эпик был удален, должен остаться один"),
                () -> assertEquals("",body5 , "Все эпики удалены. Тело пустое")
        );
    }

    @Test
    void testHistoryEndpoints() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history/");
        URI urlTask = URI.create("http://localhost:8080/tasks/task/");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic/");
        URI urlSubtask = URI.create("http://localhost:8080/tasks/subtask/");

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        Gson gsonSubtask = new GsonBuilder()
                .registerTypeAdapter(Subtask.class, new TaskAdapter())
                .create();
        String jsonSubtask = gsonSubtask.toJson(subtask1);
        Gson gsonEpic = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new TaskAdapter())
                .create();
        String jsonEpic = gsonEpic.toJson(epic);
        Gson gsonTask = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .create();
        String jsonTask = gsonTask.toJson(task);

        final HttpRequest.BodyPublisher bodyTask = HttpRequest.BodyPublishers.ofString(jsonTask);
        HttpRequest requestTask = HttpRequest.newBuilder().uri(urlTask).POST(bodyTask).build();
        client.send(requestTask, HttpResponse.BodyHandlers.ofString());
        final HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        final HttpRequest.BodyPublisher bodySubtask = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest requestSubtask = HttpRequest.newBuilder().uri(urlSubtask).POST(bodySubtask).build();
        client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());

        URI urlGetTask = URI.create("http://localhost:8080/tasks/task/1");
        HttpRequest requestGetTask = HttpRequest.newBuilder().uri(urlGetTask).GET().build();
        client.send(requestGetTask, HttpResponse.BodyHandlers.ofString());

        URI urlGetEpic = URI.create("http://localhost:8080/tasks/epic/5");
        HttpRequest requestGetEpic = HttpRequest.newBuilder().uri(urlGetEpic).GET().build();
        client.send(requestGetEpic, HttpResponse.BodyHandlers.ofString());

        URI urlGetSubtask = URI.create("http://localhost:8080/tasks/subtask/4");
        HttpRequest requestGetSubtask = HttpRequest.newBuilder().uri(urlGetSubtask).GET().build();
        client.send(requestGetSubtask, HttpResponse.BodyHandlers.ofString());

        HttpRequest request1 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        String body1 = response1.body();

        assertAll(
                () -> assertEquals("",body , "Элементы еще не добавлены"),
                () -> assertEquals("{\"identifier\":1,\"name\":\"Test addNewTask\",\"status\":\"NEW\",\"description\":\"Test addNewTask description\",\"duration\":25,\"startTime\":\"00:00 01.01.1001\"}, " +
                        "{\"identifier\":5,\"name\":\"Test addNewTask\",\"status\":\"NEW\",\"description\":\"Test addNewTask description\",\"duration\":25,\"startTime\":\"02:00 01.01.2015\",\"amount subtasks\":1}, " +
                        "{\"identifier\":4,\"name\":\"a\",\"status\":\"NEW\",\"description\":\"b\",\"duration\":25,\"startTime\":\"02:00 01.01.2015\",\"epicId\":5}, ",body1,"В теле должны быть: таск,епик и сабтаск")
        );
    }

    @Test
    void testSorterEndpoints() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        URI urlTask = URI.create("http://localhost:8080/tasks/task/");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic/");
        URI urlSubtask = URI.create("http://localhost:8080/tasks/subtask/");

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        Gson gsonSubtask = new GsonBuilder()
                .registerTypeAdapter(Subtask.class, new TaskAdapter())
                .create();
        String jsonSubtask = gsonSubtask.toJson(subtask1);
        Gson gsonEpic = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new TaskAdapter())
                .create();
        String jsonEpic = gsonEpic.toJson(epic);
        Gson gsonTask = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .create();
        String jsonTask = gsonTask.toJson(task);

        final HttpRequest.BodyPublisher bodyTask = HttpRequest.BodyPublishers.ofString(jsonTask);
        HttpRequest requestTask = HttpRequest.newBuilder().uri(urlTask).POST(bodyTask).build();
        client.send(requestTask, HttpResponse.BodyHandlers.ofString());
        final HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        final HttpRequest.BodyPublisher bodySubtask = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest requestSubtask = HttpRequest.newBuilder().uri(urlSubtask).POST(bodySubtask).build();
        client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());

        HttpRequest request1 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        String body1 = response1.body();

        assertAll(
                () -> assertEquals("",body , "Элементы еще не добавлены"),
                () -> assertEquals("{\"identifier\":1,\"name\":\"Test addNewTask\",\"status\":\"NEW\",\"description\":\"Test addNewTask description\",\"duration\":25,\"startTime\":\"00:00 01.01.1001\"}, " +
                        "{\"identifier\":4,\"name\":\"a\",\"status\":\"NEW\",\"description\":\"b\",\"duration\":25,\"startTime\":\"02:00 01.01.2015\",\"epicId\":5}, ",body1,"В теле должны быть: таск,епик и сабтаск")
        );
    }

    @AfterEach
    void after(){
        kvServer.stop();
        httpTaskServer.closeServer();
    }

}