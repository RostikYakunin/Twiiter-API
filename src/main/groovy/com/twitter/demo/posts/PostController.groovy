package com.twitter.demo.posts

import com.twitter.demo.comments.CommentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/posts")
class PostController {
    private PostService postService
    private CommentService commentService

    PostController(PostService postService, CommentService commentService) {
        this.postService = postService
        this.commentService = commentService
    }

    @GetMapping("/{id}")
    def findPostById(@PathVariable("id") String id) {
        def foundPost = postService.findPostById(id)
                .orElseThrow(
                        () -> new RuntimeException("Post with id = $id not found")
                )
        return ResponseEntity.ok(foundPost)
    }

    @PostMapping("/")
    def createPost(@RequestBody Post post) {
        Post newPost = new Post(post.content, post.authorId)
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(newPost))
    }

    @PutMapping("/{id}")
    def updatePost(@PathVariable("id") String id, @RequestBody Post post) {
        return ResponseEntity.ok(postService.updatePost(id, post))
    }

    @DeleteMapping("/{id}")
    def deletePost(@PathVariable("id") String id) {
        return ResponseEntity.ok(postService.deletePost(id))
    }

    @GetMapping("/")
    def findAllPosts() {
        return ResponseEntity.ok(postService.findAllPost())
    }

    @GetMapping("/comments/{id}")
    def findAllCommentsByPostId(@PathVariable("id") String id) {
        return ResponseEntity.ok(commentService.findAllCommentsByPostId(id))
    }
}
