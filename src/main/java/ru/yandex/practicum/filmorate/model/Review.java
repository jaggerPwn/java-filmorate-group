package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Review {
    private Long reviewId;
    private String content;
    @JsonProperty(value = "isPositive")
    private Boolean isPositive;
    private Long userId;
    private Long filmId;
    private long useful;

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