package main.model;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.time.Duration;
import java.time.LocalDateTime;
import main.utils.Status;
import main.utils.TaskType;

public class Task {
    protected TaskType type;
    protected int id;
    protected String title;
    protected String description;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
    protected final LocalDateTime defaultDateTime = LocalDateTime.MAX;

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
        return startTime == null ? defaultDateTime : startTime;
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

    public void setType(TaskType type) {
        this.type = type;
    }

    public TaskType getType() {
        return type;
    }

    public Task(int id, String title, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(duration.toMinutes());
        this.duration = duration;
        setType(TaskType.Task);
    }

    public Task(String title, String description, String start, long minutes) {
        this.title = title;
        this.description = description;
        status = Status.NEW;
        this.duration = Duration.ofMinutes(minutes);
        setType(TaskType.Task);
        try {
            this.startTime = LocalDateTime.parse(start, dateTimeFormatter);
            this.endTime = startTime.plusMinutes(duration.toMinutes());
        } catch (NullPointerException o) {
            this.startTime = null;
            this.endTime = null;
        }
    }

    public Task(String title, String description, LocalDateTime start, Duration minutes) {
        this.title = title;
        this.description = description;
        status = Status.NEW;
        this.duration = minutes;
        setType(TaskType.Task);
        try {
            this.startTime = start;
            this.endTime = startTime.plusMinutes(duration.toMinutes());
        } catch (NullPointerException o) {
            this.startTime = null;
            this.endTime = null;
        }
    }

    public LocalDateTime getDefaultDateTime() {
        return defaultDateTime;
    }

    @Override
    public String toString() {
        return id + ","
                + type + ","
                + title + ","
                + status + ","
                + description + ","
                + "n/a" + ","
                + getStartTime().format(dateTimeFormatter) + ","
                + getEndTime().format(dateTimeFormatter) + ","
                + getDuration().toMinutes();
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