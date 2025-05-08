package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaCommentRepository implements CommentRepository {

    private final EntityManager em;

    public JpaCommentRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Comment> findById(long id) {
        var query = em.createQuery("select c from Comment c left join fetch c.book b " +
                                                                "left join fetch b.author left join fetch b.genre " +
                                                                  "where c.id = :id ", Comment.class);
        query.setParameter("id", id);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Comment> findByBookId(long bookId) {
        var query = em.createQuery("select c from Comment c left join fetch c.book b " +
                                                                "left join fetch b.author left join fetch b.genre " +
                                                                  "where b.id = :bookId", Comment.class);
        query.setParameter("bookId" , bookId);
        return query.getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
            return comment;
        }
        return em.merge(comment);
    }

    @Override
    public void deleteById(long id) {
        var comment = em.find(Comment.class, id);
        if (comment == null) {
            throw new EntityNotFoundException("Comment for delete not found");
        }
        em.remove(comment);
    }
}
