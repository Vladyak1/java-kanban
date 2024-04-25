package model;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.time.Duration;
import java.time.LocalDateTime;
import utils.Status;
import utils.TaskType;

public class Task {
    private int id;
    private String title;
    private String description;
    private Status status;
    private TaskType type;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

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

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Task(String title, String description, String start, long minutes) {
        this.title = title;
        this.description = description;
        status = Status.NEW;
        this.duration = Duration.ofMinutes(minutes);
        try {
            this.startTime = LocalDateTime.parse(start, dateTimeFormatter);
            this.endTime = startTime.plusMinutes(duration.toMinutes());
        } catch (NullPointerException o) {
            this.startTime = null;
            this.endTime = null;
        }
    }

    public Task(int id, String title, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.type = TaskType.TASK;
    }

    public Task(String title, String description, LocalDateTime start, Duration minutes) {
        this.title = title;
        this.description = description;
        status = Status.NEW;
        this.duration = minutes;
        try {
            this.startTime = start;
            this.endTime = startTime.plusMinutes(duration.toMinutes());
        } catch (NullPointerException o) {
            this.startTime = null;
            this.endTime = null;
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", endTime=" + endTime +
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