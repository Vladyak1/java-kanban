package main.http.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import main.model.Subtask;
import main.model.Task;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

public class TaskSerializer<T extends Task> implements JsonSerializer<T> {
    protected final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public JsonElement serialize(T task, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", task.getId());
        jsonObject.addProperty("name", task.getTitle());
        jsonObject.addProperty("description", task.getDescription());
        jsonObject.addProperty("status", task.getStatus().toString());
        jsonObject.addProperty("startTime", task.getStartTime().format(formatter));
        jsonObject.addProperty("endTime", task.getEndTime().format(formatter));
        jsonObject.addProperty("duration", task.getDuration().toMinutes());
        if (typeOfSrc.equals(Subtask.class)) {
            jsonObject.addProperty("epicId", ((Subtask) task).getEpicId());
        }
        return jsonObject;
    }
}
