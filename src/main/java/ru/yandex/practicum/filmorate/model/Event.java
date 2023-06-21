package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private long id;
    private long userId;
    private long timestamp;
    private EventType eventType;
    private Operation operation;
    private long entityId;

    public Map<String, Object> eventToMap(){
        Map<String, Object> temp = new HashMap<>();
        temp.put("userId", userId);
        temp.put("timestamp", timestamp);
        temp.put("eventType", eventType.name());
        temp.put("operation", operation.name());
        temp.put("entityId", entityId);
        return temp;
    }
}
