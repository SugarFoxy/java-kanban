
public class Task {

    private String name;
    private Status status;
    private int identifier;

    public Task(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    public Task(String name, Status status, int identifier) {
        this.name = name;
        this.status = status;
        this.identifier = identifier;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int setIdentifier(int identifier) {
        this.identifier = identifier;
        return this.identifier;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public int getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", identifier=" + identifier +
                '}';
    }

    public enum Status{
        NEW,IN_PROCESS,DONE
    }


}

