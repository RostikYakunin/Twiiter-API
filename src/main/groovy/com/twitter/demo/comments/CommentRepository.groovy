package com.twitter.demo.comments

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository extends MongoRepository<Comment, String> {

    @Query("{ 'userId' : ?0 }")
    List<Comment> findCommentsByUserId(String id)

    @Query("{ 'postId' : ?0 }")
    List<Comment> findCommentsByPostId(String postId);
}