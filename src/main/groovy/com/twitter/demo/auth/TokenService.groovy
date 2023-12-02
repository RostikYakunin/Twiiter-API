package com.twitter.demo.auth

import org.springframework.stereotype.Service

@Service
class TokenService {
    private TokenRepository tokenRepository

    TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository
    }

    def storeToken(token) {
        if (isExists(token)){
            throw new RuntimeException("Current token exists")
        }

        return tokenRepository.save(token)
    }

    def removeToken(token) {
        return tokenRepository.delete(token)
    }

    def isExists(token) {
        return tokenRepository.existsById(token.id)
    }
}
