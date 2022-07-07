package tasks;

import java.util.ArrayList;
import java.util.Objects;


public class Epic extends Task {
    private ArrayList<Subtask> subtasks =new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, int identifier, String description) {
        super(name, identifier, description);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks){
        this.subtasks=subtasks;
    }


    public void addSubtasks(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void deleteSubtasks(){
        subtasks.clear();
    }

    public void updateStatus(){
        int sumSubtaskNew =0;
        int sumSubtaskDone =0;
        for(Subtask subtask: subtasks) {
            if (subtask.getStatus().equals(Status.NEW)) {
                ++sumSubtaskNew;
            }else if(subtask.getStatus().equals(Status.DONE)){
                ++sumSubtaskDone;
            }
        }
        if (subtasks.size() == 0 || subtasks.size() == sumSubtaskNew){
            setStatus(Status.NEW);
        } else if (subtasks.size() == sumSubtaskDone) {
            setStatus(Status.DONE);
        }else {
            setStatus(Status.IN_PROCESS);
        }
    }

    public void deleteSubtask(Subtask subtask){
        if (subtasks.contains(subtask)){
            subtasks.remove(subtask);
            updateStatus();
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
    public String toString() {
        return "Epic{" +
                "amount subtasks=" + subtasks.size() +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", identifier=" + identifier +
                ", description='" + description + '\'' +
                '}';
    }
}
