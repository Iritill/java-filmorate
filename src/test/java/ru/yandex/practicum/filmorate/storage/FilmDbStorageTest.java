package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmDbStorageTest {


    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    FilmDbStorage filmStorage;
    UserDbStorage userStorage;
    LikeDbStorage likeStorage;

    Film firstCorrectFilm;
    Film secondCorrectFilm;
    Film incorrectFilm;

    User firstUser;
    User secondUser;
    User thirdUser;

    @BeforeEach
    protected void beforeEach() {
        filmStorage = new FilmDbStorage(namedParameterJdbcTemplate);
        userStorage = new UserDbStorage(namedParameterJdbcTemplate);
        likeStorage = new LikeDbStorage(jdbcTemplate);

        firstCorrectFilm = new Film();
        firstCorrectFilm.setName("FirstCorrectFilm");
        firstCorrectFilm.setDuration(100);
        firstCorrectFilm.setMpa(new Mpa(3L, "PG-13"));
        firstCorrectFilm.setReleaseDate(LocalDate.of(2000, 10, 10));
        firstCorrectFilm.setDescription("Description");

        secondCorrectFilm = new Film();
        secondCorrectFilm.setName("SecondCorrectFilm");
        secondCorrectFilm.setDuration(1002);
        secondCorrectFilm.setMpa(new Mpa(3L, "PG-13"));
        secondCorrectFilm.setReleaseDate(LocalDate.of(2000, 10, 10));
        secondCorrectFilm.setDescription("Description2");

        incorrectFilm = new Film();
        incorrectFilm.setName("incorrectFilm");
        incorrectFilm.setDuration(-100);
        incorrectFilm.setMpa(new Mpa(100000L, "1934-PG-13"));
        incorrectFilm.setReleaseDate(LocalDate.of(3000, 10, 10));
        incorrectFilm.setDescription("qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiop");
    }

    @Test
    public void createAndGetByIdTest() {
        Film film = filmStorage.create(firstCorrectFilm);
        Film createdFilm = filmStorage.getById(film.getId());
        assertNotNull(createdFilm, "Film does not exist");
        assertEquals(film.getId(), 1);
        assertEquals(film.getName(), "FirstCorrectFilm");
        assertEquals(film.getDuration(), 100);
        assertEquals(film.getMpa(), new Mpa(3L, "PG-13"));
        assertEquals(film.getReleaseDate(), LocalDate.of(2000, 10, 10));
        assertEquals(film.getDescription(), "Description");
    }

    @Test
    public void incorrectCreateTest() {
        assertThrows(Exception.class, () -> {
            filmStorage.create(incorrectFilm);
        });
    }


    @Test
    public void notFoundFilmTest() {
        assertThrows(NotFoundException.class, () -> {
            Film film = filmStorage.getById(10000L);
            if (film == null) {
                throw new NotFoundException("Film does not exist");
            }
        });
    }

    @Test
    public void testUpdate() {
        Film film = filmStorage.create(firstCorrectFilm);

        Film newFilm = new Film();

        newFilm.setId(1L);
        newFilm.setName("SecondCorrectFilmUpdate");
        newFilm.setDuration(50);
        newFilm.setMpa(new Mpa(1L, "G"));
        newFilm.setReleaseDate(LocalDate.of(1950, 10, 10));
        newFilm.setDescription("123");

        filmStorage.update(newFilm);

        Film updatedFilm = filmStorage.getById(film.getId());

        assertEquals(newFilm.getId(), updatedFilm.getId());
        assertEquals(newFilm.getName(), updatedFilm.getName());
        assertEquals(newFilm.getDuration(), updatedFilm.getDuration());
        assertEquals(newFilm.getMpa(), updatedFilm.getMpa());
        assertEquals(newFilm.getReleaseDate(), updatedFilm.getReleaseDate());
        assertEquals(newFilm.getDescription(), updatedFilm.getDescription());
    }

    @Test
    public void incorrectUpdate() {
        filmStorage.create(firstCorrectFilm);
        Film newFilm = new Film();
        newFilm.setId(1L);
        newFilm.setName("FirstCorrectName");
        newFilm.setDuration(-50);
        newFilm.setMpa(new Mpa(00000111111L, "123-G-123"));
        newFilm.setReleaseDate(LocalDate.of(30000, 10, 10));
        assertThrows(Exception.class, () -> {
            filmStorage.update(newFilm);
            filmStorage.update(newFilm);
        });
    }


    @Test
    public void getAllTest() {
        Film film1 = filmStorage.create(firstCorrectFilm);
        Film film2 = filmStorage.create(secondCorrectFilm);
        List<Film> list = new ArrayList<>();
        list.add(film1);
        list.add(film2);
        assertEquals(list, filmStorage.getAll());
    }

    @Test
    public void getPopularFilmsTest() {
        firstUser = new User();
        firstUser.setName("1");
        firstUser.setBirthday(LocalDate.of(2000, 1, 1));
        firstUser.setEmail("1@yandex.ru");
        firstUser.setLogin("login1");

        secondUser = new User();
        secondUser.setName("2");
        secondUser.setBirthday(LocalDate.of(2000, 1, 2));
        secondUser.setEmail("2@yandex.ru");
        secondUser.setLogin("login2");

        thirdUser = new User();
        thirdUser.setName("3");
        thirdUser.setBirthday(LocalDate.of(2000, 1, 3));
        thirdUser.setEmail("3@yandex.ru");
        thirdUser.setLogin("login3");

        userStorage.create(firstUser);
        userStorage.create(secondUser);
        userStorage.create(thirdUser);

        filmStorage.create(firstCorrectFilm);
        filmStorage.create(secondCorrectFilm);

        likeStorage.add(1L, 1L);
        likeStorage.add(1L, 2L);
        likeStorage.add(1L, 3L);

        likeStorage.add(2L, 3L);
        likeStorage.add(2L, 2L);

        assertEquals(firstCorrectFilm.getId(), filmStorage.getPopularFilms(2).getFirst().getId());
        assertEquals(secondCorrectFilm.getId(), filmStorage.getPopularFilms(2).get(1).getId());
    }
}
