package ru.yandex.practicum.filmorate.dto;

import javax.validation.constraints.NotBlank;

public class DirectorsDTO {
    private Long id;
    @NotBlank
    private String name;
}
