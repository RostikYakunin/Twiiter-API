package com.twitter.demo.posts

import com.fasterxml.jackson.databind.ObjectMapper
import com.twitter.demo.comments.Comment
import com.twitter.demo.comments.CommentService
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.anyString
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest
class PostControllerSpec extends Specification {
    @Shared
    @AutoCleanup
    PostService postService

    @Shared
    @AutoCleanup
    CommentService commentService

    @Shared
    @AutoCleanup
    private MockMvc mockMvc

    @Shared
    @AutoCleanup
    private ObjectMapper objectMapper

    @Shared
    @AutoCleanup
    PostController postController


    def setup() {
        postService = mock(PostService)
        commentService = mock(CommentService)
        postController = new PostController(postService, commentService)
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build()
        objectMapper = new ObjectMapper()
    }

    def cleanup() {
        postService = null
        commentService = null
        postController = null
        mockMvc = null
        objectMapper = null
    }

    def "should find post by id"() {
        given: "an existing post id"
        def postId = "1"
        def foundPost = new Post("Test Content", "author123")
        foundPost.id = postId

        when(postService.findPostById(postId)).thenReturn(Optional.of(foundPost))

        when: "performing GET request to /{id}"
        def result = mockMvc.perform(
                get("/api/v1/posts/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON))

        then: "should return OK status and the post"
        result.andExpect(status().isOk())
                .andExpect(jsonPath("['id']").value(postId))
                .andExpect(jsonPath("['content']").value("Test Content"))
                .andExpect(jsonPath("['authorId']").value("author123"))
    }

    def "should create a new post"() {
        given: "a post request"
        def createdPost = new Post(id: "2", content: "New Content", authorId: "author456")
        when(postService.createPost(any())).thenReturn(createdPost)

        when: "performing POST request to '/'"
        def result = mockMvc.perform(
                post("/api/v1/posts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdPost)))

        then: "should return CREATED status and the created post"
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("['id']").value("2"))
                .andExpect(jsonPath("['content']").value("New Content"))
                .andExpect(jsonPath("['authorId']").value("author456"))
    }

    def "should update an existing post"() {
        given: "an existing post id and an updated post"
        def updatedPost = new Post(id: "1", content: "Updated Content", authorId: "author789")
        when(postService.updatePost(anyString(), any())).thenReturn(updatedPost)

        when: "performing PUT request to /{id}"
        def result = mockMvc.perform(
                put("/api/v1/posts/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPost)))

        then: "should return OK status and the updated post"
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("['id']").value("1"))
                .andExpect(jsonPath("['content']").value("Updated Content"))
                .andExpect(jsonPath("['authorId']").value("author789"))
    }

    def "should delete an existing post"() {
        given: "an existing post id"
        def postId = "4"
        when(postService.deletePost(postId)).thenReturn(true)

        when: "performing DELETE request to /{id}"
        def result = mockMvc.perform(
                delete("/api/v1/posts/{id}", postId))

        then: "should return OK status"
        result.andExpect(status().isOk())
    }

    def "should find all comments by post id"() {
        given: "an existing post id"

        def comments = [
                new Comment(id: "1", userId: "11", postId: "1", text: "Comment 1"),
                new Comment(id: "2", userId: "22", postId: "1", text: "Comment 2")
        ]
        when(commentService.findAllCommentsByPostId("1")).thenReturn(comments)

        when: "performing GET request to /comments/{id}"
        def result = mockMvc.perform(
                get("/api/v1/posts/comments/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))

        then: "should return OK status and the comments"
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(/$/).isArray())
                .andExpect(jsonPath(/$[0].id/).value("1"))
                .andExpect(jsonPath(/$[0].userId/).value("11"))
                .andExpect(jsonPath(/$[0].postId/).value("1"))
                .andExpect(jsonPath(/$[0].text/).value("Comment 1"))
                .andExpect(jsonPath(/$[1].id/).value("2"))
                .andExpect(jsonPath(/$[1].userId/).value("22"))
                .andExpect(jsonPath(/$[1].postId/).value("1"))
                .andExpect(jsonPath(/$[1].text/).value("Comment 2")
                )
    }
}
