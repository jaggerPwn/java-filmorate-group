package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Component("reviewDbStorage")
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Review saveReview(Review review) {
        Validator.reviewValidator(review);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlInsert = "INSERT INTO PUBLIC.\"REVIEWS\"\n" +
                "(content, ispositive, userid, filmid)" +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(sqlInsert, new String[]{"ID"});
            statement.setString(1, review.getContent());
            statement.setBoolean(2, review.getIsPositive());
            statement.setLong(3, review.getUserId());
            statement.setLong(4, review.getFilmId());
            return statement;
        }, keyHolder);
        try {
            review.setReviewId(keyHolder.getKey().longValue());
        } catch (InvalidDataAccessApiUsageException | NullPointerException e) {
            e.printStackTrace();
            throw new ValidationException("key not assigned");
        }
        return review;
    }

    @Override
    public Review getReviewById(Long id) {
        String sqlQuery = "SELECT ID, CONTENT, ISPOSITIVE, USERID, FILMID\n" +
                "FROM PUBLIC.REVIEWS where ID =  ?";
        Review review;
        try {
            review = jdbcTemplate.queryForObject(sqlQuery, ReviewDbStorage::mapToReview, id);
        } catch (DataAccessException e) {
            throw new EntityNotFoundException("review " + id + " not found");
        }
        return review;
    }

    @Override
    public List<Review> readAllReviews(Long filmId, Long count) {
        if (count == null) count = 10L;
        List<Review> reviews = null;
        if (filmId == null) {
            String sqlQuery = "SELECT ID, CONTENT, ISPOSITIVE, USERID, FILMID " +
                    "FROM PUBLIC.REVIEWS LIMIT ?";
            reviews = jdbcTemplate.query(sqlQuery, ReviewDbStorage::mapToReview, count);
        } else {
            String sqlQuery = "SELECT ID, CONTENT, ISPOSITIVE, USERID, FILMID " +
                    "FROM PUBLIC.REVIEWS WHERE FILMID = ? LIMIT ?";
            reviews = jdbcTemplate.query(sqlQuery, ReviewDbStorage::mapToReview, filmId, count);
        }
        reviews.forEach(this::loadReviewLikes);
        return reviews.stream()
                .sorted(Comparator.comparing(Review::getUsefulRate).reversed())
                .collect(Collectors.toList());
    }

    public void loadReviewLikes(Review review) {
        String sql = "SELECT * FROM REVIEWLIKES WHERE  REVIEWID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, review.getReviewId());
        while (sqlRowSet.next()) {
            long userid = sqlRowSet.getLong("USERID");
            boolean positive = sqlRowSet.getBoolean("POSITIVE");
            review.addLike(userid, positive);
        }
        review.setUseful(review.getUsefulRate());
    }

    @Override
    public void saveReviewLikes(Review review) {
        String sql = "DELETE FROM REVIEWLIKES WHERE REVIEWID = ?";
        jdbcTemplate.update(sql, review.getReviewId());
        sql = "INSERT INTO reviewLikes (REVIEWID, USERID, POSITIVE) VALUES(?, ?, ?)";
        Map<Long, Boolean> reviewLikes = review.getReviewlikes();
        for (var grade : reviewLikes.entrySet()) {
            jdbcTemplate.update(sql, review.getReviewId(), grade.getKey(), grade.getValue());
        }
    }

    @Override
    public Review updateReview(Review review) {
        //Здесь только 2 параметра обновляется, иначе не проходит постмен тесты.
        // Пояснения в посте наставника: https://app.pachca.com/chats?thread_id=1280887
        String sqlQuery = "UPDATE PUBLIC.REVIEWS \n" +
                "SET CONTENT = ?, ISPOSITIVE = ? \n" +
                "WHERE ID = ?";
        jdbcTemplate.update(sqlQuery, review.getContent(), review.getIsPositive(), review.getReviewId());
        return getReviewById(review.getReviewId());
    }


    @Override
    public void deleteReviewById(Long id) {
        String sqlQuery = "DELETE FROM PUBLIC.REVIEWS WHERE ID=?";
        jdbcTemplate.update(sqlQuery, id);
    }

    public static Review mapToReview(ResultSet resultSet, int i) throws SQLException {
        return Review.builder()
                .reviewId(resultSet.getLong("id"))
                .content(resultSet.getString("content"))
                .filmId(resultSet.getLong("filmId"))
                .userId(resultSet.getLong("userId"))
                .isPositive(resultSet.getBoolean("isPositive"))
                .build();

    }
}
