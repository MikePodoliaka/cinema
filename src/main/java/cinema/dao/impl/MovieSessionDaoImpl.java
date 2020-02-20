package cinema.dao.impl;

import cinema.dao.MovieSessionDao;
import cinema.model.MovieSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;

@Repository
public class MovieSessionDaoImpl implements MovieSessionDao {
    @Autowired
    private final SessionFactory sessionFactory;

    public MovieSessionDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public MovieSession add(MovieSession movieSession) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Long Id = (Long) session.save(movieSession);
            transaction.commit();
            movieSession.setId(Id);
            return (movieSession);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't insert MovieSession entity", e);
        }
    }

    @Override
    public List<MovieSession> findAvailableSessions(Long movieId, LocalDate date) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<MovieSession> cq = cb.createQuery(MovieSession.class);
            Root<MovieSession> root = cq.from(MovieSession.class);

            Predicate predicateId = cb.equal(root.get("movie"), movieId);
            Predicate predicateDate = cb.between(root.get("showTime"),
                    date.atStartOfDay(), date.plusDays(1).atStartOfDay());

            cq.select(root).where(cb.and(predicateId, predicateDate));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error from findAvailableSessions", e);
        }
    }

}
