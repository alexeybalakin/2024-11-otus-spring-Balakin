package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaBookRepository implements BookRepository {

    public static final String BOOK_FOR_UPDATE_NOT_FOUND = "Book for update not found";

    private final EntityManager em;

    public JpaBookRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Book> findById(long id) {
        var query = em.createQuery("select b from Book b left join fetch b.author " +
                                                              "left join fetch b.genre where b.id = :id ", Book.class);
        query.setParameter("id", id);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        var query = em.createQuery("select b from Book b " +
                                                              "left join fetch b.author " +
                                                              "left join fetch b.genre", Book.class);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        if (em.find(Book.class, book.getId()) == null) {
            throw new EntityNotFoundException(BOOK_FOR_UPDATE_NOT_FOUND);
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        var book = em.find(Book.class, id);
        if (book != null) {
            em.remove(book);
        }
    }
}
