package com.example.bytebuy.Controllers;

import com.example.bytebuy.Entities.Users;
import com.example.bytebuy.Services.UserService;
import com.example.bytebuy.Utils.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        return userService.createUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user) {
        Users dbUser = userService.getUserByName(user.getUsername());
        if (new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches(user.getPassword(), dbUser.getPassword())) {
            return jwtUtil.generateToken(dbUser.getUsername(), dbUser.getRole());
        }
        throw new RuntimeException("Invalid credentials");
    }
}

