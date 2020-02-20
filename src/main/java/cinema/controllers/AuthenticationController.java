package cinema.controllers;

import cinema.model.User;
import cinema.model.dto.UserRequestDto;
import cinema.service.AuthenticationService;

import javax.security.sasl.AuthenticationException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody UserRequestDto userRequestDto) {
        return authenticationService.register(userRequestDto.getEmail(),
                userRequestDto.getPassword());
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            authenticationService.login(userRequestDto.getEmail(), userRequestDto.getPassword());
        } catch (AuthenticationException e) {
            LOGGER.warn("Unsuccessful login", e);
            return "Wrong entered data";
        }
        return "Login successful";
    }
}