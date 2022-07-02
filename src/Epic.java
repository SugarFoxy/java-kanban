import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId =new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId (ArrayList<Integer> subtasksId) {
        this.subtasksId =subtasksId;
    }

    public void putSubtasks(int epicId) {
        subtasksId.add(epicId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "amount subtasks=" + subtasksId.size() +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", identifier=" + identifier +
                ", description='" + description + '\'' +
                '}';
    }
}
