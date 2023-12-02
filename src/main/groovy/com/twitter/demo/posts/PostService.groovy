package com.twitter.demo.posts

import com.twitter.demo.comments.CommentRepository
import com.twitter.demo.posts.utils.PostHandler
import com.twitter.demo.users.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostService {
    private final PostRepository postRepository
    private final CommentRepository commentRepository
    private final UserService userService

    PostService(PostRepository postRepository, UserService userService, CommentRepository commentRepository) {
        this.postRepository = postRepository
        this.userService = userService
        this.commentRepository = commentRepository
    }

    def createPost(post) {
        def userById = userService.findUserById(post.authorId)

        if (userById.isEmpty()) {
            throw new RuntimeException("Can not find user by this id= $post.authorId")
        }

        return postRepository.save(post)
    }

    def findPostById(id) {
        return postRepository.findById(id)
    }

    def updatePost(postId, updatedPost) {
        def existingPost = postRepository.findById(postId).orElseThrow(
                () -> new RuntimeException("Post with id= $postId not foud")
        )

        def handledPost = PostHandler.postUpdateHandler(existingPost, updatedPost)
        return postRepository.save(handledPost)
    }

    def deletePost(postId) {
        postRepository.deleteById(postId)
        return !postRepository.existsById(postId)
    }

    def findAllPost() {
        return postRepository.findAll()
    }
}
