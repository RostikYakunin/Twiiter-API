package com.twitter.demo.posts


import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("posts")
class Post {
    @Id
    String id
    def content
    def authorId
    Set<String> likesId = []
    Set<String> commentsId = []

    Post() {
    }

    Post(content, author) {
        this.content = content
        this.authorId = author
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Post post = (Post) o

        if (authorId != post.authorId) return false
        if (commentsId != post.commentsId) return false
        if (content != post.content) return false
        if (id != post.id) return false
        if (likesId != post.likesId) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (content != null ? content.hashCode() : 0)
        result = 31 * result + (authorId != null ? authorId.hashCode() : 0)
        result = 31 * result + (likesId != null ? likesId.hashCode() : 0)
        result = 31 * result + (commentsId != null ? commentsId.hashCode() : 0)
        return result
    }


    @Override
    String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", content=" + content +
                ", authorId=" + authorId +
                ", likesId=" + likesId +
                ", commentsId=" + commentsId +
                '}';
    }
}
