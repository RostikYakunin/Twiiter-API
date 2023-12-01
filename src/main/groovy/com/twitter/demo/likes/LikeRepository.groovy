package com.twitter.demo.likes


import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface LikeRepository extends MongoRepository<Like, String> {

    @Query("{ 'userId' : ?0 }")
    List<Like> findLikesByUserId(String userId)
}