package com.twitter.demo.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.twitter.demo.users.User
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.anyString
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class AuthControllerSpec extends Specification {
    @Shared
    @AutoCleanup
    AuthService authService

    @Shared
    @AutoCleanup
    private MockMvc mockMvc

    @Shared
    @AutoCleanup
    private ObjectMapper objectMapper

    @Shared
    @AutoCleanup
    AuthController authController

    def setup() {
        authService = mock(AuthService)
        authController = new AuthController(authService)
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build()
        objectMapper = new ObjectMapper()
    }

    def cleanup() {
        mockMvc = null
        objectMapper = null
        authController = null
        authService = null
    }

    def "should successfully log in"() {
        given: "a user"
        def user = new User(username: "testUser", email: "test@example.com", password: "password")

        when: "performing login"
        def result = mockMvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()


        then: "login is successful"
        result.response.status == 200
        result.response.contentAsString != null
    }

    def "should successfully log out"() {
        given: "a valid Authorization token"
        def tokenId = "validTokenId"
        when(authService.logout(anyString())).thenReturn(true)

        when: "performing logout"
        def result = mockMvc.perform(
                post("/api/v1/auth/logout")
                .header("Authorization", tokenId)
        )
                .andReturn()

        then: "logout is successful"
        result.response.status == 200
        result.response.contentAsString == "Logged out successfully"
    }

    def "should deny logout with invalid token"() {
        given: "an invalid Authorization token"
        def tokenId = "invalidTokenId"
        when(authService.logout(anyString())).thenReturn(false)

        when: "performing logout"
        def result = mockMvc.perform(
                post("/api/v1/auth/logout")
                .header("Authorization", tokenId)
        )
                .andReturn()

        then: "logout is denied"
        result.response.status == 400
        result.response.contentAsString == "Logout was denied"
    }
}
