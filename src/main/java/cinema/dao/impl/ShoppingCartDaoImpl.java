package cinema.dao.impl;

import cinema.dao.ShoppingCartDao;
import cinema.model.ShoppingCart;

import cinema.model.Ticket;
import cinema.model.User;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Autowired
    private final SessionFactory sessionFactory;

    public ShoppingCartDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Long itemId = (Long) session.save(shoppingCart);
            transaction.commit();
            shoppingCart.setId(itemId);
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't insert Movie entity", e);
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            ShoppingCart shoppingCart =
                    session.createQuery("FROM ShoppingCart WHERE user=:user", ShoppingCart.class)
                            .setParameter("user", user).uniqueResult();
            List<Ticket> tickets = session.createQuery("SELECT ticket FROM ShoppingCart AS sc"
                    + " JOIN sc.tickets AS ticket WHERE sc.user.id=:userId", Ticket.class)
                    .setParameter("userId", user.getId()).list();
            shoppingCart.setTickets(tickets);
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't get shopping cart by user", e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        try (Session session =sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't update ShoppingCart", e);
        }
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.clear();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't clear shoppingCart", e);
        }
    }
}