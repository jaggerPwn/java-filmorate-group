package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.ReviewDTO;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/reviews")
public class ReviewController {

    final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> addReview(@RequestBody @Valid ReviewDTO reviewDTO) {
        log.info("Получен POST запрос по эндпоинту '/reviews' на публикацию поста");
        return new ResponseEntity<>(reviewService.addReview(reviewDTO), HttpStatus.OK);
    }

    @GetMapping("{reviewId}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long reviewId) {
        log.info("Получен GET запрос по эндпоинту '/reviews/{reviewId}' на получение поста " + reviewId);
        return new ResponseEntity<>(reviewService.getReviewById(reviewId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews(
            @RequestParam(value = "filmId", required = false) Long filmId,
            @RequestParam(value = "count", defaultValue = "10", required = false) Long count) {
        log.info("Получен GET запрос по эндпоинту '/reviews' на получение всех постов ");
        return new ResponseEntity<>(reviewService.getAllReviews(filmId, count), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ReviewDTO> updateReview(@RequestBody @Valid ReviewDTO reviewDTO) {
        log.info("Получен PUT запрос по эндпоинту '/reviews' на изменение поста");
        return new ResponseEntity<>(reviewService.updateReview(reviewDTO), HttpStatus.OK);
    }

    @DeleteMapping("{filmId}")
    public void deleteFilmById(@PathVariable Long filmId) {
        log.info("Получен Delete запрос по эндпоинту '/films/{filmId}' на удаление фильма по ID");
        reviewService.deleteFilmById(filmId);
    }

    @PutMapping("{reviewId}/like/{userId}")
    public void addReviewLike(
            @RequestParam(value = "reviewId") Long reviewId,
            @RequestParam(value = "userId") Long userId) {
        log.info(String.format("Получен PUT запрос по эндпоинту '/reviews/:reviewId/like/:userId' " +
                "на лайк поста %d юзером%d", reviewId, userId));
        reviewService.addReviewLike(reviewId, userId);
    }

    @PutMapping("{reviewId}/dislike/{userId}")
    public void addReviewDisLike(
            @PathVariable Long reviewId,
            @PathVariable Long userId) {
        log.info(String.format("Получен PUT запрос по эндпоинту '/reviews/:reviewId/like/:userId' " +
                "на DISлайк поста %d юзером%d", reviewId, userId));
        reviewService.addReviewDISLike(reviewId, userId);
    }

}
