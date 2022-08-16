package tasks;

public enum Status {
    NEW("NEW"), IN_PROCESS("IN_PROCESS"), DONE("DONE");
    private final String status;
    Status(String status){
        this.status = status;
    }
    public String getStatus(){
        return status;
    }
}
