package tasks;

import java.util.Objects;

public class Subtask extends Task {
    private int identifierEpic;

    public Subtask(String name, String description, int identifierEpic, long duration, String startTime) {
        super(name, description, duration, startTime);
        this.identifierEpic = identifierEpic;
    }
    public Subtask(String name, Status status, int identifier, String description, int identifierEpic, long duration, String startTime) {
        super(name, status, identifier, description, duration, startTime);
        this.identifierEpic = identifierEpic;
    }
    public int getIdentifierEpic() {
        return identifierEpic;
    }
    public void setIdentifierEpic(int identifierEpic) {
        this.identifierEpic = identifierEpic;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return identifierEpic == subtask.identifierEpic;
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), identifierEpic);
    }
    @Override
    public String toString() {
        return "Subtask{" +
                "identifierEpic=" + identifierEpic +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", identifier=" + identifier +
                ", description='" + description + '\'' +
                ", duration=" + duration.toMinutes() + "min"+
                ", startTime=" + startTime.format(DATE_TIME_FORMATTER) +
                ", endTime= " + getEndTime().format(DATE_TIME_FORMATTER) +
                '}';
    }
    @Override
    public String toCSVRow(){
        String[] taskData = new String[9];
        taskData[0] = Integer.toString(getIdentifier());
        taskData[1] = Tasks.SUBTASK.getTaskType();
        taskData[2] = getName();
        taskData[3] = getStatus().getStatus();
        taskData[4] = getDescription();
        taskData[5] = String.valueOf(getIdentifierEpic());
        taskData[6] = String.valueOf(getDuration().toMinutes());
        taskData[7] = getStartTime().format(DATE_TIME_FORMATTER);
        taskData[8] = "\n";
        return String.join(",", taskData);
    }
}

