package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    Film saveFilm(Film film);

    Film updateFilm(Film film);

    List<Film> readAllFilms();

}
