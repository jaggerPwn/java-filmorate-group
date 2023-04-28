package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserControllerTest {

    @Test
    public void userNameShoulbeAsLogin() {
        User user = User.builder()
                .id(6L)
                .email("Yandex@yandex.ru")
                .login("Dude")
                .name("")
                .birthday(LocalDate.of(1980, 6, 6))
                .build();
        Validator.userValidator(user);
        Assertions.assertEquals(user.getName(), user.getLogin());
    }


    @Test
    public void shouldThrowExceptionWhenUserEmailIsWrong() {
        User user = User.builder()
                .id(6L)
                .email("")
                .login("Dude")
                .name("Alisa")
                .birthday(LocalDate.of(1980, 6, 6))
                .build();
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> Validator.userValidator(user));
        Assertions.assertEquals("Email is wrong", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUserLoginIsWrong() {
        User user = User.builder()
                .id(6L)
                .email("Yandex@yandex.ru")
                .login("")
                .name("Alisa")
                .birthday(LocalDate.of(1980, 6, 6))
                .build();
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> Validator.userValidator(user));
        Assertions.assertEquals("Login is wrong", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUserDBirthIsWrong() {
        User user = User.builder()
                .id(6L)
                .email("Yandex@yandex.ru")
                .login("Dude")
                .name("Alisa")
                .birthday(LocalDate.of(3980, 6, 6))
                .build();
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> Validator.userValidator(user));
        Assertions.assertEquals("Birthday can`t be in future", exception.getMessage());
    }

    @Test
    public void shouldNotThrowExceptionWhenUserIsValid() {
        User user = User.builder()
                .id(6L)
                .email("Yandex@yandex.ru")
                .login("Dude")
                .name("Alisa")
                .birthday(LocalDate.of(1980, 6, 6))
                .build();
        Assertions.assertDoesNotThrow(() -> Validator.userValidator(user));
    }
}
