package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class ValidatorTest {

    @Test
    public void shouldThrowExceptionWhenFilmNameIsEmpty() {
        Film film = new Film(5L, "", "Descr", LocalDate.of(2000, 11, 11), 200);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> Validator.filmValidator(film));
        Assertions.assertEquals("Name can not be empty", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenFilmDescrIsMore200() {
        Film film = new Film(5L, "Film", "YANDEX_POWER".repeat(100), LocalDate.of(2000, 11, 11), 200);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> Validator.filmValidator(film));
        Assertions.assertEquals("Description size can`t be mre than 200.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenFilmDatesIsWrong() {
        Film film = new Film(5L, "Yandex", "Descr", LocalDate.of(1800, 11, 11), 200);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> Validator.filmValidator(film));
        Assertions.assertEquals("Date is wrong", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDurationIsNegative() {
        Film film = new Film(5L, "Yandex", "Descr", LocalDate.of(2000, 11, 11), -200);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> Validator.filmValidator(film));
        Assertions.assertEquals("Duration must be positive", exception.getMessage());
    }

    @Test
    public void shouldMotThrowExceptionWhenFilmIsValid() {
        Film film = new Film(5L, "Yandex", "Descr", LocalDate.of(2000, 11, 11), 200);
        Assertions.assertDoesNotThrow(() -> Validator.filmValidator(film));
    }

    @Test
    public void userNameShoulbeAsLogin() {
        User user = new User(6L, "Yandex@yandex.ru", "Yandex", "", LocalDate.of(1980, 6, 6));
        Validator.userValidator(user);
        Assertions.assertEquals(user.getName(), user.getLogin());
    }


    @Test
    public void shouldThrowExceptionWhenUserEmailIsWrong() {
        User user = new User(6L, "", "Yandex", "Alisa", LocalDate.of(1980, 6, 6));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> Validator.userValidator(user));
        Assertions.assertEquals("Email is wrong", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUserLoginIsWrong() {
        User user = new User(6L, "Yandex@yandex.ru", "", "Alisa", LocalDate.of(1980, 6, 6));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> Validator.userValidator(user));
        Assertions.assertEquals("Login is wrong", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUserDBirthIsWrong() {
        User user = new User(6L, "Yandex@yandex.ru", "Yandex", "Alisa", LocalDate.of(3980, 6, 6));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> Validator.userValidator(user));
        Assertions.assertEquals("Birthday can`t be in future", exception.getMessage());
    }

    @Test
    public void shouldNotThrowExceptionWhenUserIsValid() {
        User user = new User(6L, "Yandex@yandex.ru", "Yandex", "Alisa", LocalDate.of(1980, 6, 6));
        Assertions.assertDoesNotThrow(() -> Validator.userValidator(user));
    }

}