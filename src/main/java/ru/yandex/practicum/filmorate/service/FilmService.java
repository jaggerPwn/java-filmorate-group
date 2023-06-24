package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDTO;

import java.util.List;

public interface FilmService {

    FilmDTO saveFilm(FilmDTO film);

    FilmDTO updateFilm(FilmDTO film);

    List<FilmDTO> readAllFilms();

    FilmDTO getFilmByID(Long id);

    void deleteLikeById(Long idFilm, Long idUser);

    void userLike(Long idFilm, Long idUser);

    List<FilmDTO> readTopFilms(Long count);

    List<FilmDTO> searchFilm(String query, String by);

    List<FilmDTO> getTopFilms(Long count);
    void deleteFilm(Long id);

    List<FilmDTO> getCommonFilms(Long userId, Long friendId);

    List<FilmDTO> getSortedFilms(Long id, String sortBy);

}
