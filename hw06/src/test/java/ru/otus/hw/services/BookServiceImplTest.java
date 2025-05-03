package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BookServiceImpl.class)
class BookServiceImplTest {

    private static final long FIRST_BOOK_ID = 1L;
    private static final long SECOND_BOOK_ID = 2L;
    private static final long FIRST_AUTHOR_ID = 1L;
    private static final long FIRST_GENRE_ID = 1L;
    private static final String BOOK_TITLE_1 = "BookTitle_1";
    private static final String BOOK_TITLE_2 = "BookTitle_2";

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private BookService bookService;

    @Test
    public void shouldReturnCorrectBookById() {
        var expectedBook = Optional.of(new Book(FIRST_BOOK_ID, "BookTitle_1", new Author(), new Genre()));

        when(bookRepository.findById(FIRST_BOOK_ID)).thenReturn(expectedBook);

        var actualBook = bookService.findById(FIRST_BOOK_ID);

        verify(bookRepository, times(1)).findById(FIRST_BOOK_ID);
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @Test
    public void shouldReturnCorrectBooksList() {
        var expectedBook1 = new Book(FIRST_BOOK_ID, BOOK_TITLE_1, new Author(), new Genre());
        var expectedBook2 = new Book(SECOND_BOOK_ID, BOOK_TITLE_2, new Author(), new Genre());
        var expectedBookList = List.of(expectedBook1, expectedBook2);

        when(bookRepository.findAll()).thenReturn(expectedBookList);

        var actualBookList = bookService.findAll();

        verify(bookRepository, times(1)).findAll();
        assertThat(actualBookList).isEqualTo(expectedBookList);
    }

    @Test
    public void shouldDeleteBook() {
        bookService.deleteById(FIRST_BOOK_ID);

        verify(bookRepository, times(1)).deleteById(FIRST_BOOK_ID);
    }

    @Test
    void shouldSaveNewBook() {
        var author = new Author(FIRST_AUTHOR_ID, "Author_1");
        var genre = new Genre(FIRST_GENRE_ID, "Genre_1");
        var book = new Book(0, "newBook", author, genre);
        var expectedBook = new Book(4, "newBook", author, genre);

        when(authorRepository.findById(FIRST_AUTHOR_ID)).thenReturn(Optional.of(author));
        when(genreRepository.findById(FIRST_GENRE_ID)).thenReturn(Optional.of(genre));
        when(bookRepository.save(book)).thenReturn(expectedBook);

        var returnedBook = bookService.insert("newBook", FIRST_AUTHOR_ID, FIRST_GENRE_ID);

        verify(authorRepository, times(1)).findById(FIRST_AUTHOR_ID);
        verify(genreRepository, times(1)).findById(FIRST_GENRE_ID);
        verify(bookRepository, times(1)).save(book);

        assertThat(returnedBook).isEqualTo(expectedBook);
    }
}