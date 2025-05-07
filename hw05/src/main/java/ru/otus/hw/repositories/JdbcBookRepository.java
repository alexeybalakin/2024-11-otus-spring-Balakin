package ru.otus.hw.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {

    public static final String BOOK_FOR_UPDATE_NOT_FOUND = "Book for update not found";

    private final NamedParameterJdbcOperations jdbc;

    public JdbcBookRepository(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Map.of("id", id);
        try {
            var result = jdbc.queryForObject(
                "select b.id book_id, b.title, a.id author_id, a.full_name, g.id genre_id, g.name from books b " +
                    "left join authors a on b.author_id = a.id " +
                    "left join genres g on b.genre_id = g.id " +
                    "where b.id = :id", params, new BookRowMapper());
            return Optional.of(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        return jdbc.query("select b.id book_id, b.title, a.id author_id, a.full_name, g.id genre_id, g.name " +
                                "from books b " +
                                "left join authors a on b.author_id = a.id " +
                                "left join genres g on b.genre_id = g.id", new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Map.of("id", id);
        jdbc.update("delete from books where id = :id", params);
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource()
            .addValue("title", book.getTitle())
            .addValue("author_id", book.getAuthor().getId())
            .addValue("genre_id", book.getGenre().getId());

        jdbc.update("insert into books (title, author_id, genre_id) values (:title, :author_id, :genre_id)",
            params, keyHolder);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        var params = new MapSqlParameterSource()
            .addValue("id", book.getId())
            .addValue("title", book.getTitle())
            .addValue("author_id", book.getAuthor().getId())
            .addValue("genre_id", book.getGenre().getId());

        var rows = jdbc.update("update books set title = :title, author_id = :author_id, genre_id = :genre_id " +
                                          "where id = :id", params);
        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        if (rows == 0) {
            throw new EntityNotFoundException(BOOK_FOR_UPDATE_NOT_FOUND);
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var bookId = rs.getLong("book_id");
            var title = rs.getString("title");
            var authorId = rs.getLong("author_id");
            var fullName = rs.getString("full_name");
            var genreId = rs.getLong("genre_id");
            var name = rs.getString("name");

            var author = new Author(authorId, fullName);
            var genre = new Genre(genreId, name);

            return new Book(bookId, title, author, genre);
        }
    }
}
