package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReviewDTO {
    private Long reviewId;
    private String content;
    @JsonProperty(value = "isPositive")
    private Boolean isPositive;
    private Long userId;
    private Long filmId;
    @JsonProperty(value = "useful")
    private int useful;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final Map<Long, Boolean> reviewLikes = new HashMap<>();

    public int getLike() {
        int like = 0;
        for (var positive : reviewLikes.values()) {
            if (positive) {
                like++;
            } else {
                like--;
            }
        }
        return like;
    }

    public void addGrade(Long userId, boolean positive) {
        reviewLikes.put(userId, positive);
    }

    public void delGrade(Long userId) {
        reviewLikes.remove(userId);
    }
}