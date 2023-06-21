package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
    private long useful;
}
