package ru.yandex.practicum.tasktracker.utils;

public class enums {
    public enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }

    public enum TaskType {
        TASK,
        EPIC,
        SUBTASK
    }

    public enum csvTableHeaders {
        ID,
        TYPE,
        NAME,
        STATUS,
        DESCRIPTION,
        EPIC
    }
}
