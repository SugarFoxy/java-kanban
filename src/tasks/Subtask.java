package tasks;


import java.util.Objects;

public class Subtask extends Task {
    private int identifierEpic;

    public Subtask(String name, String description, int identifierEpic) {
        super(name, description);
        this.identifierEpic = identifierEpic;
    }

    public Subtask(String name, Status status, int identifier, String description, int identifierEpic) {
        super(name, status, identifier, description);
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
                '}';
    }
}

