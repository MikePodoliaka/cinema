package cinema.controllers;

import cinema.model.CinemaHall;
import cinema.model.dto.CinemaHallRequestDto;
import cinema.model.dto.CinemaHallResponseDto;
import cinema.service.CinemaHallService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cinema-halls")
public class CinemaHallController {
    private final CinemaHallService cinemaHallService;

    public CinemaHallController(CinemaHallService cinemaHallService) {
        this.cinemaHallService = cinemaHallService;
    }

    @GetMapping(value = "/all")
    public List<CinemaHallResponseDto> getAllCinemaHalls() {
        return cinemaHallService.getAll().stream()
                .map(this::transferCinemaHallToDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/add")
    public CinemaHall addCinemaHall(@RequestBody CinemaHallRequestDto cinemaHallRequestDto) {
        CinemaHall newCinemaHall = new CinemaHall();
        newCinemaHall.setDescription(cinemaHallRequestDto.getDescription());
        newCinemaHall.setCapacity(cinemaHallRequestDto.getCapacity());
        return cinemaHallService.add(newCinemaHall);
    }

    private CinemaHallResponseDto transferCinemaHallToDto(CinemaHall cinemaHall) {
        CinemaHallResponseDto cinemaHallResponseDto = new CinemaHallResponseDto();
        cinemaHallResponseDto.setDescription(cinemaHall.getDescription());
        return cinemaHallResponseDto;
    }
}