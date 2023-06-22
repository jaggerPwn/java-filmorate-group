package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

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
    @JsonProperty(value = "useful")
    private int useful = 0;
    private final Map<Long, Boolean> reviewlikes = new HashMap<>();

    public int getUsefulFromReviewLikesMap() {
        int useful = 0;
        for (var positive : reviewlikes.values()) {
            if (positive) {
                useful++;
            } else {
                useful--;
            }
        }
        return useful;
    }

    public void addLike(Long userId, boolean positive) {
        reviewlikes.put(userId, positive);
    }

    public void deleteLike(Long userId) {
        reviewlikes.remove(userId);
    }

    public Map<Long, Boolean> getReviewlikes() {
        return new HashMap<>(reviewlikes);
    }

    public Map<String, Object> reviewToMap() {
        Map<String, Object> temp = new HashMap<>();
        temp.put("content", content);
        temp.put("isPositive", isPositive);
        temp.put("userId", userId);
        temp.put("filmId", filmId);
        temp.put("useful", getUsefulFromReviewLikesMap());
        return temp;
    }
}