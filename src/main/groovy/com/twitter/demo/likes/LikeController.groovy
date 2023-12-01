package com.twitter.demo.likes

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/likes/")
class LikeController {
    private LikeService likeService

    LikeController(LikeService likeService) {
        this.likeService = likeService
    }

    @PostMapping
    def addLike(@RequestParam("userId") String userId, @RequestParam("postId") String postId) {
        return likeService.likePost(userId, postId)
    }

    @DeleteMapping("{id}")
    def removeLike(@PathVariable("id") String id) {
        return likeService.unlikePost(id)
    }

    @GetMapping
    def getAllLikes() {
        return likeService.findAllLikes()
    }
}
