package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами")
@DataJpaTest
@Import({JpaGenreRepository.class})
class JpaGenreRepositoryTest {

    private static final long FIRST_GENRE_ID = 1L;
    private static final int EXPECTED_NUMBER_OF_GENRES = 3;

    @Autowired
    private JpaGenreRepository repositoryJpa;
    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenreById() {
        var actualGenre = repositoryJpa.findById(FIRST_GENRE_ID);
        var expectedGenre = em.find(Genre.class, FIRST_GENRE_ID);
        assertThat(actualGenre).isPresent()
                .get()
                .isEqualTo(expectedGenre);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = repositoryJpa.findAll();

        assertThat(actualGenres).hasSize(EXPECTED_NUMBER_OF_GENRES);
    }
}