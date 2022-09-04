package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String name;
    protected Status status;
    protected int identifier = 0;
    protected String description;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    public Task(String name, String description, long duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime,DATE_TIME_FORMATTER);
    }
    protected Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }
    public Task(String name, Status status, int identifier, String description, long duration, String startTime) {
        this.name = name;
        this.status = status;
        this.identifier = identifier;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime,DATE_TIME_FORMATTER);
    }
    protected Task(String name, Status status, int identifier, String description) {
        this.name = name;
        this.status = status;
        this.identifier = identifier;
        this.description = description;
    }
    public Task(String name, int identifier, String description, long duration, String startTime) {
        this.name = name;
        this.identifier = identifier;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime,DATE_TIME_FORMATTER);
    }
    protected Task(String name, int identifier, String description) {
        this.name = name;
        this.identifier = identifier;
        this.description = description;
    }
    public Duration getDuration() {
        return duration;
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public String getName() {
        return name;
    }
    public LocalDateTime getEndTime(){
       return startTime.plus(duration);
    }
    public void setName(String name) {
        this.name = name;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public int getIdentifier() {
        return identifier;
    }
    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return identifier == task.identifier && name.equals(task.name) && status == task.status && description.equals(task.description);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name, status, identifier, description);
    }
    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", identifier=" + identifier +
                ", description='" + description + '\'' +
                ", duration=" + duration.toMinutes() + "min"+
                ", startTime=" + startTime.format(DATE_TIME_FORMATTER) +
                ", endTime= " + getEndTime().format(DATE_TIME_FORMATTER) +
                '}';
    }
    public String toCSVRow() {
        String[] taskData = new String[9];
        taskData[0] = Integer.toString(getIdentifier());
        taskData[1] = Tasks.TASK.getTaskType();
        taskData[2] = getName();
        taskData[3] = getStatus().getStatus();
        taskData[4] = getDescription();
        taskData[5] = "";
        taskData[6] = String.valueOf(getDuration().toMinutes());
        taskData[7] = getStartTime().format(DATE_TIME_FORMATTER);
        taskData[8] = "\n";
        return String.join(",", taskData);
    }
}
