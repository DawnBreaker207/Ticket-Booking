package com.example.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.LoginResponseDTO;
import com.example.backend.model.User;
import com.example.backend.service.Impl.UserServiceImpl;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
	this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAll() {
	return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getOne(@PathVariable Long id) {
	return ResponseEntity.status(HttpStatus.OK).body(userService.findOne(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getEmail(@PathVariable String email) {
	return ResponseEntity.status(HttpStatus.OK).body(userService.findByEmail(email));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User newUser) {
	return ResponseEntity.status(HttpStatus.OK).body(userService.registerUser(newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody User loginRequest) {
	User user = userService.findByEmail(loginRequest.getEmail());
	if (user == null) {
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
		    .body(new LoginResponseDTO("Invalid credentials", null, null));
	}

	if (userService.isPasswordMatch(loginRequest.getPassword(), user.getPassword())) {
	    LoginResponseDTO response = new LoginResponseDTO("Login successful", user.getName(), user.getId());
	    return ResponseEntity.ok().body(response);
	} else {
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
		    .body(new LoginResponseDTO("Invalid credentials", null, null));
	}
    }

}
