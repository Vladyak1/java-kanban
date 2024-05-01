package main.model;

import main.utils.Status;
import main.utils.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subTaskIds;

    public Epic(String title, String description) {
        super(title, description, null, 0);
        this.subTaskIds = new ArrayList<>();
        setType(TaskType.Epic);
    }

    public Epic(int id, String name, String description, Status status, LocalDateTime startTime, LocalDateTime endTime,
                Duration duration) {
        super(id, name, description, status, startTime, duration);
        this.subTaskIds = new ArrayList<>();
        setType(TaskType.Epic);
        try {
            setEndTime(endTime);
        } catch (NullPointerException o) {
            this.endTime = null;
        }
    }

    public void addSubTaskIds(int id) {
        subTaskIds.add(id);
    }

    public List<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime == null ? defaultDateTime : endTime;
    }

    @Override
    public String toString() {
        return getId() + ","
                + getType() + ","
                + getTitle() + ","
                + getStatus() + ","
                + getDescription() + ","
                + "n/a" + ","
                + getStartTime().format(dateTimeFormatter) + ","
                + getEndTime().format(dateTimeFormatter) + ","
                + getDuration().toMinutes();
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