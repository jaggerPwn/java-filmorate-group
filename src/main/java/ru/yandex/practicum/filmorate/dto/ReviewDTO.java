package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ReviewDTO {
    @NotNull
    private long reviewId;
    private String content;
    @JsonProperty(value = "isPositive")
    private boolean isPositive;
    @NotNull
    private long userId;
    @NotNull
    private long filmId;
    @Builder.Default
    private long useful = 0;
}
