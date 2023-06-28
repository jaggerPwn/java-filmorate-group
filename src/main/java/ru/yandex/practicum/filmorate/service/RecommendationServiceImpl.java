package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeDBStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RecommendationServiceImpl implements RecommendationService{

    private final LikeDBStorage likeDBStorage;
    private final FilmService filmService;

    @Autowired
    public RecommendationServiceImpl(LikeDBStorage likeDBStorage, FilmService filmService) {
        this.likeDBStorage = likeDBStorage;
        this.filmService = filmService;
    }

    @Override
    public List<Film> findRecommendation(Long idUser) {
        List<Long> sameUserIds = likeDBStorage.getUsersWithSameLikes(idUser);
        if (sameUserIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> recommendations = likeDBStorage.getFilmRecommendationsFrom(idUser, sameUserIds);

        List<Film> films = new ArrayList<>();
        for (Long id : recommendations) {
            films.add(FilmMapper.dtoToFilm(filmService.getFilmByID(id)));
        }
        log.debug("Получен список Films рекоменованных для User с ID {}", idUser);
        return films;
    }
}
