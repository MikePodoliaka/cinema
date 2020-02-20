package cinema.dao.impl;

import cinema.dao.UserDao;
import cinema.model.User;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {
    private final SessionFactory sessionFactory;

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User add(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Long userId = (Long) session.save(user);
            transaction.commit();
            user.setId(userId);
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't insert user entity", e);
        }
    }

    @Override
    public User getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Can't get user by id", e);
        }
    }

    @Override
    public User findByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE email =: e", User.class);
            query.setParameter("e", email);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Cant' find user by email " + e);
        }
    }
}