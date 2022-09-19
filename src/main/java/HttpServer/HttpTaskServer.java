package HttpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import servise.Managers;
import servise.interfase.TaskManager;
import shared.TaskAdapter;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private final TaskManager manager = Managers.getDefaultHttp(URI.create("http://localhost:7789"));
    HttpServer httpServer;

    public void loadServer() {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new MyHandler());
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void closeServer() {
        httpServer.stop(0);
    }

    class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {

            try {
                String response;

                String method = httpExchange.getRequestMethod();
                String path = httpExchange.getRequestURI().getPath();
                String[] element = path.split("/");
                if (element.length >= 3 && element[2].equals("task")) {
                    response = taskOperations(method, httpExchange, element);
                } else if (element.length >= 3 && element[2].equals("epic")) {
                    response = epicOperations(method, httpExchange, element);
                } else if (element.length >= 3 && element[2].equals("subtask")) {
                    response = subtaskOperations(method, httpExchange, element);
                } else if (element.length >= 3 && element[2].equals("history")) {
                    response = historyOperations(method, httpExchange);
                } else if (element.length == 2 && element[1].equals("tasks")) {
                    response = allTaskOperations(method, httpExchange);
                } else {
                    throw new RuntimeException("Неверный путь");
                }
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } catch (Exception e) {
                try {
                    httpExchange.sendResponseHeaders(400, 0);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                httpExchange.close();
            }

        }

        private String allTaskOperations(String method, HttpExchange httpExchange) throws IOException {
            StringBuilder response = new StringBuilder();
            try {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Task.class, new TaskAdapter())
                        .create();
                if ("GET".equals(method)) {
                    for (Task task : manager.getPrioritizedTasks()) {
                        if (task instanceof Subtask) {
                            gson = new GsonBuilder().registerTypeAdapter(Subtask.class, new TaskAdapter())
                                    .create();
                            Subtask subtask = (Subtask) task;
                            response.append(gson.toJson(subtask)).append(", ");
                        } else {
                            response.append(gson.toJson(task)).append(", ");
                        }
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    throw new RuntimeException("Такой эндпоинт отсутствует");
                }
            } catch (Exception e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
            return response.toString();
        }

        private String historyOperations(String method, HttpExchange httpExchange) throws IOException {
            StringBuilder response = new StringBuilder();
            try {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Task.class, new TaskAdapter())
                        .create();
                if ("GET".equals(method)) {
                    for (Task task : manager.getHistory()) {
                        if (task instanceof Epic) {
                            gson = new GsonBuilder().registerTypeAdapter(Epic.class, new TaskAdapter())
                                    .create();
                            Epic epic = (Epic) task;
                            response.append(gson.toJson(epic)).append(", ");
                        } else if (task instanceof Subtask) {
                            gson = new GsonBuilder().registerTypeAdapter(Subtask.class, new TaskAdapter())
                                    .create();
                            Subtask subtask = (Subtask) task;
                            response.append(gson.toJson(subtask)).append(", ");
                        } else {
                            response.append(gson.toJson(task)).append(", ");
                        }
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    throw new RuntimeException("Такой эндпоинт у history отсутствует");
                }
            } catch (Exception e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
            return response.toString();
        }

        private String subtaskOperations(String method, HttpExchange httpExchange, String[] element) throws IOException {
            StringBuilder response = new StringBuilder();
            Gson gson;
            int id;
            try {
                switch (method) {
                    case "POST":
                        InputStream inputStream = httpExchange.getRequestBody();
                        gson = new GsonBuilder()
                                .registerTypeAdapter(Subtask.class, new TaskAdapter())
                                .create();
                        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        Subtask newSubtask = gson.fromJson(body, Subtask.class);
                        id = newSubtask.getIdentifier();
                        List<Subtask> subtaskList = manager.getListSubtasks();
                        boolean containsSubtask = false;
                        if (subtaskList.size() == 0 || id == 0) {
                            manager.addNewSubtask(newSubtask);
                        } else {
                            for (Subtask subtask : subtaskList) {
                                if (subtask.getIdentifier() == id) {
                                    containsSubtask = true;
                                    break;
                                }
                            }
                        }
                        if (containsSubtask) {
                            manager.updateSubtask(newSubtask);
                        } else {
                            manager.addNewSubtask(newSubtask);
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        break;
                    case "GET":
                        gson = new GsonBuilder()
                                .registerTypeAdapter(Subtask.class, new TaskAdapter())
                                .create();
                        if (element.length == 3) {
                            for (Subtask subtask : manager.getListSubtasks()) {
                                response.append(gson.toJson(subtask)).append(", ");
                            }
                            httpExchange.sendResponseHeaders(200, 0);
                        } else if (element.length == 4) {
                            id = Integer.parseInt(element[3]);
                            response = new StringBuilder(gson.toJson(manager.getSubtask(id)));
                            httpExchange.sendResponseHeaders(200, 0);
                        } else if (element.length == 5 && element[3].equals("epic")) {
                            id = Integer.parseInt(element[4]);
                            List<Subtask> subtasks = manager.getListSubtaskFromEpic(id);
                            if (subtasks.size() != 0) {
                                for (Subtask subtask : subtasks) {
                                    response.append(gson.toJson(subtask)).append(", ");
                                }
                                httpExchange.sendResponseHeaders(200, 0);
                            }
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    case "DELETE":
                        if (element.length == 3) {
                            manager.deleteAllSubtasks();
                        } else if (element.length == 4) {
                            manager.deleteSubtaskByIdentifier(Integer.parseInt(element[3]));
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        break;
                    default:
                        throw new RuntimeException("неверный эндпоит");
                }
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
            return response.toString();
        }

        private String epicOperations(String method, HttpExchange httpExchange, String[] element) throws IOException {
            StringBuilder response = new StringBuilder();
            Gson gson;
            int id;
            try {
                switch (method) {
                    case "POST":

                        InputStream inputStream = httpExchange.getRequestBody();
                        gson = new GsonBuilder()
                                .registerTypeAdapter(Epic.class, new TaskAdapter())
                                .create();
                        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        Epic newEpic = gson.fromJson(body, Epic.class);
                        id = newEpic.getIdentifier();
                        List<Epic> epicList = manager.getListEpic();
                        boolean containsEpic = false;
                        if (epicList.size() == 0 || id == 0) {
                            manager.addNewEpic(newEpic);
                        } else {
                            for (Epic epic : epicList) {
                                if (epic.getIdentifier() == id) {
                                    containsEpic = true;
                                    break;
                                }
                            }
                        }
                        if (containsEpic) {
                            manager.updateEpic(newEpic);
                        } else {
                            manager.addNewEpic(newEpic);
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        break;
                    case "GET":
                        gson = new GsonBuilder()
                                .registerTypeAdapter(Epic.class, new TaskAdapter())
                                .create();
                        if (element.length == 3) {
                            for (Epic epic : manager.getListEpic()) {
                                response.append(gson.toJson(epic)).append(", ");
                            }
                            httpExchange.sendResponseHeaders(200, 0);
                        } else if (element.length == 4) {
                            id = Integer.parseInt(element[3]);
                            response = new StringBuilder(gson.toJson(manager.getEpic(id)));
                            httpExchange.sendResponseHeaders(200, 0);
                        }

                        break;
                    case "DELETE":
                        if (element.length == 3) {
                            manager.deleteAllEpics();
                        } else if (element.length == 4) {
                            manager.deleteEpicByIdentifier(Integer.parseInt(element[3]));
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    default:
                        throw new RuntimeException("неверный эндпоит");
                }
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
            return response.toString();
        }

        private String taskOperations(String method, HttpExchange httpExchange, String[] element) throws IOException {
            StringBuilder response = new StringBuilder();
            Gson gson;
            int id;
            try {
                switch (method) {
                    case "POST":
                        InputStream inputStream = httpExchange.getRequestBody();
                        gson = new GsonBuilder()
                                .registerTypeAdapter(Task.class, new TaskAdapter())
                                .create();
                        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        Task newTask = gson.fromJson(body, Task.class);
                        id = newTask.getIdentifier();
                        List<Task> taskList = manager.getListTasks();
                        boolean containsTask = false;
                        if (taskList.size() == 0 || id == 0) {
                            manager.addNewTask(newTask);
                        } else {
                            for (Task task : taskList) {
                                if (task.getIdentifier() == id) {
                                    containsTask = true;
                                    break;
                                }
                            }
                        }
                        if (containsTask) {
                            manager.updateTask(newTask);
                        } else {
                            manager.addNewTask(newTask);
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        break;
                    case "GET":
                        gson = new GsonBuilder()
                                .registerTypeAdapter(Task.class, new TaskAdapter())
                                .create();
                        if (element.length == 3) {
                            for (Task task : manager.getListTasks()) {
                                response.append(gson.toJson(task)).append(", ");
                            }
                            httpExchange.sendResponseHeaders(200, 0);
                        } else if (element.length == 4) {
                            id = Integer.parseInt(element[3]);
                            response = new StringBuilder(gson.toJson(manager.getTask(id)));
                            httpExchange.sendResponseHeaders(200, 0);
                        }

                        break;
                    case "DELETE":
                        if (element.length == 3) {
                            manager.deleteAllTasks();
                        } else if (element.length == 4) {
                            manager.deleteTaskByIdentifier(Integer.parseInt(element[3]));
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    default:
                        throw new RuntimeException("неверный эндпоит");
                }
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
            return response.toString();
        }
    }
}

