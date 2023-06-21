package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Component
@Slf4j
public class FeedDBStorage implements FeedStorage {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public FeedDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Event> getFeed(long id) {
        String sql = "SELECT *" +
                "FROM feed as f" +
                "WHERE user_id = ?;";
        return jdbcTemplate.query(sql, this::mapToEvent, id);
    }

    @Override
    public Event saveFeed(Event event) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("feed")
                .usingGeneratedKeyColumns("id");

        Number key = simpleJdbcInsert.executeAndReturnKey(event.eventToMap());
        event.setId((long)key);
        return event;
    }

    private Event mapToEvent(ResultSet rs, int rowNum) throws SQLException{
        return Event.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .timestamp(rs.getLong("timestamp"))
                .eventType(EventType.valueOf(rs.getString("event_type")))
                .operation(Operation.valueOf(rs.getString("operation")))
                .entityId(rs.getLong("entity_id"))
                .build();
    }
}
