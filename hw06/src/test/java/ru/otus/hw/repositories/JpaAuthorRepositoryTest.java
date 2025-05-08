package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами")
@DataJpaTest
@Import({JpaAuthorRepository.class})
class JpaAuthorRepositoryTest {

    private static final long FIRST_AUTHOR_ID = 1L;
    private static final int EXPECTED_NUMBER_OF_AUTHORS = 3;

    @Autowired
    private JpaAuthorRepository repositoryJpa;
    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        var actualAuthor = repositoryJpa.findById(FIRST_AUTHOR_ID);
        var expectedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualAuthors = repositoryJpa.findAll();

        assertThat(actualAuthors).hasSize(EXPECTED_NUMBER_OF_AUTHORS);
    }
}