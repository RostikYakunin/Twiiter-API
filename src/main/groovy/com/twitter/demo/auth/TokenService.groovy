package com.twitter.demo.auth

import org.springframework.stereotype.Service

@Service
class TokenService {
    private TokenRepository tokenRepository

    TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository
    }

    def storeToken(Token token) {
        return tokenRepository.save(token)
    }

    def removeToken(tokenId) {
        tokenRepository.deleteById(tokenId)
        return !tokenRepository.existsById(tokenId)
    }
}
