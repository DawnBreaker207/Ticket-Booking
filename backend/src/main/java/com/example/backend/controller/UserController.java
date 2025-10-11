package com.example.backend.controller;

import com.example.backend.config.response.ResponseObject;
import com.example.backend.model.User;
import com.example.backend.service.Impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
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


}
