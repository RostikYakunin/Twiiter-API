package com.twitter.demo.likes

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest
class LikeControllerSpec extends Specification {
    @Shared
    @AutoCleanup
    LikeService likeService

    @Shared
    @AutoCleanup
    private MockMvc mockMvc

    @Shared
    @AutoCleanup
    private ObjectMapper objectMapper

    @Shared
    @AutoCleanup
    LikeController likeController


    def setup() {
        likeService = mock(LikeService)
        likeController = new LikeController(likeService)
        mockMvc = MockMvcBuilders.standaloneSetup(likeController).build()
        objectMapper = new ObjectMapper()
    }

    def cleanup() {
        likeService = null
        likeController = null
        mockMvc = null
        objectMapper = null
    }

    def "should add a like to a post"() {
        given: "a user and a post id"
        def userId = "user123"
        def postId = "post456"

        def testLike = new Like(userId, postId)
        when(likeService.likePost(anyString(), anyString())).thenReturn(testLike)

        when: "performing POST request to /api/v1/likes/"
        def result = mockMvc.perform(
                post("/api/v1/likes/")
                        .param("userId", userId)
                        .param("postId", postId))

        then: "should return CREATED status and the like response"
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("['userId']").value(userId))
                .andExpect(jsonPath("['postId']").value(postId))
    }

    def "should remove a like from a post"() {
        given: "an existing like id"
        def likeId = "like123"
        when(likeService.unlikePost(anyString())).thenReturn(true)

        when: "performing DELETE request to /api/v1/likes/{id}"
        def result = mockMvc.perform(
                delete("/api/v1/likes/{id}", likeId))

        then: "should return OK status"
        result.andExpect(status().isOk())
    }
}
