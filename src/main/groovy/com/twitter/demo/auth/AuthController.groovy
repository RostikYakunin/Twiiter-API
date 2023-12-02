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
    def login(@RequestBody user) {
        def newUser = new User(user.username, user.email, user.password)
        return ResponseEntity.ok(authService.login(newUser))
    }

    @PostMapping("/logout")
    def logout(@RequestHeader("Authorization") String tokenId) {
        def logout = authService.logout(tokenId)

        if (!logout) {
            return ResponseEntity.badRequest().body("Logout was denied")
        }

        return ResponseEntity.ok("Logged out successfully")
    }
}

