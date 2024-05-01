package main.model;

import main.utils.Status;
import main.utils.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private int epicId = 0;

    public int getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(int subTaskId) {
        this.subTaskId = subTaskId;
    }

    private int subTaskId;

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public Subtask(String title, String description, String start, long minutes) {
        super(title, description, start, minutes);
        setType(TaskType.Subtask);
    }

    public Subtask(String title, String description, LocalDateTime start, Duration minutes) {
        super(title, description, start, minutes);
        setType(TaskType.Subtask);
    }

    public Subtask(int id, String name, String description, Status status, LocalDateTime startTime, Duration duration, int epicId) {
        super(id, name, description, status, startTime, duration);
        this.epicId = epicId;
        setType(TaskType.Subtask);
    }

    @Override
    public String toString() {
        return getId() + ","
                + getType() + ","
                + getTitle() + ","
                + getStatus() + ","
                + getDescription() + ","
                + getEpicId() + ","
                + getStartTime().format(dateTimeFormatter) + ","
                + getEndTime().format(dateTimeFormatter) + ","
                + getDuration().toMinutes();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subTask = (Subtask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}