package com.twitter.demo.auth


import com.twitter.demo.users.UserService
import org.springframework.stereotype.Service

import java.time.Instant

@Service
class AuthService {
    private TokenService tokenService
    private UserService userService

    AuthService(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService
        this.userService = userService
    }

    def login(user) {
        String token = generateToken(user.username, user.email, user.password)

        userService.createUser(user)
        return tokenService.storeToken(new Token(token))
    }

    def logout(tokenId) {
        tokenService.removeToken(tokenId)
    }

    static def generateToken(username, email, password) {
        def instant = Instant.now().toString()
        return "${username}:${email}:${password}:$instant"
    }
}