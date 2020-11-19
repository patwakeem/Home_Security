package com.interactivehome.main_service.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interactivehome.main_service.model.user.dto.UserDto;
import com.interactivehome.main_service.model.user.entity.User;
import com.interactivehome.main_service.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<String> RegisterUser(@RequestBody UserDto dto) {
        userService.registerUser(dto);
        String body = "";
        try {
            ObjectMapper map = new ObjectMapper();
            body = map.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            System.out.println("Json Processing Exception: " + e.getMessage());
            return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(body);
    }

    @GetMapping("/user/{userId}")
    public User GetUser(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/user/{userId}")
    public User ModifyUser(@PathVariable Integer userId,
                           @RequestBody UserDto dto) {
        return userService.modifyUser(userId, dto);
    }

    @GetMapping("/user")
    public List<User> GetAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/user/{userId}")
    public Boolean DeleteUser(@PathVariable Integer userId) {
        return userService.deleteUser(userId);
    }
}
