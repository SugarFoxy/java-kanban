import java.util.HashMap;

public class Manôger {
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, Task> epicHashMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();

    void setHashMaps(String nameTask, String nameSubtask) {

        int taskIndex = 0;

        setSubtaskHashMap(nameSubtask);
        fillHashMapTaskAndEpic(++taskIndex, nameTask);
        
    }

    public void fillHashMapTaskAndEpic(int taskIndex, String nameTask){
        if (subtaskHashMap.size() == 0) {
            setTaskHashMap(taskIndex,nameTask);
        } else {
            setEpicHashMap(taskIndex,nameTask);
        }
    }

    public void setSubtaskHashMap( String nameSubtask) {
        int subtaskIndex = 0;
        while (true) {
            String subtaskName = nameSubtask;
            if (subtaskName.equals("0")) {
                subtaskIndex = 0;
                break;
            } else {
                subtaskHashMap.put(subtaskIndex, new Subtask(subtaskName, false));
            }
        }
    }

    public void setTaskHashMap(int taskIndex, String nameTask) {
        taskHashMap.put(taskIndex, new Task(nameTask, false));
    }

    public void setEpicHashMap(int taskIndex, String nameTask) {
        epicHashMap.put(taskIndex, new Epic(nameTask, false, subtaskHashMap));
    }

    public HashMap<Integer, Subtask> subtaskHashMap () {
        return subtaskHashMap;
    }
}

