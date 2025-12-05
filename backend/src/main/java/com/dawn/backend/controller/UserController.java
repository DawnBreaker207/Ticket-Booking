package com.dawn.backend.controller;

import com.dawn.backend.config.response.ResponseObject;
import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.dto.request.UserRequest;
import com.dawn.backend.dto.response.UserResponse;
import com.dawn.backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Operations related to user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseObject<ResponsePage<UserResponse>> getAll(Pageable pageable) {
        return new ResponseObject<>(HttpStatus.OK, "Success", userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseObject<UserResponse> getOne(@PathVariable Long id) {
        return new ResponseObject<>(HttpStatus.OK, "Success", userService.findOne(id));
    }

    @GetMapping("/email/{email}")
    public ResponseObject<UserResponse> getEmail(@PathVariable String email) {
        return new ResponseObject<>(HttpStatus.OK, "Success", userService.findByEmail(email));
    }

    @PutMapping("/update/{id}/status")
    public ResponseObject<UserResponse> updateStatus(@PathVariable Long id, @RequestBody Boolean status) {
        return new ResponseObject<>(HttpStatus.OK, "Success", userService.updateStatus(id, status));
    }

    @PutMapping("/update/{id}/profile")
    public ResponseObject<UserResponse> updateUserInfo(@PathVariable Long id, @RequestBody UserRequest req) {
        return new ResponseObject<>(HttpStatus.OK, "Success", userService.update(id, req));
    }
}
