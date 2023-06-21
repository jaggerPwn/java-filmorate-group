package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    FilmDTO saveFilm(FilmDTO film);

    FilmDTO updateFilm(FilmDTO film);

    List<FilmDTO> readAllFilms();

    FilmDTO getFilmByID(Long id);

    void deleteLikeById(Long idFilm, Long idUser);

    void userLike(Long idFilm, Long idUser);

    List<FilmDTO> readTopFilms(Long count);

    public List<FilmDTO> searchFilm(String query, String by);

    public List<FilmDTO> topFilms();
}
