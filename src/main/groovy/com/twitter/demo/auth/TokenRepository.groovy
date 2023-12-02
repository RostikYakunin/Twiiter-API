package com.twitter.demo.auth

import org.springframework.data.mongodb.repository.MongoRepository

interface TokenRepository extends MongoRepository<Token, String>{

}