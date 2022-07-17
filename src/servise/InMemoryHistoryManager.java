package servise;

import servise.interfase.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public  class InMemoryHistoryManager implements HistoryManager {

    private LinkedList<Task> browsingHistory = new LinkedList<>();

    @Override
    public void addHistory(Task task){
        browsingHistory.addFirst(task);
        int amountElements =10;
        if(browsingHistory.size() > amountElements){
            browsingHistory.removeLast();
        }
    }

    @Override
    public List<Task> getHistory() {
        if(!browsingHistory.isEmpty()){
            return new ArrayList<>(browsingHistory);
        }
        return null;
    }
}
