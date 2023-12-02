package com.twitter.demo.users.utils

import com.twitter.demo.users.User

class UserHandler {

    static def userUpdateHandler(User existingUser, User updatedUser) {
        if (updatedUser.username) {
            existingUser.username = updatedUser.username
        }
        if (updatedUser.email) {
            existingUser.email = updatedUser.email
        }
        if (updatedUser.password) {
            existingUser.password = updatedUser.password
        }

        return existingUser
    }
}
