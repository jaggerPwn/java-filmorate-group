package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.ReviewDTO;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.util.List;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewStorage rs;
    private final UserService us;

    @Autowired
    public ReviewServiceImpl(@Qualifier("reviewDbStorage") ReviewStorage rs, UserService us) {
        this.rs = rs;
        this.us = us;
    }


    @Override
    public ReviewDTO addReview(ReviewDTO reviewDTO) {
        Review review = rs.saveReview(ReviewMapper.dtoToReview(reviewDTO));
        log.debug("получен запрос на добавление Review ");
        return ReviewMapper.reviewToDTO(review);
    }

    @Override
    public ReviewDTO updateReview(ReviewDTO reviewDTO) {
        Review review = rs.updateReview(ReviewMapper.dtoToReview(reviewDTO));
        log.debug("получен запрос на обновление Review c ID c {}", reviewDTO.getReviewId());
        return ReviewMapper.reviewToDTO(review);
    }

    @Override
    public void deleteReviewById(Long filmId) {
        log.debug("получен запрос на удаление Review c ID c {}", filmId);
        rs.deleteReviewById(filmId);
    }

    @Override
    public void addReviewLike(Long reviewId, Long userId, boolean positive) {
        Review review = this.rs.getReviewById(reviewId);
        UserDTO user = us.getUserById(userId);
        Validator.validateForGrade(review, user);
        log.debug("Получен запрос на добавление лайка");
        rs.saveReviewLikes(reviewId, userId, positive);
    }

    @Override
    public ReviewDTO getReviewById(long reviewId) {
        log.debug("получен запрос на обновление Review c ID c {}", reviewId);
        return ReviewMapper.reviewToDTO(rs.getReviewById(reviewId));
    }

    @Override
    public List<ReviewDTO> getAllReviews(Long filmId, Long count) {
        log.debug("получен запрос на получение ревью по фильму ID c {}", filmId);
        return ReviewMapper.listUsersToListDto(rs.readAllReviews(filmId, count));
    }
}
