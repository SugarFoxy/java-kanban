package servise;

import HttpServer.KVTaskClient;
import com.google.gson.*;
import shared.TaskAdapter;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;

public class HttpTaskManager extends FileBackedTasksManager {
    URI uri;
    KVTaskClient taskClient;
    private Gson gson = new GsonBuilder().create();

    public HttpTaskManager(URI uri) {
        this.uri = uri;
        setKVServer();
        loadData();
    }

    private void setKVServer() {
        try {
            taskClient = new KVTaskClient(uri);
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private void loadData() {
            getTaskFromJson();
            getEpicFromJson();
            getSubtaskFromJson();
            getHistoryFromJson();
    }

    @Override
    public void save() {
            if (!getTasksJson().isEmpty())
                taskClient.put("Tasks", getTasksJson());
            if (!getEpicsJson().isEmpty())
                taskClient.put("Epics", getEpicsJson());
            if (!getSubtasksJson().isEmpty())
                taskClient.put("Subtasks", getSubtasksJson());
            if (!getHistoryJson().isEmpty())
                taskClient.put("History", getHistoryJson());
    }

    private void getHistoryFromJson()  {
        String historyTasks = taskClient.load("History");
        if (!historyTasks.isEmpty()) {
            for (String task : historyTasks.split("\n")) {
                JsonElement element = JsonParser.parseString(task);
                JsonObject jsonObject = element.getAsJsonObject();
                int id = jsonObject.get("identifier").getAsInt();
                if (tasks.containsKey(id)) {
                    history.addHistory(tasks.get(id));
                } else if (epics.containsKey(id)) {
                    history.addHistory(epics.get(id));
                } else
                    history.addHistory(subtasks.get(id));
            }
        }
    }

    private void getTaskFromJson(){
        gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .create();
        String tasksJson = taskClient.load("Tasks");
        if (!tasksJson.isEmpty()) {
            for (String task : tasksJson.split("\n")) {
                Task t = gson.fromJson(task, Task.class);
                tasks.put(t.getIdentifier(), t);
                sorterTask.add(t);
            }
        }
    }

    private void getEpicFromJson(){
        gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new TaskAdapter())
                .create();
        String epicsJson = taskClient.load("Epics");
        if (!epicsJson.isEmpty()) {
            for (String epic : epicsJson.split("\n")) {
                Epic e = gson.fromJson(epic, Epic.class);
                epics.put(e.getIdentifier(), e);
            }
        }
    }

    private void getSubtaskFromJson(){
        gson = new GsonBuilder()
                .registerTypeAdapter(Subtask.class, new TaskAdapter())
                .create();
        String subtasksJson = taskClient.load("Subtasks");
        if (!subtasksJson.isEmpty()) {
            for (String task : subtasksJson.split("\n")) {
                Subtask t = gson.fromJson(task, Subtask.class);
                subtasks.put(t.getIdentifier(), t);
                sorterTask.add(t);
            }
        }
    }

    private String getTasksJson() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .create();
        StringBuilder taskJson = new StringBuilder();
        for (Task task : tasks.values()) {
            taskJson.append(gson.toJson(task)).append(" \n");
        }
        return taskJson.toString();
    }

    private String getEpicsJson() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new TaskAdapter())
                .create();
        StringBuilder taskJson = new StringBuilder();
        for (Epic epic : epics.values()) {
            taskJson.append(gson.toJson(epic)).append(" \n");
        }
        return taskJson.toString();
    }

    private String getSubtasksJson() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Subtask.class, new TaskAdapter())
                .create();
        StringBuilder taskJson = new StringBuilder();
        for (Subtask subtask : subtasks.values()) {
            taskJson.append(gson.toJson(subtask)).append(" \n");
            epics.get(subtask.getIdentifierEpic()).addSubtasks(subtask);
        }
        return taskJson.toString();
    }

    private String getHistoryJson() {
        StringBuilder taskJson = new StringBuilder();

        for (Task task : getHistory()) {
            if (task instanceof Epic) {
                gson = new GsonBuilder()
                        .registerTypeAdapter(Epic.class, new TaskAdapter())
                        .create();
                Epic epic = (Epic) task;
                taskJson.append(gson.toJson(epic)).append(" \n");
            } else if (task instanceof Subtask) {
                gson = new GsonBuilder()
                        .registerTypeAdapter(Subtask.class, new TaskAdapter())
                        .create();
                Subtask subtask = (Subtask) task;
                taskJson.append(gson.toJson(subtask)).append(" \n");
            } else {
                gson = new GsonBuilder()
                        .registerTypeAdapter(Task.class, new TaskAdapter())
                        .create();
                taskJson.append(gson.toJson(task)).append(" \n");
            }
        }
        return taskJson.toString();
    }
}

