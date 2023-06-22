package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


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
}
