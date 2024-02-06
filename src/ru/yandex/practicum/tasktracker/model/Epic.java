package ru.yandex.practicum.tasktracker.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{
    private List<Integer> subTaskIds;

    public Epic(Integer id, String name, String description) {
        super(id, name, description);
        this.subTaskIds = new ArrayList<>();
    }

    public void addNewSubTask(Integer subTaskId) {
        subTaskIds.add(subTaskId);
    }

    public void deleteSubTask(Integer subTaskId) {
        subTaskIds.remove(subTaskId);
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
                '}';
    }
}
