package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DirectorStorage {
    Director saveDirector(Director director);

    Director updateDirector(Director director);

    List<Director> readAllDirectors();

    Optional<Director> getDirectorById(Long id);

    Director deleteDirectorById(Long id);

    Set<Director> getDirectorsByFilmId(Long filmId);
}
