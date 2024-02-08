package ru.yandex.practicum.tasktracker.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{
    private List<Integer> subTaskIds;

    public Epic(String title, String description) {
        super(title, description);
        this.subTaskIds = new ArrayList<>();
    }

    public void addSubTaskIds(int id) {
        subTaskIds.add(id);
    }

    public List<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subTaskIds=" + subTaskIds + '\'' +
                '}';
    }

    public void removeSubTaskIds(Integer id) {
        subTaskIds.remove(id);
    }

    public void clearSubTaskIds() {
        subTaskIds.clear();
    }
}
