package ru.yandex.practicum.tasktracker.model;

import ru.yandex.practicum.tasktracker.utils.Status;
import ru.yandex.practicum.tasktracker.utils.TaskType;

import java.util.Objects;

public class Task {
    private int id;
    private String title;
    private String description;
    private Status status;

    private TaskType type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Integer getEpicId() {
        return null;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        status = Status.NEW;
        type = TaskType.TASK;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(title, task.title) && Objects.equals(description, task.description) &&
                Objects.equals(id, task.id) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status);
    }
}
