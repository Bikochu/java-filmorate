package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {
	FilmController filmController = new FilmController();
	UserController userController = new UserController();

	@BeforeEach
	void beforeEach() {
		filmController.getFilms().clear();
		filmController.setId(0);
		userController.getUsers().clear();
		userController.setId(0);
	}

	@Test
	void validCreateFilm() throws ValidationException {
		Film film = Film.builder()
				.name("Name")
				.description("Description")
				.releaseDate(LocalDate.of(2000,12,20))
				.duration(100L)
				.build();
		filmController.addFilm(film);
		assertEquals(1,film.getId(), "Номер фильму не присвоен.");
		assertEquals(1, filmController.getFilms().size(), "Фильм не добавлен.");
	}

	@Test
	void validNameOfFilm() {
		Film film = Film.builder()
				.id(1)
				.description("Description")
				.releaseDate(LocalDate.of(2000,12,20))
				.duration(100L)
				.build();

		assertThrows(ValidationException.class, ()-> filmController.addFilm(film), "Фильм добавлен.");
	}

	@Test
	void validDescriptionOfFilm() {
		Film film = Film.builder()
				.id(1)
				.name("Name")
				.description("01234567890123456789012345678901234567890123456789012345678901234567890123456789" +
						"01234567890123456789012345678901234567890123456789012345678901234567890123456789" +
						"01234567890123456789012345678901234567890123456789012345678901234567890123456789")
				.releaseDate(LocalDate.of(2000,12,20))
				.duration(100L)
				.build();
		assertThrows(ValidationException.class, ()-> filmController.addFilm(film), "Фильм добавлен.");
	}

	@Test
	void validReleaseDateOfFilm() {
		Film film = Film.builder()
				.id(1)
				.name("Name")
				.description("Description")
				.releaseDate(LocalDate.of(1800,12,20))
				.duration(100L)
				.build();
		assertThrows(ValidationException.class, ()-> filmController.addFilm(film), "Фильм добавлен.");
	}

	@Test
	void validDurationOfFilm() {
		Film film = Film.builder()
				.id(1)
				.name("Name")
				.description("Description")
				.releaseDate(LocalDate.of(2000,12,20))
				.duration(-100L)
				.build();
		assertThrows(ValidationException.class, ()-> filmController.addFilm(film), "Фильм добавлен.");
	}

	@Test
	void validUpdateFilm() throws ValidationException {
		Film film = Film.builder()
				.name("Name")
				.description("Description")
				.releaseDate(LocalDate.of(2000,12,20))
				.duration(100L)
				.build();

		Film film2 = Film.builder()
				.id(1)
				.name("Film")
				.description("Description of film")
				.releaseDate(LocalDate.of(2000,12,20))
				.duration(100L)
				.build();

		filmController.addFilm(film);
		filmController.updateFilm(film2);

		assertEquals(1,filmController.getFilms().size(), "Добавлено больше фильмов в список.");
	}

	@Test
	void updateNameNullOfFilm() throws ValidationException {
		Film film = Film.builder()
				.name("Name")
				.description("Description")
				.releaseDate(LocalDate.of(2000,12,20))
				.duration(100L)
				.build();
		Film film2 = Film.builder()
				.id(1)
				.description("Description of film")
				.releaseDate(LocalDate.of(2000,12,20))
				.duration(100L)
				.build();
		filmController.addFilm(film);

		assertThrows(ValidationException.class, ()-> filmController.updateFilm(film2), "Фильм добавлен.");
		assertEquals(film.getName(),filmController.getFilms().get(0).getName(), "Имя фильма изменилось.");
	}

	@Test
	void updateDescriptionOfFilm() throws ValidationException {
		Film film = Film.builder()
				.name("Name")
				.description("Description")
				.releaseDate(LocalDate.of(2000,12,20))
				.duration(100L)
				.build();
		Film film2 = Film.builder()
				.id(1)
				.name("Film")
				.description("01234567890123456789012345678901234567890123456789012345678901234567890123456789" +
						"01234567890123456789012345678901234567890123456789012345678901234567890123456789" +
						"01234567890123456789012345678901234567890123456789012345678901234567890123456789")
				.releaseDate(LocalDate.of(2000,12,20))
				.duration(100L)
				.build();
		filmController.addFilm(film);

		assertThrows(ValidationException.class, ()-> filmController.updateFilm(film2), "Фильм добавлен.");
		assertEquals(film.getDescription(),filmController.getFilms().get(0).getDescription(), "Описание фильма изменилось.");
	}

	@Test
	void updateReleaseDateOfFilm() throws ValidationException {
		Film film = Film.builder()
				.name("Name")
				.description("Description")
				.releaseDate(LocalDate.of(2000,12,20))
				.duration(100L)
				.build();
		Film film2 = Film.builder()
				.id(1)
				.name("Film")
				.description("Description of film")
				.releaseDate(LocalDate.of(1800,12,20))
				.duration(100L)
				.build();
		filmController.addFilm(film);

		assertThrows(ValidationException.class, ()-> filmController.updateFilm(film2), "Фильм добавлен.");
		assertEquals(film.getReleaseDate(),filmController.getFilms().get(0).getReleaseDate(), "Дата фильма изменилась.");
	}

	@Test
	void updateDurationOfFilm() throws ValidationException {
		Film film = Film.builder()
				.name("Name")
				.description("Description")
				.releaseDate(LocalDate.of(2000,12,20))
				.duration(100L)
				.build();
		Film film2 = Film.builder()
				.id(1)
				.name("Film")
				.description("Description of film")
				.releaseDate(LocalDate.of(2000,12,20))
				.duration(-100L)
				.build();
		filmController.addFilm(film);

		assertThrows(ValidationException.class, ()-> filmController.updateFilm(film2), "Фильм добавлен.");
		assertEquals(film.getDuration(),filmController.getFilms().get(0).getDuration(), "Продолжительность фильма изменилась.");
	}

	@Test
	void validCreateUser() throws ValidationException {
		User user = User.builder()
				.email("practicum@mail.ru")
				.login("login")
				.name("Practicum")
				.birthday(LocalDate.of(1987,12,20))
				.build();

		userController.addUser(user);
		assertEquals(1,user.getId(), "Номер пользователю не присвоен.");
		assertEquals(1, userController.getUsers().size(), "Пользователь не добавлен.");
	}

	@Test
	void validEmailOfUser() {
		User user = User.builder()
				.email("practicumMail.ru")
				.login("login")
				.name("Practicum")
				.birthday(LocalDate.of(1987,12,20))
				.build();

		assertThrows(ValidationException.class, ()-> userController.addUser(user), "Пользователь добавлен.");
	}

	@Test
	void validLoginOfUser() {
		User user = User.builder()
				.email("practicum@mail.ru")
				.name("Practicum")
				.birthday(LocalDate.of(1987,12,20))
				.build();

		assertThrows(ValidationException.class, ()-> userController.addUser(user), "Пользователь добавлен.");
	}

	@Test
	void validNameOfUser() {
		User user = User.builder()
				.email("practicum@mail.ru")
				.login("login")
				.birthday(LocalDate.of(1987,12,20))
				.build();

		assertEquals(user.getLogin(), user.getName(), "Имя не присвоено.");
	}

	@Test
	void validBirthdayOfUser() {
		User user = User.builder()
				.email("practicum@mail.ru")
				.login("login")
				.name("Practicum")
				.birthday(LocalDate.of(3000,12,20))
				.build();

		assertThrows(ValidationException.class, ()-> userController.addUser(user), "Пользователь добавлен.");
	}

	@Test
	void validUpdateUser() throws ValidationException {
		User user = User.builder()
				.email("practicum@mail.ru")
				.login("login")
				.name("Practicum")
				.birthday(LocalDate.of(1987,12,20))
				.build();

		User user2 = User.builder()
				.id(1)
				.email("ivan@mail.ru")
				.login("IvanLogin")
				.name("IvanPracticum")
				.birthday(LocalDate.of(2000,12,20))
				.build();

		userController.addUser(user);
		userController.updateUser(user2);

		assertEquals(1,userController.getUsers().size(), "Добавлено больше пользователей в список.");
	}

	@Test
	void validUpdateEmailOfUser() throws ValidationException {
		User user = User.builder()
				.email("practicum@mail.ru")
				.login("login")
				.name("Practicum")
				.birthday(LocalDate.of(1987,12,20))
				.build();

		User user2 = User.builder()
				.id(1)
				.email("ivanMail.ru")
				.login("IvanLogin")
				.name("IvanPracticum")
				.birthday(LocalDate.of(2000,12,20))
				.build();

		userController.addUser(user);
		assertThrows(ValidationException.class, ()-> userController.updateUser(user2), "Пользователь обновлен.");
		assertEquals(user.getEmail(),userController.getUsers().get(0).getEmail(), "Емаил изменился.");

	}

	@Test
	void validUpdateLoginOfUser() throws ValidationException {
		User user = User.builder()
				.email("practicum@mail.ru")
				.login("login")
				.name("Practicum")
				.birthday(LocalDate.of(1987,12,20))
				.build();

		User user2 = User.builder()
				.id(1)
				.email("ivan@Mail.ru")
				.login("Ivan Login")
				.name("IvanPracticum")
				.birthday(LocalDate.of(2000,12,20))
				.build();

		userController.addUser(user);
		assertThrows(ValidationException.class, ()-> userController.updateUser(user2), "Пользователь обновлен.");
		assertEquals(user.getLogin(),userController.getUsers().get(0).getLogin(), "Логин изменился.");
	}

	@Test
	void validUpdateNullNameOfUser() throws ValidationException {
		User user = User.builder()
				.email("practicum@mail.ru")
				.login("login")
				.name("Practicum")
				.birthday(LocalDate.of(1987,12,20))
				.build();

		User user2 = User.builder()
				.id(1)
				.email("ivan@Mail.ru")
				.login("IvanLogin")
				.birthday(LocalDate.of(2000,12,20))
				.build();

		userController.addUser(user);
		userController.updateUser(user2);

		assertEquals(user2.getLogin(),userController.getUsers().get(0).getName(), "Поле Имя пустое.");
	}

	@Test
	void validUpdateBirthdayOfUser() throws ValidationException {
		User user = User.builder()
				.email("practicum@mail.ru")
				.login("login")
				.name("Practicum")
				.birthday(LocalDate.of(1987,12,20))
				.build();

		User user2 = User.builder()
				.id(1)
				.email("ivan@Mail.ru")
				.login("IvanLogin")
				.name("IvanPracticum")
				.birthday(LocalDate.of(3000,12,20))
				.build();

		userController.addUser(user);
		assertThrows(ValidationException.class, ()-> userController.updateUser(user2), "Пользователь обновлен.");
		assertEquals(user.getBirthday(),userController.getUsers().get(0).getBirthday(), "Дата не изменилась.");
	}
}
