package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    private long id;
    private long userId;
    private long timestamp;
    private EventType eventType;
    private Operation operation;
    private long entityId;
}

