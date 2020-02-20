package cinema.dao.impl;

import cinema.dao.OrderDao;
import cinema.model.Order;
import cinema.model.User;

import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl implements OrderDao {
    private final SessionFactory sessionFactory;

    public OrderDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Order add(Order order) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Long orderId = (Long) session.save(order);
            transaction.commit();
            order.setId(orderId);
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Cannot create Order cart " + e);
        }
    }

    @Override
    public List<Order> getUserOrders(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<Order> query = session.createQuery("FROM Order  WHERE user = "
                    + ":user", Order.class);
            query.setParameter("user", user);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Cannot find OrderList using user " + e);
        }
    }

    @Override
    public List<Order> getAll() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<Order> criteriaQuery = session.getCriteriaBuilder()
                    .createQuery(Order.class);
            criteriaQuery.from(Order.class);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all orders " + e);
        }
    }
}