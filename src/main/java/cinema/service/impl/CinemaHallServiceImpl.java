package cinema.service.impl;

import cinema.dao.CinemaHallDao;
import cinema.lib.Inject;
import cinema.lib.Service;
import cinema.model.CinemaHall;
import cinema.service.CinemaHallService;

import java.util.List;

@Service
public class CinemaHallServiceImpl implements CinemaHallService {
    @Inject
    private static CinemaHallDao cinemaHallDao;

    @Override
    public CinemaHall add(CinemaHall cinemaHall) {
        return cinemaHallDao.add(cinemaHall);
    }

    @Override
    public List<CinemaHall> getAll() {
        return cinemaHallDao.getAll();
    }
}
