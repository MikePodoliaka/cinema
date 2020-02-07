package cinema.service.impl;

import cinema.dao.ShoppingCartDao;
import cinema.dao.TicketDao;
import cinema.lib.Inject;
import cinema.lib.Service;
import cinema.model.MovieSession;
import cinema.model.ShoppingCart;
import cinema.model.Ticket;
import cinema.model.User;
import cinema.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private static ShoppingCartDao shoppingCartDao;
    @Inject
    private static TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setCinemaHall(movieSession.getCinemaHall());
        ticket.setMovie(movieSession.getMovie());
        ticket.setShowTime(movieSession.getShowTime());

        ShoppingCart shoppingCart = shoppingCartDao.getByUser(user);
        shoppingCart.getTickets().add(ticket);
        ticketDao.add(ticket);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user);
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartDao.add(shoppingCart);

    }
}
