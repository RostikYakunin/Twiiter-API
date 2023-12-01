package com.twitter.demo.users

import com.fasterxml.jackson.databind.ObjectMapper
import com.twitter.demo.comments.CommentRepository
import com.twitter.demo.likes.LikeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService {
    private UserRepository userRepository
    private LikeService likeService
    private CommentRepository commentRepo

    UserService(UserRepository userRepository, LikeService likeService, CommentRepository commentRepo) {
        this.userRepository = userRepository
        this.likeService = likeService
        this.commentRepo = commentRepo
    }

    def createUser(User user) {
        return userRepository.save(user)
    }

    def findUserById(id) {
        return userRepository.findById(id)
    }

    def updateUser(userId, updatedUser) {
        if (userRepository.existsById(userId)) {
            def existingUser = userRepository.findById(userId).orElse(null)

            if (existingUser) {
                if (updatedUser.username) {
                    existingUser.username = updatedUser.username
                }
                if (updatedUser.email) {
                    existingUser.email = updatedUser.email
                }
                if (updatedUser.password) {
                    existingUser.password = updatedUser.password
                }

                return userRepository.save(existingUser)
            }
        }
        return null
    }

    def deleteUser(userId) {
        userRepository.deleteById(userId)

        return !userRepository.existsById(userId)
    }

    def findAll() {
        return userRepository.findAll();
    }

    def subscribeUser(followerUserId, followingUserId) {
        def follower = userRepository.findById(followerUserId).orElseThrow(RuntimeException::new)
        def following = userRepository.findById(followingUserId).orElseThrow(RuntimeException::new)

        if (follower.followingIdList.contains(following.id)) {
            throw new RuntimeException("User wit id: $followerUserId is alredy sibscribed on user $followingUserId")
        }

        follower.followingIdList.add(following.id)
        userRepository.save(follower)

        return follower
    }

    def unsubscribeUser(followerUserId, followingUserId) {
        def follower = userRepository.findById(followerUserId).orElseThrow(RuntimeException::new)
        def following = userRepository.findById(followingUserId).orElseThrow(RuntimeException::new)

        if (follower.followingIdList.contains(following.id)) {
            follower.followingIdList.remove(following.id)
            userRepository.save(follower)
        }

        return follower
    }

    def createUserDescriptionById(String id) {
        def foundUser = userRepository.findById(id).orElseThrow(RuntimeException::new)
        def listOfLikes = likeService.findLikesByUserId(id)
        def listOfComments = commentRepo.findCommentsByUserId(id)


        StringBuilder result = new StringBuilder()

        result.append("Username: ")
                .append(foundUser.username)
                .append("\n")

        result.append("Likes: ").append("\n")
        listOfLikes.forEach(
                like -> {
                    result.append("     ").append(like).append("\n")
                }
        )

        result.append("\n")

        result.append("Comments: ").append("\n")
        listOfComments.forEach(
                comment -> {
                    result.append("     ").append(comment).append("\n")
                }
        )

        return result.toString();
    }
}
