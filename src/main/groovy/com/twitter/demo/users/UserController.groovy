package com.twitter.demo.users


import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users/")
class UserController {
    private final UserService userService

    UserController(UserService userService) {
        this.userService = userService
    }

    @GetMapping("{id}")
    def findUserById(@PathVariable("id") id) {
        return userService.findUserById(id).orElseThrow() //TODO: remake all get methods
    }

    @PostMapping
    def createUser(@RequestBody user) {
        User newUser = new User(user.username, user.email, user.password)

        return userService.createUser(newUser)
    }

    @PutMapping("{id}")
    def updateUser(@PathVariable("id") id, @RequestBody user) {
        return userService.updateUser(id, user)
    }

    @DeleteMapping("{id}")
    def deleteUser(@PathVariable("id") id) {
        return userService.deleteUser(id)
    }

    @GetMapping
    def findAllUsers() {
        return userService.findAll();
    }

    @PostMapping("subscription")
    def subscribeUser(@RequestParam("followerId") String followerId, @RequestParam("followingId") String followingId) {
        return userService.subscribeUser(followerId, followingId)
    }

    @DeleteMapping("subscription")
    def unsubscribeUser(@RequestParam("followerId") String followerId, @RequestParam("followingId") String followingId) {
        return userService.unsubscribeUser(followerId, followingId)
    }

    @GetMapping("description/{id}")
    def createUserDescription (@PathVariable("id") id) {
        return userService.createUserDescriptionById(id)
    }
}
