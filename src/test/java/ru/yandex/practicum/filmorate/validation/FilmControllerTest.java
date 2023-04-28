package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmControllerTest {

    @Test
    public void shouldThrowExceptionWhenFilmNameIsEmpty() {
        Film film = Film.builder()
                .id(5L)
                .name("")
                .description("A great movie")
                .releaseDate(LocalDate.of(2000, 11, 11))
                .duration(200)
                .build();
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> Validator.filmValidator(film));
        Assertions.assertEquals("Name can not be empty", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenFilmDescrIsMore200() {
        Film film = Film.builder()
                .id(5L)
                .name("The Big Lebowski")
                .description("A greatest movie".repeat(100))
                .releaseDate(LocalDate.of(1998, 1, 18))
                .duration(200)
                .build();
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> Validator.filmValidator(film));
        Assertions.assertEquals("Description size can`t be mre than 200.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenFilmDatesIsWrong() {
        Film film = Film.builder()
                .id(5L)
                .name("The Big Lebowski")
                .description("A greatest movie")
                .releaseDate(LocalDate.of(1888, 1, 18))
                .duration(200)
                .build();
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> Validator.filmValidator(film));
        Assertions.assertEquals("Date is wrong", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDurationIsNegative() {
        Film film = Film.builder()
                .id(5L)
                .name("The Big Lebowski")
                .description("A greatest movie")
                .releaseDate(LocalDate.of(1998, 1, 18))
                .duration(-200)
                .build();
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> Validator.filmValidator(film));
        Assertions.assertEquals("Duration must be positive", exception.getMessage());
    }

    @Test
    public void shouldNotThrowExceptionWhenFilmIsValid() {
        Film film = Film.builder()
                .id(5L)
                .name("The Big Lebowski")
                .description("A greatest movie")
                .releaseDate(LocalDate.of(1998, 1, 18))
                .duration(200)
                .build();
        Assertions.assertDoesNotThrow(() -> Validator.filmValidator(film));
    }

}
