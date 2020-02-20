package cinema.dao;

import cinema.model.Movie;

import java.util.List;

public interface MovieDao {
    Movie add (Movie movie);
    Movie getById(Long id);
    List <Movie> getAll ();

}
