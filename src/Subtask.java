import java.util.Objects;

public class Subtask extends Task {
    private Integer identifierEpic;
    public Subtask( String name, Status status, Integer identifierEpic){
        super(name,status);
        this.identifierEpic = identifierEpic;
    }

    public Subtask(String name, Status status, Integer identifierEpic, int identifier) {
        super(name, status, identifier);
        this.identifierEpic = identifierEpic;
    }

    public void setIdentifierEpic(Integer identifierEpic) {
        this.identifierEpic = identifierEpic;
    }

    public Integer getIdentifierEpic() {
        return identifierEpic;
    }

    @Override
    public String toString() {
        return super.toString() +
                " Subtask{" +
                "identifierEpic=" + identifierEpic +
                '}';
    }
}
