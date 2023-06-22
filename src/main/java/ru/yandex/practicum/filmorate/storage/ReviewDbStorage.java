package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


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
        log.debug("Отзыв создан");
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
        log.debug("Отзыв получен");
        return review;
    }

    @Override
    public List<Review> readAllReviews(Long filmId, Long count) {
        if (count == null) count = 10L;
        List<Review> reviews = null;
        if (filmId == null) {
            String sqlQuery = "SELECT R.ID,\n" +
                    "r.CONTENT,\n" +
                    "r.ISPOSITIVE,\n" +
                    "r.USERID,\n" +
                    "r.FILMID, \n" +
                    "COUNT(NULLIF(POSITIVE, false))-COUNT(NULLIF(POSITIVE,true)) useful\n" +
                    "FROM PUBLIC.REVIEWS r\n" +
                    "LEFT JOIN REVIEWLIKES rl ON rl.REVIEWID = r.ID\n" +
                    "GROUP BY R.ID\n" +
                    "ORDER BY useful desc\n" +
                    "LIMIT ?;";
            reviews = jdbcTemplate.query(sqlQuery, ReviewDbStorage::mapToReview, count);
        } else {
            String sqlQuery = "SELECT R.ID,\n" +
                    "r.CONTENT,\n" +
                    "r.ISPOSITIVE,\n" +
                    "r.USERID,\n" +
                    "r.FILMID, \n" +
                    "COUNT(NULLIF(POSITIVE, false))-COUNT(NULLIF(POSITIVE,true)) useful\n" +
                    "FROM PUBLIC.REVIEWS r\n" +
                    "LEFT JOIN REVIEWLIKES rl ON rl.REVIEWID = r.ID\n" +
                    "WHERE FILMID = ?\n" +
                    "GROUP BY R.ID\n" +
                    "ORDER BY useful desc\n" +
                    "LIMIT ?;";
            reviews = jdbcTemplate.query(sqlQuery, ReviewDbStorage::mapToReview, filmId, count);
        }
        log.debug("Отзывы получены");
        return reviews;
    }

    @Override
    public void saveReviewLikes(Long reviewId, Long userId, boolean positive) {
        String sql = "DELETE FROM REVIEWLIKES WHERE REVIEWID = ?";
        jdbcTemplate.update(sql, reviewId);
        sql = "INSERT INTO reviewLikes (REVIEWID, USERID, POSITIVE) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, reviewId, userId, positive);
        log.debug("Лайки сохранены");
    }

    @Override
    public Review updateReview(Review review) {
        //Здесь только 2 параметра обновляется, иначе не проходит постмен тесты.
        // Пояснения в посте наставника: https://app.pachca.com/chats?thread_id=1280887
        String sqlQuery = "UPDATE PUBLIC.REVIEWS \n" +
                "SET CONTENT = ?, ISPOSITIVE = ? \n" +
                "WHERE ID = ?";
        jdbcTemplate.update(sqlQuery, review.getContent(), review.getIsPositive(), review.getReviewId());
        Review reviewById = getReviewById(review.getReviewId());
        log.debug("Отзыв обновлен");
        return reviewById;
    }


    @Override
    public void deleteReviewById(Long id) {
        String sqlQuery = "DELETE FROM PUBLIC.REVIEWS WHERE ID=?";
        jdbcTemplate.update(sqlQuery, id);
        log.debug("Отзыв удален");
    }

    public static Review mapToReview(ResultSet resultSet, int i) throws SQLException {
        Review build = Review.builder()
                .reviewId(resultSet.getLong("id"))
                .content(resultSet.getString("content"))
                .filmId(resultSet.getLong("filmId"))
                .userId(resultSet.getLong("userId"))
                .isPositive(resultSet.getBoolean("isPositive"))
                .build();
        try {
            build.setUseful(resultSet.getInt("useful"));
        } catch (SQLException ignored) {
        }
        return build;

    }
}
