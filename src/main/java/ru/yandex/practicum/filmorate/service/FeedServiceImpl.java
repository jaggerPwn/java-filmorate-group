package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.EventDTO;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.util.List;
@Service
@Slf4j
public class FeedServiceImpl implements FeedService {

    private final FeedStorage feedStorage;
    @Autowired
    public FeedServiceImpl(FeedStorage feedStorage) {
        this.feedStorage = feedStorage;
    }

    @Override
    public List<EventDTO> getFeed(long id) {
        return EventMapper.listEventsToListDto(feedStorage.getFeed(id));
    }

    @Override
    public EventDTO saveFeed(long userId, long timestamp, EventType eventType, Operation operation, long entityId) {
        Event eventToSave = Event.builder()
                .userId(userId)
                .timestamp(timestamp)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();

        return EventMapper.eventToDTO(feedStorage.saveFeed(eventToSave));
    }
}
