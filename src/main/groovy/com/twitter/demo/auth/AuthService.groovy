package com.twitter.demo.auth

import com.twitter.demo.users.User
import com.twitter.demo.users.UserService
import org.springframework.stereotype.Service

import java.time.Instant

@Service
class AuthService {
    private TokenService tokenService
    private UserService userService

    AuthService(TokenService tokenService) {
        this.tokenService = tokenService
    }

    def login(username, email, password) {
        String token = generateToken(username, email, password)
        def user = new User(username, email, password)

        userService.createUser(user)
        tokenService.storeToken(token)
        return token
    }

    def logout(String token) {
        tokenService.removeToken(token)
    }

    static def generateToken(username, email, password) {
        def instant = Instant.now().toString()
        return "${username}:${email}:${password}:$instant"
    }
}