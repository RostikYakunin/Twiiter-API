package com.twitter.demo.users

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController {
    private final UserService userService

    UserController(UserService userService) {
        this.userService = userService
    }

    @GetMapping("/{id}")
    def findUserById(@PathVariable("id") id) {
        def foundUser = userService.findUserById(id)
                .orElseThrow(
                        () -> new RuntimeException("User with id = $id not found")
                )
        return ResponseEntity.ok(foundUser)
    }

    @PostMapping("/")
    def createUser(@RequestBody user) {
        User newUser = new User(user.username, user.email, user.password)
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(newUser))
    }

    @PutMapping("/{id}")
    def updateUser(@PathVariable("id") id, @RequestBody user) {
        return ResponseEntity.ok(userService.updateUser(id, user))
    }

    @DeleteMapping("/{id}")
    def deleteUser(@PathVariable("id") id) {
        return ResponseEntity.ok(userService.deleteUser(id))
    }

    @GetMapping("/")
    def findAllUsers() {
        return ResponseEntity.ok(userService.findAll())
    }

    @GetMapping("/subscription/{id}")
    def findAllFollowersPostsByUserId(@PathVariable("id") id) {
        return ResponseEntity.ok(userService.findAllFollowersPostsByUserId(id))
    }

    @PostMapping("/subscription")
    def createSubscription(
            @RequestParam("followerId") String followerId,
            @RequestParam("followingId") String followingId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.subscribeUser(followerId, followingId))
    }

    @DeleteMapping("/subscription")
    def deleteSubscription(
            @RequestParam("followerId") String followerId,
            @RequestParam("followingId") String followingId) {
        return ResponseEntity.ok(userService.unsubscribeUser(followerId, followingId))
    }

    @GetMapping("/description/{id}")
    def createUserDescription(@PathVariable("id") id) {
        return ResponseEntity.ok(userService.createUserDescriptionById(id))
    }
}
