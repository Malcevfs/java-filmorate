package ru.yandex.practicum.filmorate.tests;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storrage.film.InMemoryFilmStorage;

import static ru.yandex.practicum.filmorate.tests.TestValidator.errorMessage;

import java.time.LocalDate;


public class ValidationTests {

    @Test
    public void createUserTest() {
        User user = new User("testya.ru", "", "Nick", LocalDate.of(2025, 12, 01));

        Assertions.assertAll(
                () -> Assertions.assertTrue(errorMessage(user, "invalid Email")),
                () -> Assertions.assertTrue(errorMessage(user, "empty data in Login")),
                () -> user.setEmail(""),
                () -> Assertions.assertTrue(errorMessage(user, "empty data in Email")),
                () -> Assertions.assertTrue(errorMessage(user, "birthday can`t be future")),
                () -> user.setLogin("wrong login"),
                () -> Assertions.assertTrue(errorMessage(user, "wrong symbols in login"))
        );
    }

    @Test
    public void createFilmTest() {
        Film film = new Film("", "Description", LocalDate.of(1950, 12, 01), -200);
        film.setDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation");

        Assertions.assertAll(
                () -> Assertions.assertTrue(errorMessage(film, "empty name")),
                () -> Assertions.assertTrue(errorMessage(film, "max description size is 200 chars")),
                () -> Assertions.assertTrue(errorMessage(film, "duration is negative"))
        );
    }

    @Test(expected = ValidationException.class)
    public void invalidReleaseDateTest() {
        InMemoryFilmStorage filmController = new InMemoryFilmStorage();
        Film film = new Film("name", "Description", LocalDate.of(1800, 12, 01), 200);

        filmController.add(film);
    }

}
