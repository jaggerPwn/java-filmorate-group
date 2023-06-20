package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Review {
    @NotNull
    private long id;
    private String content;
    private boolean isPositive;
    @NotNull
    private long userId;
    @NotNull
    private long filmId;
    @Builder.Default
    private long useful = 0;

    public Map<String, Object> reviewToMap() {
        Map<String, Object> temp = new HashMap<>();
        temp.put("content", content);
        temp.put("isPositive", isPositive);
        temp.put("userId", userId);
        temp.put("filmId", filmId);
        temp.put("useful", useful);
        return temp;
    }
}