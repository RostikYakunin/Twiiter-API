package com.twitter.demo.likes


import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "likes")
class Like {

    @Id
    String id
    def userId
    def postId

    Like() {
    }

    Like(String userId, String postId) {
        this.userId = userId
        this.postId = postId
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Like like = (Like) o

        if (id != like.id) return false
        if (postId != like.postId) return false
        if (userId != like.userId) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (userId != null ? userId.hashCode() : 0)
        result = 31 * result + (postId != null ? postId.hashCode() : 0)
        return result
    }


    @Override
    String toString() {
        return "Like{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", postId=" + postId +
                '}'
    }
}
