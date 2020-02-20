package cinema.controllers;

import cinema.model.User;
import cinema.model.dto.UserRequestDto;
import cinema.model.dto.UserResponseDto;
import cinema.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/add")
    public User addUser(@RequestBody UserRequestDto userRequestDto) {
        User newUser = new User();
        newUser.setEmail(userRequestDto.getEmail());
        newUser.setPassword(userRequestDto.getPassword());
        return userService.add(newUser);
    }

    @GetMapping(value = "/by-email")
    public UserResponseDto getAll(@RequestParam String email) {
        User user = userService.findByEmail(email);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(user.getEmail());
        return userResponseDto;
    }
}