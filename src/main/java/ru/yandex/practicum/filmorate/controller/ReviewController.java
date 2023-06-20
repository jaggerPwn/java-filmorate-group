package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.ReviewDTO;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;

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
        log.info("Получен GET запрос по эндпоинту '/reviews' на получение поста " + reviewId);
        return new ResponseEntity<>(reviewService.getReviewById(reviewId), HttpStatus.OK);
    }
}
