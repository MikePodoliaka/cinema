package cinema.controllers;

import cinema.model.MovieSession;
import cinema.model.dto.MovieSessionRequestDto;
import cinema.model.dto.MovieSessionResponseDto;
import cinema.service.CinemaHallService;
import cinema.service.MovieService;
import cinema.service.MovieSessionService;

import java.time.LocalDate;
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
@RequestMapping("/movie-sessions")
public class MovieSessionController {
    private final MovieSessionService movieSessionService;
    private final MovieService movieService;
    private final CinemaHallService cinemaHallService;

    public MovieSessionController(
            MovieSessionService movieSessionService, MovieService movieService,
            CinemaHallService cinemaHallService) {
        this.movieSessionService = movieSessionService;
        this.movieService = movieService;
        this.cinemaHallService = cinemaHallService;
    }

    @GetMapping(value = "/all")
    public List<MovieSessionResponseDto> getAllMovieSessions(
            @RequestParam Long movieId, @RequestParam LocalDate localDate) {
        return movieSessionService.findAvailableSessions(movieId, localDate).stream()
                .map(this::transferMovieSessionToDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/add")
    public MovieSession addMovieSessions(
            @RequestBody MovieSessionRequestDto movieSessionRequestDto) {
        return movieSessionService.add(transferDtoToMovieSession(movieSessionRequestDto));
    }

    private MovieSessionResponseDto transferMovieSessionToDto(MovieSession movieSession) {
        MovieSessionResponseDto movieSessionResponseDto = new MovieSessionResponseDto();
        movieSessionResponseDto.setCinemaHallDescription(
                movieSession.getCinemaHall().getDescription());
        movieSessionResponseDto.setMovieTitle(movieSession.getMovie().getTitle());
        movieSessionResponseDto.setShowTime(movieSession.getShowTime().toString());
        return movieSessionResponseDto;
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