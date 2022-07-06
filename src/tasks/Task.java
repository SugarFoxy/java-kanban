package tasks;


import java.util.Objects;

public class Task extends MasterTasks {
    public Task(String name, String description) {
        super(name, description);
    }

    public Task(String name, Epic.Status status, int identifier, String description) {
        super(name, status, identifier, description);
    }
    @Override
    public Epic.Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task that = (Task) o;
        return identifier == that.identifier
                && name.equals(that.name)
                && status == that.status
                && description.equals(that.description);
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
                '}';
    }
}
