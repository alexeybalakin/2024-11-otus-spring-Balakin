package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями")
@DataJpaTest
@Import({JpaCommentRepository.class})
class JpaCommentRepositoryTest {

    private static final long FIRST_COMMENT_ID = 1L;
    private static final int EXPECTED_NUMBER_OF_COMMENTS = 2;

    @Autowired
    private JpaCommentRepository repositoryJpa;
    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var actualComment = repositoryJpa.findById(FIRST_COMMENT_ID);
        var expectedComment = em.find(Comment.class, FIRST_COMMENT_ID);
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список комментариев к книге по ее id")
    @Test
    void shouldReturnCorrectCommentsList() {
        var actualComments = repositoryJpa.findByBookId(1);

        assertThat(actualComments).hasSize(EXPECTED_NUMBER_OF_COMMENTS);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var book = em.find(Book.class, 1);
        var expectedComment = new Comment(0, "newComment", book);
        var returnedComment = repositoryJpa.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(em.find(Comment.class, returnedComment.getId()))
                .isNotNull()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var book = em.find(Book.class, 2);
        var expectedComment = new Comment(FIRST_COMMENT_ID, "editedComment", book);

        assertThat(em.find(Comment.class, FIRST_COMMENT_ID))
                .isNotNull()
                .isNotEqualTo(expectedComment);

        var returnedBook = repositoryJpa.save(expectedComment);
        assertThat(returnedBook).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(em.find(Comment.class, FIRST_COMMENT_ID))
                .isNotNull()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    void shouldDeleteComment() {
        assertThat(em.find(Comment.class, FIRST_COMMENT_ID)).isNotNull();
        repositoryJpa.deleteById(FIRST_COMMENT_ID);
        assertThat(em.find(Comment.class, FIRST_COMMENT_ID)).isNull();
    }
}