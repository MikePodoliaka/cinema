package cinema.controller;

import cinema.model.Movie;
import cinema.model.User;
import cinema.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @PostMapping("/add")
    public String add(Movie movie) {

        Movie newMovie = new Movie();
        newMovie.setTitle("movie.getTitle()");
        newMovie.setDescription("movie.getDescription()");
        movieService.add(newMovie);
        return "add ok";
    }

    @GetMapping("/getAll")
    public List<Movie> getAll() {
        List<Movie> movies = new ArrayList<>();
        movieService.getAll().forEach(mov ->movies.add(new Movie(mov)));
        return movies;
    }
}
