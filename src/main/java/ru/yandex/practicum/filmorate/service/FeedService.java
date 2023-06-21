package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.EventDTO;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.util.List;

public interface FeedService {

    List<EventDTO> getFeed(long id);
    EventDTO saveFeed(long userId, long timestamp, EventType eventType, Operation operation, long entityId);
}
