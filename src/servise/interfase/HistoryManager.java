package servise.interfase;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    int AMOUNT_OF_ELEMENTS_IN_HISTORY =10;
    List<Task> browsingHistory = new ArrayList<>();

     void addHistory(Task task) ;

    List<Task> getHistory();
}
