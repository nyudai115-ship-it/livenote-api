package com.example.livenote.controller;

import com.example.livenote.dto.UserSignupRequest;
import com.example.livenote.dto.UserLoginRequest;
import com.example.livenote.entity.User;
import com.example.livenote.service.UserService;
import com.example.livenote.exception.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupRequest request) {
        try {
            User created = userService.signup(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(created);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        boolean success = userService.login(request.getUsername(), request.getPassword());
        if (success) {
            return ResponseEntity.ok("ログイン成功");
        } else {
            return ResponseEntity.status(401).body("ユーザー名またはパスワードが違います");
        }
    }
}
