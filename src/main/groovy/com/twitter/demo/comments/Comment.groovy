package com.twitter.demo.comments

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "comments")
class Comment {
    @Id
    String id
    String userId
    String postId
    def text

    Comment() {
    }

    Comment(userId, postId, text) {
        this.userId = userId
        this.postId = postId
        this.text = text
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Comment comment = (Comment) o

        if (id != comment.id) return false
        if (postId != comment.postId) return false
        if (text != comment.text) return false
        if (userId != comment.userId) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (userId != null ? userId.hashCode() : 0)
        result = 31 * result + (postId != null ? postId.hashCode() : 0)
        result = 31 * result + (text != null ? text.hashCode() : 0)
        return result
    }

    @Override
    String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", postId=" + postId +
                ", text=" + text +
                '}'
    }
}
