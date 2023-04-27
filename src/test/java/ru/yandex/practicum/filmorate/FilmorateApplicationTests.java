package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidationException;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void contextLoads() {
    }

}

/*
    public static void filmValidator(Film film) {
        if (film.getName().isEmpty()) {
            log.debug("Name can not be empty");
            throw new ValidationException("Name can not be empty");
        }
        if (film.getDescription().length() > 200) {
            log.debug("Description size can`t be mre than 200.");
            throw new ValidationException("Description size can`t be mre than 200.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Date is wrong");
            throw new ValidationException("Date is wrong");
        }
        if (film.getDuration() <= 0) {
            log.debug("Duration must be positive");
            throw new ValidationException("Duration must be positive");
        }
        log.debug("Validation is successful");
    }*/
