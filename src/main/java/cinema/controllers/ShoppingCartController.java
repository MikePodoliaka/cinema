package cinema.controllers;

import cinema.model.MovieSession;
import cinema.model.ShoppingCart;
import cinema.model.Ticket;
import cinema.model.User;
import cinema.model.dto.MovieSessionRequestDto;
import cinema.model.dto.TicketResponseDto;
import cinema.service.CinemaHallService;
import cinema.service.MovieService;
import cinema.service.ShoppingCartService;
import cinema.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping-carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CinemaHallService cinemaHallService;
    private final MovieService movieService;
    private final UserService userService;

    public ShoppingCartController(ShoppingCartService shoppingCartService,
                                  MovieService movieService, CinemaHallService cinemaHallService,
                                  UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.cinemaHallService = cinemaHallService;
        this.movieService = movieService;
        this.userService = userService;
    }

    @PostMapping(value = "/add-movie-session")
    public ShoppingCart addMovieSessionToCart(
            @RequestBody MovieSessionRequestDto movieSessionRequestDto,
            @RequestParam Long userId) {
        MovieSession movieSession = transferDtoToMovieSession(movieSessionRequestDto);
        User user = userService.getById(userId);
        shoppingCartService.addSession(movieSession, user);
        return shoppingCartService.getByUser(user);
    }

    @GetMapping(value = "/by-user")
    public List<TicketResponseDto> getByUser(@RequestParam Long userId) {
        ShoppingCart shoppingCart = shoppingCartService.getByUser(userService.getById(userId));
        return shoppingCart.getTickets().stream()
                .map(this::transferTicketToDto)
                .collect(Collectors.toList());
    }

    private TicketResponseDto transferTicketToDto(Ticket ticket) {
        TicketResponseDto ticketResponseDto = new TicketResponseDto();
        ticketResponseDto.setCinemaHallDescription(ticket.getCinemaHall().getDescription());
        ticketResponseDto.setMovieTitle(ticket.getMovie().getTitle());
        ticketResponseDto.setShowTime(ticket.getShowTime().toString());
        ticketResponseDto.setUserEmail(ticket.getUser().getEmail());
        return ticketResponseDto;
    }

    private MovieSession transferDtoToMovieSession(MovieSessionRequestDto movieSessionRequestDto) {
        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(movieService.getById(movieSessionRequestDto.getMovieId()));
        movieSession.setCinemaHall(cinemaHallService
                .getById(movieSessionRequestDto.getCinemaHallId()));
        LocalDateTime localDateTime = LocalDateTime.parse(movieSessionRequestDto.getShowTime());
        movieSession.setShowTime(localDateTime);
        return movieSession;
    }
}