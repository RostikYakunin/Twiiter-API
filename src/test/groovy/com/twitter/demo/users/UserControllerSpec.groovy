package com.twitter.demo.users

import com.fasterxml.jackson.databind.ObjectMapper
import com.twitter.demo.posts.Post
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
class UserControllerSpec extends Specification {
    @Shared
    @AutoCleanup
    UserService userService

    @Shared
    @AutoCleanup
    private MockMvc mockMvc

    @Shared
    @AutoCleanup
    private ObjectMapper objectMapper

    @Shared
    @AutoCleanup
    UserController userController


    def setup() {
        userService = mock(UserService)
        userController = new UserController(userService)
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build()
        objectMapper = new ObjectMapper()
    }

    def cleanup() {
        userService = null
        userController = null
        mockMvc = null
        objectMapper = null
    }

    def "should find user by id"() {
        given: "an existing user`s id"
        def userId = "1"
        def foundUser = new User(id: userId, username: "testUser", email: "test@example.com", password: "password")

        when(userService.findUserById(userId)).thenReturn(Optional.of(foundUser))

        when: "performing GET request to /api/v1/users/{id}"
        def result = mockMvc.perform(
                get("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "should return OK status and the user"
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("['id']").value(userId))
                .andExpect(jsonPath("['username']").value("testUser"))
                .andExpect(jsonPath("['email']").value("test@example.com"))
                .andExpect(jsonPath("['password']").value("password"))
    }

    def "should create a new user"() {
        given: "a new User"
        def newUser = new User(username: "newUser", email: "newuser@example.com", password: "newpassword")
        when(userService.createUser(newUser)).thenReturn(newUser)

        when: "performing POST request to /api/v1/users/"
        def result = mockMvc.perform(
                post("/api/v1/users/")
                        .content(objectMapper.writeValueAsString(newUser))
                        .contentType(MediaType.APPLICATION_JSON))

        then: "should return CREATED status and the user"
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("['username']").value("newUser"))
                .andExpect(jsonPath("['email']").value("newuser@example.com"))
                .andExpect(jsonPath("['password']").value("newpassword"))
    }

    def "should update user by id"() {
        given: "an existing user id and updated user data"
        def updatedUser = new User(id: "1", username: "updatedUser", email: "updated@example.com", password: "updatedpassword")
        when(userService.updateUser(anyString(), any())).thenReturn(updatedUser)

        when: "performing PUT request to /api/v1/users/{id}"
        def result = mockMvc.perform(
                put("/api/v1/users/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))

        then: "should return OK status and the updated user"
        result.andExpect(status().isOk())
                .andExpect(jsonPath("['id']").value("1"))
                .andExpect(jsonPath("['username']").value("updatedUser"))
                .andExpect(jsonPath("['email']").value("updated@example.com"))
                .andExpect(jsonPath("['password']").value("updatedpassword"))
    }

    def "should delete user by id"() {
        given: "an existing user id"
        def userId = "1"
        when(userService.deleteUser(userId)).thenReturn(true)

        when: "performing DELETE request to /api/v1/users/{id}"
        def result = mockMvc.perform(delete("/api/v1/users/{id}", userId))

        then: "should return OK status"
        result.andExpect(status().isOk())
    }

    def "should find all followers' posts by user id"() {
        given: "an existing user id"
        def userId = "1"
        def posts = [
                new Post(id: "1", content: "Post 1", authorId: "11"),
                new Post(id: "2", content: "Post 2", authorId: "22")
        ]
        when(userService.findAllFollowersPostsByUserId(userId)).thenReturn(posts)

        when: "performing GET request to /api/v1/users/subscription/{id}"
        def result = mockMvc.perform(get("/api/v1/users/subscription/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))

        then: "should return OK status and the posts"
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[0]['id']").value("1"))
                .andExpect(jsonPath("[0]['content']").value("Post 1"))
                .andExpect(jsonPath("[0]['authorId']").value("11"))
                .andExpect(jsonPath("[1]['id']").value("2"))
                .andExpect(jsonPath("[1]['content']").value("Post 2"))
                .andExpect(jsonPath("[1]['authorId']").value("22"))
    }

    def "should create a subscription"() {
        given: "followerId and followingId"
        def followerId = "follower123"
        def followingId = "following456"
        def updatedFollower = new User("name", "email", "password")
        updatedFollower.followingIdSet.add(followingId)

        println updatedFollower

        when(userService.subscribeUser(followerId, followingId)).thenReturn(updatedFollower)

        when: "performing POST request to /api/v1/users/subscription"
        def result = mockMvc.perform(post("/api/v1/users/subscription")
                .param("followerId", followerId)
                .param("followingId", followingId))

        then: "should return CREATED status and the subscription response"
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("['username']").value("name"))
                .andExpect(jsonPath("['email']").value("email"))
                .andExpect(jsonPath("['password']").value("password"))
                .andExpect(jsonPath("['followerId']").doesNotExist())
                .andExpect(jsonPath(/$.followingIdSet[0]/).value(followingId))
    }

    def "should delete a subscription"() {
        given: "followerId and followingId"
        def followerId = "follower123"
        def followingId = "following456"
        def updatedFollower = new User("name", "email", "password")
        when(userService.unsubscribeUser(followerId, followingId)).thenReturn(updatedFollower)

        when: "performing DELETE request to /api/v1/users/subscription"
        def result = mockMvc.perform(delete("/api/v1/users/subscription")
                .param("followerId", followerId)
                .param("followingId", followingId))

        then: "should return OK status"
        result.andExpect(status().isOk())
    }

    def "should get user description by id"() {
        given: "an existing user id"
        def userId = "userId"
        def userDescription =
                "Username: username\n" +
                "Likes: \n" +
                "\n" +
                "Comments: "


        when: "performing GET request to /api/v1/users/description/{id}"
        when(userService.createUserDescriptionById(userId)).thenReturn(userDescription)
        def result = mockMvc.perform(
                get("/api/v1/users/description/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))

        then: "should return OK status and the user description"
        result.andExpect(status().isOk())
        .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
    }
}
