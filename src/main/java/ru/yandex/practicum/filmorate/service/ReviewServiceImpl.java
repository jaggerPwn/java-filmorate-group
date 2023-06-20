package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.ReviewDTO;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

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
        Review review1 = rs.saveReview(ReviewMapper.dtoToReview(reviewDTO));
        return ReviewMapper.reviewToDTO(review1);
    }

    @Override
    public ReviewDTO getReviewById(long reviewId) {
        log.debug("Review c ID c {} получен.", reviewId);
        return ReviewMapper.reviewToDTO(rs.getReviewById(reviewId));
    }
}
