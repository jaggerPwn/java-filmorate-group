package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;
import ru.yandex.practicum.filmorate.validation.ValidationException;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService service = new FilmServiceImpl();

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody Film newFilm) {
        try {
            Validator.filmValidator(newFilm);
            return new ResponseEntity<>(service.saveFilm(newFilm), HttpStatus.CREATED);
        } catch (ValidationException e) {
            return new ResponseEntity<>(newFilm, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film newFilm) {
        try {
            Validator.filmValidator(newFilm);
            return new ResponseEntity<>(service.updateFilm(newFilm), HttpStatus.OK);
        } catch (ValidationException e) {
            return new ResponseEntity<>(newFilm, HttpStatus.valueOf(500));
        }
    }

    @GetMapping
    public ResponseEntity<List<Film>> readAllFilms() {
        return new ResponseEntity<>(service.readAllFilms(), HttpStatus.OK);
    }

}
