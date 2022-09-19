package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;


public class Epic extends Task {
    private ArrayList<Subtask> subtasks = new ArrayList<>();
    private LocalDateTime endTime;
    public Epic(String name, String description) {
        super(name, description);
    }
    public Epic(String name, int identifier, String description) {
        super(name, identifier, description);
    }
    public Epic(String name, Status status, int identifier, String description) {
        super(name, status, identifier, description);
    }
    private void updateTime() {
        if (subtasks.size() != 0) {
            long sumMinutes = 0;
            LocalDateTime first = subtasks.get(0).getStartTime();
            for (Subtask subtask : subtasks) {
                sumMinutes += subtask.getDuration().toMinutes();
                if (subtask.getStartTime().isBefore(first) ) {
                    first = subtask.getStartTime();
                }
            }
            duration = Duration.ofMinutes(sumMinutes);
            startTime = first;
        } else {
            duration = null;
            startTime = null;
        }
        updateEndTime();
    }
    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }
    public void addSubtasks(Subtask subtask) {
        if (!subtasks.contains(subtask)) {
            subtasks.add(subtask);
            updateStatus();
            updateTime();
        }
    }
    public void deleteSubtasks() {
        subtasks.clear();
        updateStatus();
        updateTime();
    }
    private void updateStatus() {
        int sumSubtaskNew = 0;
        int sumSubtaskDone = 0;
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus().equals(Status.NEW)) {
                ++sumSubtaskNew;
            } else if (subtask.getStatus().equals(Status.DONE)) {
                ++sumSubtaskDone;
            }
        }
        if (subtasks.size() == 0 || subtasks.size() == sumSubtaskNew) {
            setStatus(Status.NEW);
        } else if (subtasks.size() == sumSubtaskDone) {
            setStatus(Status.DONE);
        } else {
            setStatus(Status.IN_PROCESS);
        }
    }

    @Override
    public Status getStatus(){
        updateStatus();
        return super.getStatus();
    }

    public void deleteSubtask(Subtask subtask) {
        if (subtasks.contains(subtask)) {
            subtasks.remove(subtask);
            updateStatus();
            updateTime();
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return subtasks.equals(epic.subtasks);
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }
    @Override
    public LocalDateTime getEndTime(){
        if(endTime == null){
            updateEndTime();
        }
        return endTime;
    }

    private void updateEndTime(){
        if(startTime != null && subtasks.size()!=0){
            LocalDateTime end =subtasks.get(0).getEndTime();
            for (Subtask subtask:subtasks){
                if (subtask.getEndTime().isAfter(end)){
                    end = subtask.getEndTime();
                }
            }
            endTime=end;
        }else {
            endTime=null;
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "amount subtasks=" + subtasks.size() +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", identifier=" + identifier +
                ", description='" + description + '\'' +
                ", duration= " + (duration!=null ? String.valueOf(duration.toMinutes()) : "null") + "min" +
                ", startTime= " + (getStartTime()!=null ? getStartTime().format(DATE_TIME_FORMATTER) : "null") +
                ", endTime= " + (getEndTime()!=null ? getEndTime().format(DATE_TIME_FORMATTER) : "null") +
                '}';
    }
    @Override
    public String toCSVRow(){
        String[] taskData = new String[9];
        taskData[0] = Integer.toString(getIdentifier());
        taskData[1] = Tasks.EPIC.getTaskType();
        taskData[2] = getName();
        taskData[3] = getStatus().getStatus();
        taskData[4] = getDescription();
        taskData[5] = "";
        taskData[6] = (duration!=null ? String.valueOf(duration.toMinutes()) : "0");
        taskData[7] = (getStartTime()!=null ? getStartTime().format(DATE_TIME_FORMATTER) : "0");
        taskData[8] = "\n";
        return String.join(",", taskData);
    }
}
