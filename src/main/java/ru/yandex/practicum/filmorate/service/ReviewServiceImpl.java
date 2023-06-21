package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.ReviewDTO;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewStorage rs;

    @Autowired
    public ReviewServiceImpl(@Qualifier("reviewDbStorage") ReviewStorage rs) {
        this.rs = rs;
    }


    @Override
    public ReviewDTO addReview(ReviewDTO reviewDTO) {
        Review review = rs.saveReview(ReviewMapper.dtoToReview(reviewDTO));
        return ReviewMapper.reviewToDTO(review);
    }

    @Override
    public ReviewDTO updateReview(ReviewDTO reviewDTO) {
        Review review = rs.updateReview(ReviewMapper.dtoToReview(reviewDTO));
        return ReviewMapper.reviewToDTO(review);
    }

    @Override
    public void deleteFilmById(Long filmId) {
        rs.deleteReviewById(filmId);
    }

    @Override
    public void addReviewLike(Long reviewId, Long userId) {
        //TODO валидация на наличие лайка
        //TODO добавить проверку был ли лайк от пользователя
        ReviewDTO review = getReviewById(reviewId);
        long useful = review.getUseful() + 1;
        review.setUseful(useful);
        updateReview(review);
    }

    @Override
    public void addReviewDISLike(Long reviewId, Long userId) {
        ReviewDTO review = getReviewById(reviewId);
        long useful = review.getUseful() - 1;
        review.setUseful(useful);
        updateReview(review);
    }

    @Override
    public ReviewDTO getReviewById(long reviewId) {
        log.debug("Review c ID c {} получен.", reviewId);
        return ReviewMapper.reviewToDTO(rs.getReviewById(reviewId));
    }

    @Override
    public List<ReviewDTO> getAllReviews(Long filmId, Long count) {
        log.debug("Все Review получены.");
        return ReviewMapper.listUsersToListDto(rs.readAllReviews(filmId, count));
    }
}
