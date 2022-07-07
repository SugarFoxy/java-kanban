package tasks;


import java.util.Objects;

public class Task {
    protected String name;
    protected Status status;
    protected int identifier;
    protected String description;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status= Status.NEW;
    }

    public Task(String name, Status status, int identifier, String description) {
        this.name = name;
        this.status = status;
        this.identifier = identifier;
        this.description = description;
    }

    public Task (String name, int identifier, String description) {
        this.name = name;
        this.identifier = identifier;
        this.description = description;
    }

    public String getName() {
        return name;
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
                '}';
    }
}
