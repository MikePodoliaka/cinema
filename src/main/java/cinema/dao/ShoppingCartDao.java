package cinema.dao;

import cinema.model.ShoppingCart;
import cinema.model.User;

public interface ShoppingCartDao {
    ShoppingCart add(ShoppingCart shoppingCart);

    ShoppingCart getByUser(User user);

    void update(ShoppingCart shoppingCart);
 void clear (ShoppingCart shoppingCart);
}
