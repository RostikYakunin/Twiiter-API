package com.twitter.demo.posts.utils

import com.twitter.demo.posts.Post

class PostHandler {

    static def postUpdateHandler(Post existingPost, Post updatedPost) {
        if (updatedPost.authorId) {
            existingPost.authorId = updatedPost.authorId
        }

        if (updatedPost.content) {
            existingPost.content = updatedPost.content
        }

        if (updatedPost.commentsId) {
            existingPost.commentsId.addAll(updatedPost.commentsId)
        }

        return existingPost
    }
}
