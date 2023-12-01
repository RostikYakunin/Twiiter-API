package com.twitter.demo.posts

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository extends MongoRepository<Post, String>{

}