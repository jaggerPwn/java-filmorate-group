package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ReviewDTO {
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
}
