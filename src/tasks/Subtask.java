package tasks;


public class Subtask extends Task {
    private int identifierEpic;

    public Subtask(String name, String description, int identifierEpic) {
        super(name, description);
        this.identifierEpic = identifierEpic;
    }

    public int getIdentifierEpic() {
        return identifierEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "identifierEpic=" + identifierEpic +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", identifier=" + identifier +
                ", description='" + description + '\'' +
                '}';
    }
}
