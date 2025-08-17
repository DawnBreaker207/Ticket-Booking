package com.example.backend.controller;

import java.util.List;

import com.example.backend.response.ResponseObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.UserDTO;
import com.example.backend.model.User;
import com.example.backend.service.Impl.UserServiceImpl;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Operations related to user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseObject<List<User>> getAll() {
        return new ResponseObject<>(HttpStatus.OK, "Success", userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseObject<User> getOne(@PathVariable Long id) {
        return new ResponseObject<>(HttpStatus.OK, "Success", userService.findOne(id));
    }

    @GetMapping("/email/{email}")
    public ResponseObject<User> getEmail(@PathVariable String email) {
        return new ResponseObject<>(HttpStatus.OK, "Success", userService.findByEmail(email));
    }

    @PostMapping("/register")
    public ResponseObject<User> register(@RequestBody UserDTO newUser) {
        return new ResponseObject<>(HttpStatus.OK, "Success", userService.registerUser(newUser));
    }

    @PostMapping("/login")
    public ResponseObject<User> login(@RequestBody UserDTO user) {
        return new ResponseObject<>(HttpStatus.OK, "Success", userService.login(user));
    }

}
