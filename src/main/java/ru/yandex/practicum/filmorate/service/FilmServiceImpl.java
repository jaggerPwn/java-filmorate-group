package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmDTO;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationException;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage fs;
    private final UserStorage us;

    @Autowired
    public FilmServiceImpl(FilmStorage fs, UserStorage us) {
        this.fs = fs;
        this.us = us;
    }

    @Override
    public FilmDTO saveFilm(FilmDTO filmDTO) {
        Film film = FilmMapper.dtoToFilm(filmDTO);
        if (Validator.filmValidator(film)) {
            log.debug("Фильм " + filmDTO + " сохранён.");
            return FilmMapper.filmToFilmDTO(fs.saveFilm(film));
        }
        throw new ValidationException("Валидация фильма" + filmDTO + " не пройдена");
    }

    @Override
    public FilmDTO updateFilm(FilmDTO filmDTO) {
        Film film = FilmMapper.dtoToFilm(filmDTO);
        if (Validator.filmValidator(film)) {
            log.debug("Фильм " + filmDTO + " обновлён.");
            return FilmMapper.filmToFilmDTO(fs.updateFilm(film));
        }
        throw new ValidationException("Валидация фильма" + filmDTO + " не пройдена");
    }

    @Override
    public List<FilmDTO> readAllFilms() {
        log.debug("Полный список фильмов возвращён.");
        return FilmMapper.listFilmsToListFilmsDto(fs.readAllFilms());
    }

    @Override
    public FilmDTO getFilmByID(Long id) {
        log.debug("Фильм " + id + " возвращён.");
        return FilmMapper.filmToFilmDTO(fs.getFilmById(id));
    }

    @Override
    public void deleteLikeById(Long idFilm, Long idUser) {
        Film film = fs.getFilmById(idFilm);
        us.getUserById(idUser); // для валидации
        film.getLikes().remove(idUser);
        log.debug("Лайк у фильма удалён по ID");
    }

    @Override
    public void userLike(Long idFilm, Long idUser) {
        Film film = fs.getFilmById(idFilm);
        us.getUserById(idUser); // для валидации
        film.getLikes().add(idUser);
        log.debug("Лайк у фильма " + idFilm + "установлен по " + idUser + " юзера");
    }

    @Override
    public List<FilmDTO> readTopFilms(Long count) {
        log.debug("Вывод список из " + count + " фильмов по популярности.");
        return readAllFilms().stream()
                .sorted((a, b) -> b.getLikes().size() - a.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());

    }
}
