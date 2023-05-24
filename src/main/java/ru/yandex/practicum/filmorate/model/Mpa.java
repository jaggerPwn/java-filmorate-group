package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Mpa {

    @NotNull
    @NotBlank
    private String ratingName;
    private Integer ratingId;

    //o	G — у фильма нет возрастных ограничений,
    //o	PG — детям рекомендуется смотреть фильм с родителями,
    //o	PG-13 — детям до 13 лет просмотр не желателен,
    //o	R — лицам до 17 лет просматривать фильм можно только в присутствии взрослого,
    //o	NC-17 — лицам до 18 лет просмотр запрещён.
}
