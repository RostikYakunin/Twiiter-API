package com.twitter.demo.comments


import com.twitter.demo.posts.PostService
import com.twitter.demo.users.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentService {
    private CommentRepository commentRepository
    private PostService postService
    private UserRepository userRepo

    CommentService(CommentRepository commentRepository, PostService postService, UserRepository userRepo) {
        this.commentRepository = commentRepository
        this.postService = postService
        this.userRepo = userRepo
    }

    def createComment(comment) {
        def foundPost = postService.findPostById(comment.postId)
        def foundUser = userRepo.findById(comment.userId)

        if (foundPost.isEmpty() || foundUser.isEmpty()) {
            throw new RuntimeException("Post or User not found")
        }

        def savedComment = commentRepository.save(comment)
        foundPost.get().commentsId.add(savedComment.id)
        postService.updatePost(foundPost.get().id, foundPost.get())

        return savedComment
    }

    def findCommentById(id) {
        return commentRepository.findById(id)
    }

    def deleteComment(commentId) {
        commentRepository.deleteById(commentId)
        return !commentRepository.existsById(commentId)
    }

    def findAllCommentsByPostId(id) {
        return commentRepository.findCommentsByPostId(id)
    }
}
