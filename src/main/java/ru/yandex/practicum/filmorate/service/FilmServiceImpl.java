package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class FilmServiceImpl implements FilmService {

    private final Map<Long, Film> films = new HashMap<>(); // для хранения фильмов
    private Long genId = 1L;

    @Override
    public Film saveFilm(Film film) {
        film.setId(genId);
        films.put(film.getId(), film);
        genId++;
        log.debug("Фильм успешно сохранён");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Фильм успешно обновлён");
            return film;
        } else {
            log.debug("Фильм не обновлён");
            throw new RuntimeException("Нет такого ID");
        }
    }

    @Override
    public List<Film> readAllFilms() {
        log.debug("Вывод списка фильмов");
        return new ArrayList<>(films.values());
    }
}
