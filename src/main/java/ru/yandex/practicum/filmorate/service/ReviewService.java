package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.ReviewDTO;

public interface ReviewService {

    ReviewDTO addReview(ReviewDTO review);

    ReviewDTO getReviewById(long reviewId);
}
