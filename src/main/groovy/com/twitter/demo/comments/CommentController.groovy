package com.twitter.demo.comments

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/comments")
class CommentController {
    private CommentService commentService

    CommentController(CommentService commentService) {
        this.commentService = commentService
    }

    @GetMapping("/{id}")
    def findCommentById(@PathVariable("id") id) {
        def foundComment = commentService.findCommentById(id).orElseThrow(
                () -> new RuntimeException("Comment with id= $id not found")
        )
        return ResponseEntity.ok(foundComment)
    }

    @PostMapping("/")
    def addComment(@RequestBody Comment comment) {
        Comment newComment = new Comment(comment.userId, comment.postId, comment.text)
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(newComment))
    }

    @DeleteMapping("/{id}")
    def removeComment(@PathVariable("id") id) {
        return ResponseEntity.ok(commentService.deleteComment(id))
    }

    @GetMapping("/")
    def findAllComments() {
        return ResponseEntity.ok(commentService.findAllComments())
    }
}
