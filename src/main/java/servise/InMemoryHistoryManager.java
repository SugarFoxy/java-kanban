package servise;

import servise.interfase.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> browsingHistory = new HashMap<>();
    private Node<Task> first;
    private Node<Task> last;

    private void linkLast(Task t) {
        final Node<Task> l = last;
        final Node<Task> newNode = new Node<>(l, t, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            if (node.prev == null && node.next == null) {
                first = null;
                last = null;
            } else {
                if (node.prev != null) {
                    node.prev.next = node.next;
                } else {
                    node.next.prev = null;
                    first = node.next;
                }
                if (node.next != null) {
                    node.next.prev = node.prev;
                } else {
                    node.prev.next = null;
                    last = node.prev;
                }
            }
        }
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node<Task> node = first;
        while (node != null) {
            tasks.add(node.data);
            node = node.next;
        }
        return tasks;
    }

    @Override
    public void addHistory(Task task) {
        if (task != null) {
            remove(task.getIdentifier());
            linkLast(task);
            browsingHistory.put(task.getIdentifier(), last);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(browsingHistory.get(id));
        browsingHistory.remove(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    private static class Node<T extends Task> {

        public T data;
        public Node<T> next;
        public Node<T> prev;

        public Node(Node<T> prev, T element, Node<T> next) {
            this.data = element;
            this.next = next;
            this.prev = prev;
        }
    }
}

