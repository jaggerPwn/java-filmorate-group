package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;

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
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlInsert = "INSERT INTO PUBLIC.\"REVIEWS\"\n" +
                "(content, ispositive, userid, filmid)" +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(sqlInsert, new String[]{"ID"});
            statement.setString(1, review.getContent());
            statement.setBoolean(2, review.isPositive());
            statement.setLong(3, review.getUserId());
            statement.setLong(4, review.getFilmId());
            return statement;
        }, keyHolder);
        try {
            review.setId(keyHolder.getKey().longValue());
        } catch (InvalidDataAccessApiUsageException | NullPointerException e) {
            e.printStackTrace();
            throw new ValidationException("key not assigned");
        }
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        return null;
    }

    @Override
    public List<Review> readAllReviews() {
        return null;
    }

    @Override
    public Review getReviewById(Long id) {
        String sqlQuery = "SELECT ID, CONTENT, ISPOSITIVE, USERID, FILMID, USEFUL\n" +
                "FROM PUBLIC.REVIEWS where ID =  ?";
        Review review;
        try {
            review = jdbcTemplate.queryForObject(sqlQuery, this::mapToReview, id);
        } catch (DataAccessException e) {
            throw new ValidationException("review " + id + " not found");
        }
        return review;
    }

    @Override
    public void deleteReviewById(Long id) {

    }

    private Review mapToReview(ResultSet resultSet, int i) throws SQLException {
        return Review.builder()
                .id(resultSet.getLong("id"))
                .content(resultSet.getString("content"))
                .filmId(resultSet.getLong("filmId"))
                .userId(resultSet.getLong("userId"))
                .useful(resultSet.getLong("useful"))
                .isPositive(resultSet.getBoolean("isPositive"))
                .build();

    }
}
