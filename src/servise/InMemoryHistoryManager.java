package servise;

import servise.interfase.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public  class InMemoryHistoryManager implements HistoryManager {
    @Override
    public void addHistory(Task task){
        browsingHistory.add(0,task);
    }
    @Override
    public List<Task> getHistory() {
        if(!browsingHistory.isEmpty()){
            return new ArrayList<>(browsingHistory);
        }
        return null;
    }
}
