package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    Review saveReview(Review review);

    Review updateReview(Review review);

    List<Review> readAllReviews();

    Review getReviewById(Long id);

    void deleteReviewById(Long id);
}
