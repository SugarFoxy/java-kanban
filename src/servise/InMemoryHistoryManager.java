package servise;

import servise.interfase.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public  class InMemoryHistoryManager implements HistoryManager {

    private List<Task> browsingHistory = new ArrayList<>();
    @Override
    public void addHistory(Task task){
        browsingHistory.add(0,task);
        deleteExtraElement();
    }

    @Override
    public List<Task> getHistory() {
        if(!browsingHistory.isEmpty()){
            return new ArrayList<>(browsingHistory);
        }
        return null;
    }

    private void deleteExtraElement(){
        if(browsingHistory.size() > AMOUNT_OF_ELEMENTS_IN_HISTORY){
           browsingHistory.remove(AMOUNT_OF_ELEMENTS_IN_HISTORY);
        }
    }
}
