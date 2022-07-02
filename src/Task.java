
public class Task {

    protected String name;
    protected Status status;
    protected int identifier;
    protected String description;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status=Status.NEW;
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

