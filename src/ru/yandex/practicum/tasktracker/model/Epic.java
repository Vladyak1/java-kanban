package ru.yandex.practicum.tasktracker.model;

import ru.yandex.practicum.tasktracker.utils.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subTaskIds;

    public Epic(String title, String description) {
        super(title, description);
        this.subTaskIds = new ArrayList<>();
        this.setType(enums.TaskType.EPIC);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTaskIds, epic.subTaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskIds);
    }

    public void removeSubTaskIds(Integer id) {
        subTaskIds.remove(id);
    }

    public void clearSubTaskIds() {
        subTaskIds.clear();
    }
}
