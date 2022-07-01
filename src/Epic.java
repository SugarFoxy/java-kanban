import java.util.HashMap;

public class Epic extends Task {
   private HashMap<Integer, Subtask> subtasksHashMap;
    public Epic( String name, Status status) {
        super(name, status);

    }

    public Epic(String name, Status status, int identifier, HashMap<Integer, Subtask> subtasksHashMap) {
        super(name, status, identifier);
        this.subtasksHashMap = subtasksHashMap;
    }


    public HashMap<Integer, Subtask> getSubtasksHashMap() {
        return subtasksHashMap;
    }

    public void setSubtasksHashMap(HashMap<Integer, Subtask> subtasksHashMap) {
        this.subtasksHashMap = subtasksHashMap;
    }

    @Override
    public String toString() {
        String result = super.toString()+
                " Epic{" +
                "subtaskHashMap=" +'\'';
        if(subtasksHashMap != null) {
           result+= subtasksHashMap.values().toString() + '}';
        }else {
            result+="null";
        }
        return result;
    }
}
