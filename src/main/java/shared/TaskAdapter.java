package shared;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class TaskAdapter extends TypeAdapter<Task> {
    private final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    @Override
    public void write(JsonWriter jsonWriter, Task task) throws IOException {
        if (task instanceof Epic) {
            Epic epic = (Epic) task;
            jsonWriter.beginObject()
                    .name("identifier")
                    .value(epic.getIdentifier())
                    .name("name")
                    .value(epic.getName())
                    .name("status")
                    .value(epic.getStatus().getStatus())
                    .name("description")
                    .value(epic.getDescription());
            if (epic.getDuration() == null) {
                jsonWriter.name("duration")
                        .value(0);
            } else {
                jsonWriter.name("duration")
                        .value(epic.getDuration().toMinutes());
            }
            if (epic.getStartTime() == null) {
                jsonWriter.name("startTime")
                        .value("null");
            } else {
                jsonWriter.name("startTime")
                        .value(epic.getStartTime().format(formatterWriter));
            }
            jsonWriter.name("amount subtasks")
                    .value(epic.getSubtasks().size())
                    .endObject()
                    .close();
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            jsonWriter.beginObject()
                    .name("identifier")
                    .value(subtask.getIdentifier())
                    .name("name")
                    .value(subtask.getName())
                    .name("status")
                    .value(subtask.getStatus().getStatus())
                    .name("description")
                    .value(subtask.getDescription())
                    .name("duration")
                    .value(subtask.getDuration().toMinutes())
                    .name("startTime")
                    .value(subtask.getStartTime().format(formatterWriter))
                    .name("epicId")
                    .value(subtask.getIdentifierEpic())
                    .endObject()
                    .close();
        } else {
            jsonWriter.beginObject()
                    .name("identifier")
                    .value(task.getIdentifier())
                    .name("name")
                    .value(task.getName())
                    .name("status")
                    .value(task.getStatus().getStatus())
                    .name("description")
                    .value(task.getDescription())
                    .name("duration")
                    .value(task.getDuration().toMinutes())
                    .name("startTime")
                    .value(task.getStartTime().format(formatterWriter))
                    .endObject()
                    .close();
        }
    }

    @Override
    public Task read(final JsonReader jsonReader) throws IOException {
        String name = "";
        String description = "";
        int id = 0;
        String status = "";
        long duration = 0;
        String startTime = "";
        int epicId = -1;
        int amountSubtasks = -1;
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String parameter = jsonReader.nextName();
            switch (parameter) {
                case "name":
                    name = jsonReader.nextString();
                    break;
                case "description":
                    description = jsonReader.nextString();
                    break;
                case "identifier":
                    id = jsonReader.nextInt();
                    break;
                case "status":
                    status = jsonReader.nextString();
                    break;
                case "duration":
                    duration = jsonReader.nextLong();
                    break;
                case "startTime":
                    startTime = jsonReader.nextString();
                    break;
                case "amount subtasks":
                    amountSubtasks = jsonReader.nextInt();
                    break;
                case "epicId":
                    epicId = jsonReader.nextInt();
                    break;
                default:
                    jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        if (epicId >= 0) {
            if (id == 0) {
                return new Subtask(name, description, epicId, duration, startTime);
            } else {
                return new Subtask(name, Status.valueOf(status), id, description, epicId, duration, startTime);
            }
        } else if (amountSubtasks >= 0) {
            if (id == 0) {
                return new Epic(name, description);
            } else {
                return new Epic(name, id, description);
            }
        } else {
            if (id == 0) {
                return new Task(name, description, duration, startTime);
            } else {
                return new Task(name, Status.valueOf(status), id, description, duration, startTime);
            }
        }
    }
}
