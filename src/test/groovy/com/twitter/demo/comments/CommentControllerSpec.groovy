package com.twitter.demo.comments

import com.fasterxml.jackson.databind.ObjectMapper
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
class CommentControllerSpec extends Specification {
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
    CommentController commentController


    def setup() {
        commentService = mock(CommentService)
        commentController = new CommentController(commentService)
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build()
        objectMapper = new ObjectMapper()
    }

    def cleanup() {
        commentService = null
        commentController = null
        mockMvc = null
        objectMapper = null
    }

    def "should find comment by id"() {
        given: "an existing comment id"
        def newComment = new Comment("1", "1", "Test Comment")
        newComment.id = "1"

        when(commentService.findCommentById(anyString())).thenReturn(Optional.of(newComment))

        when: "performing GET request to /api/v1/comments/{id}"
        def result = mockMvc.perform(
                get("/api/v1/comments/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON))

        then: "should return OK status and the comment"
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("['id']").value("1"))
                .andExpect(jsonPath("['userId']").value("1"))
                .andExpect(jsonPath("['postId']").value("1"))
                .andExpect(jsonPath("['text']").value("Test Comment"))
    }

    def "should add a new comment"() {
        given: "a new comment"
        def newComment = new Comment("1", "1", "New Comment")
        newComment.id = "1"

        when(commentService.createComment(any(Comment))).thenReturn(newComment)

        when: "performing POST request to /api/v1/comments/"
        def result = mockMvc.perform(
                post("/api/v1/comments/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newComment)))

        then: "should return CREATED status and the new comment"
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("['id']").exists())
                .andExpect(jsonPath("['userId']").value("1"))
                .andExpect(jsonPath("['postId']").value("1"))
                .andExpect(jsonPath("['text']").value("New Comment"))
    }

    def "should remove a comment by id"() {
        given: "an existing comment id"
        def commentId = "1"
        when(commentService.deleteComment(anyString())).thenReturn(true)

        when: "performing DELETE request to /api/v1/comments/{id}"
        def result = mockMvc.perform(
                delete("/api/v1/comments/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON))

        then: "should return OK status"
        result.andExpect(status().isOk())
    }
}
