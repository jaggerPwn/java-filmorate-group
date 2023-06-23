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

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> addReview(@RequestBody @Valid ReviewDTO reviewDTO) {
        log.info("Получен POST запрос по эндпоинту '/reviews' на публикацию отзыва");
        return new ResponseEntity<>(reviewService.addReview(reviewDTO), HttpStatus.CREATED);
    }

    @GetMapping("{reviewId}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long reviewId) {
        log.info("Получен GET запрос по эндпоинту '/reviews/{reviewId}' на получение отзыва с ID {} ", reviewId);
        return new ResponseEntity<>(reviewService.getReviewById(reviewId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews(
            @RequestParam(required = false) Long filmId,
            @RequestParam(defaultValue = "10", required = false) Long count) {
        log.info("Получен GET запрос по эндпоинту '/reviews' на получение REVIEWS в кол-ве {} для FILM c ID {}",
                count, filmId);
        return new ResponseEntity<>(reviewService.getAllReviews(filmId, count), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ReviewDTO> updateReview(@RequestBody @Valid ReviewDTO reviewDTO) {
        log.info("Получен PUT запрос по эндпоинту '/reviews' на изменение отзывов");
        return new ResponseEntity<>(reviewService.updateReview(reviewDTO), HttpStatus.OK);
    }

    @DeleteMapping("{filmId}")
    public void deleteReviewById(@PathVariable Long filmId) {
        log.info("Получен Delete запрос по эндпоинту '/films/{filmId}' на удаление отзыва по ID фильма {} ", filmId);
        reviewService.deleteReviewById(filmId);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addReviewLike(
            @PathVariable Long reviewId,
            @PathVariable Long userId) {
        log.info("Получен PUT запрос по эндпоинту '/reviews/{reviewId}/like/{userId}' на лайк отзыва {} юзером {}",
                reviewId, userId);
        reviewService.addReviewLikeOrDislike(reviewId, userId, true);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addReviewDisLike(
            @PathVariable Long reviewId,
            @PathVariable Long userId) {
        log.info("Получен PUT запрос по эндпоинту '/reviews/{reviewId}/like/{userId}' на disлайк отзыва {} юзером {}",
                reviewId, userId);
        reviewService.addReviewLikeOrDislike(reviewId, userId, false);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void deleteReviewLike(
            @PathVariable Long reviewId,
            @PathVariable Long userId) {
        log.info("Получен DELETE запрос по эндпоинту '/reviews/{reviewId}/like/{userId}' на удаление " +
                "лайка отзыва {} юзером {}", reviewId, userId);
        reviewService.deleteReviewLikeOrDislike(reviewId, userId, true);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void deleteReviewDisLike(
            @PathVariable Long reviewId,
            @PathVariable Long userId) {
        log.info("Получен DELETE запрос по эндпоинту '/reviews/{reviewId}/like/{userId}' на удаление " +
                "disлайка отзыва {} юзером {}", reviewId, userId);
        reviewService.deleteReviewLikeOrDislike(reviewId, userId, false);
    }

}
