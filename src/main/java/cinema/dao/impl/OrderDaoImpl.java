package cinema.dao.impl;

import cinema.dao.OrderDao;
import cinema.lib.Dao;
import cinema.model.Order;
import cinema.model.Ticket;
import cinema.model.User;
import cinema.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order add(Order order) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Long orderId = (Long) session.save(order);
            transaction.commit();
            order.setId(orderId);
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't insert order", e);
        }
    }

    @Override
    public List<Order> getOrderHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
            Root<Order> root = criteriaQuery.from(Order.class);

            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("user"), user));
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error from getOrderHistory", e);
        }

    }
}
