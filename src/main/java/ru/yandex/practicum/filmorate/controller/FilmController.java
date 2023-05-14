package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.FilmDTO;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FilmDTO> createFilm(@RequestBody @Valid FilmDTO newFilm) {
        return new ResponseEntity<>(service.saveFilm(newFilm), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<FilmDTO> updateFilm(@RequestBody @Valid FilmDTO newFilm) {
        return new ResponseEntity<>(service.updateFilm(newFilm), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FilmDTO>> readAllFilms() {
        return new ResponseEntity<>(service.readAllFilms(), HttpStatus.OK);
    }

    @PutMapping("{id}/like/{userId}")
    public ResponseEntity<?> userLike(@PathVariable Long id, @PathVariable Long userId) {
        service.userLike(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}/like/{userId}")
    public ResponseEntity<?> deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        service.deleteLikeById(id, userId);
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    @GetMapping("popular")
    public ResponseEntity<List<FilmDTO>> readTopFilms(@RequestParam(required = false, defaultValue = "10") Long count) {
        return new ResponseEntity<>(service.readTopFilms(count), HttpStatus.OK);
    }

    @GetMapping("{filmId}")
    public ResponseEntity<FilmDTO> getFilmById(@PathVariable Long filmId) {
        return new ResponseEntity<>(service.getFilmByID(filmId), HttpStatus.OK);
    }


}
