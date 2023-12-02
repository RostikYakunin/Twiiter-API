package com.twitter.demo.auth

import com.twitter.demo.users.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {
    private AuthService authService

    AuthController(AuthService authService) {
        this.authService = authService
    }

    @PostMapping("/login")
    def login(@RequestBody User user) {
        String token = authService.login(user.getUsername(), user.email, user.getPassword())
        return ResponseEntity.ok(token)
    }

    @PostMapping("/logout")
    def logout(@RequestHeader("Authorization") String token) {
        authService.logout(token)
        return ResponseEntity.ok("Logged out successfully")
    }
}

