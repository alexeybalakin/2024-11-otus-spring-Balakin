package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.hw.repositories.JpaBookRepository.BOOK_FOR_UPDATE_NOT_FOUND;

@DisplayName("Репозиторий на основе Jpa для работы с книгами")
@DataJpaTest
@Import({JpaBookRepository.class})
class JpaBookRepositoryTest {

    private static final long FIRST_BOOK_ID = 1L;
    private static final int EXPECTED_NUMBER_OF_BOOKS = 3;

    @Autowired
    private JpaBookRepository repositoryJpa;
    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var actualBook = repositoryJpa.findById(FIRST_BOOK_ID);
        var expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repositoryJpa.findAll();

        assertThat(actualBooks).hasSize(EXPECTED_NUMBER_OF_BOOKS);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var author = em.find(Author.class, 1);
        var genre = em.find(Genre.class, 1);
        var expectedBook = new Book(0, "newBook", author, genre);
        var returnedBook = repositoryJpa.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, returnedBook.getId()))
                .isNotNull()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var author = em.find(Author.class, 2);
        var genre = em.find(Genre.class, 3);
        var expectedBook = new Book(FIRST_BOOK_ID, "editedBook", author, genre);

        assertThat(em.find(Book.class, FIRST_BOOK_ID))
                .isNotNull()
                .isNotEqualTo(expectedBook);

        var returnedBook = repositoryJpa.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, FIRST_BOOK_ID))
                .isNotNull()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен выбрасывать исключение, если не найдена книга для обновления ")
    @Test
    void shouldThrowEntityNotFoundExceptionThenBookNotFoundForUpdate() {
        var author = em.find(Author.class, 2);
        var genre = em.find(Genre.class, 3);
        var expectedBook = new Book(100L, "BookTitle_10500", author, genre);

        assertThat(em.find(Book.class, expectedBook.getId())).isNull();

        var exception = assertThrows(EntityNotFoundException.class,
            () -> repositoryJpa.save(expectedBook));
        assertThat(exception.getMessage()).isEqualTo(BOOK_FOR_UPDATE_NOT_FOUND);
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBook() {
        assertThat(em.find(Book.class, FIRST_BOOK_ID)).isNotNull();
        repositoryJpa.deleteById(FIRST_BOOK_ID);
        assertThat(em.find(Book.class, FIRST_BOOK_ID)).isNull();
    }
}