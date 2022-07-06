package tasks;

import java.util.Objects;

public abstract class MasterTasks {
    protected String name;
    protected Epic.Status status;
    protected int identifier;
    protected String description;

    public MasterTasks(String name, String description) {
        this.name = name;
        this.description = description;
        this.status= Epic.Status.NEW;
    }

    public MasterTasks(String name, Epic.Status status, int identifier, String description) {
        this.name = name;
        this.status = status;
        this.identifier = identifier;
        this.description = description;
    }

    public MasterTasks(String name, int identifier, String description) {
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

    protected Epic.Status getStatus() {
        return status;
    }

    public void setStatus(Epic.Status status) {
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
}
