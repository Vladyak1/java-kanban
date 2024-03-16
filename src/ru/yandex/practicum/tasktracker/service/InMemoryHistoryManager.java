package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    public static class Node {
        Task item;
        Node next;
        Node prev;

        Node(Node prev, Task element, Node next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    private Map<Integer, Node> history = new HashMap<>();
    Node first;
    Node last;

    public List<Task> getAll() {
        ArrayList<Task> list = new ArrayList<>();
        Node current = first;
        while (current != null) {
            list.add(current.item);
            current = current.next;
        }
        return list;
    }

    void linkLast(Task task) {
        final Node newNode = new Node(last, task, null);
        if (first == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        Node prevNode = node.prev;
        Node nextNode = node.next;
        if (prevNode == null) {
            first = nextNode;
        } else {
            prevNode.next = nextNode;
        }
        if (nextNode == null) {
            last = prevNode;
            //prevNode.next = null;
        } else {
            nextNode.prev = prevNode;
        }
        history.remove(node.item.getId());
    }

    @Override
    public Map<Integer, Node> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            System.out.println("Задача не может быть пустой");
            return;
        }
        Node node = history.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(task);
        history.put(task.getId(), last);
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

}
