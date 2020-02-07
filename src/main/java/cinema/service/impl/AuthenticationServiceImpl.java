package cinema.service.impl;

import cinema.dao.UserDao;
import cinema.lib.Inject;
import cinema.lib.Service;
import cinema.model.User;
import cinema.service.AuthenticationService;
import cinema.service.ShoppingCartService;
import cinema.service.UserService;
import cinema.util.HashUtil;

import javax.naming.AuthenticationException;
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
@Inject
    private static UserService userService;
@Inject
private static ShoppingCartService shoppingCartService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        User user = userService.findByEmail(email);
        if (user == null || !user.getPassword()
                .equals(HashUtil.hashPassword(password, user.getSalt()))) {
            throw new AuthenticationException("Invalid email or password");
        }
        return user;
    }

    @Override
    public User register(String email, String password) {
          User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        User user=userService.add(newUser);
shoppingCartService.registerNewShoppingCart(user);
        return user;
    }
}
