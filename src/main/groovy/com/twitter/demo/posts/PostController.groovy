package com.twitter.demo.posts

import com.twitter.demo.comments.CommentService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/posts/")
class PostController {
    private PostService postService
    private CommentService commentService

    PostController(PostService postService, CommentService commentService) {
        this.postService = postService
        this.commentService = commentService
    }

    @GetMapping("{id}")
    def findPostById(@PathVariable("id") String id){
        return postService.findPostById(id)
    }

    @PostMapping
    def createPost(@RequestBody Post post) {
        Post newPost = new Post(post.content, post.authorId)
        return postService.createPost(newPost)
    }

    @PutMapping("{id}")
    Post updatePost(@PathVariable("id") String id, @RequestBody Post post) {
        return postService.updatePost(id, post)
    }

    @DeleteMapping("{id}")
    boolean deletePost(@PathVariable("id") String id) {
        return postService.deletePost(id)
    }

    @GetMapping
    def findAllPosts(){
        return postService.findAllPost()
    }

    @GetMapping("comments/{id}")
    def findAllCommentsByPostId(@PathVariable("id") String id){
        return commentService.findAllCommentsByPostId(id)
    }
}
