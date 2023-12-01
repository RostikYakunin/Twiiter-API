package com.twitter.demo.comments

import org.springframework.web.bind.annotation.*

import java.awt.List

@RestController
@RequestMapping("/api/v1/comments/")
class CommentController {
    private CommentService commentService

    CommentController(CommentService commentService) {
        this.commentService = commentService
    }

    @GetMapping("{id}")
    def findCommentById(@PathVariable("id") id){
        return commentService.findCommentById(id).orElseThrow()
    }

    @PostMapping
    def addComment(@RequestBody Comment comment) {
        Comment newComment = new Comment(comment.userId, comment.postId, comment.text)
        return commentService.createComment(newComment)
    }

    @DeleteMapping("{id}")
    def removeComment(@PathVariable("id") id) {
        return commentService.deleteComment(id)
    }

    @GetMapping
    def findAllComments () {
        return commentService.findAllComments()
    }
}
