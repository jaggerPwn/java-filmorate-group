package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmDTO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FilmMapper {

    private FilmMapper() {
    }

    public static FilmDTO filmToFilmDTO(Film film) {
        if (film == null) {
            return null;
        }

        return FilmDTO.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .likes(film.getLikes())
                .build();
    }

    public static Film dtoToFilm(FilmDTO filmDTO) {
        if (filmDTO == null) {
            return null;
        }

        return Film.builder()
                .id(filmDTO.getId())
                .name(filmDTO.getName())
                .description(filmDTO.getDescription())
                .releaseDate(filmDTO.getReleaseDate())
                .duration(filmDTO.getDuration())
                .build();
    }

    public static List<FilmDTO> listFilmsToListFilmsDto(Collection<Film> films) {
        return films.stream().map(FilmMapper::filmToFilmDTO).collect(Collectors.toList());
    }

}
