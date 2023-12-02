package com.twitter.demo.likes

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/likes")
class LikeController {
    private LikeService likeService

    LikeController(LikeService likeService) {
        this.likeService = likeService
    }

    @PostMapping("/")
    def addLike(@RequestParam("userId") String userId, @RequestParam("postId") String postId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(likeService.likePost(userId, postId))
    }

    @DeleteMapping("/{id}")
    def removeLike(@PathVariable("id") String id) {
        return ResponseEntity.ok(likeService.unlikePost(id))
    }
}
