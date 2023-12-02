package com.twitter.demo.users


import com.twitter.demo.comments.CommentRepository
import com.twitter.demo.likes.LikeService
import com.twitter.demo.posts.PostRepository
import com.twitter.demo.users.utils.UserHandler
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService {
    private UserRepository userRepository
    private LikeService likeService
    private CommentRepository commentRepo
    private PostRepository postRepository

    UserService(UserRepository userRepository, LikeService likeService, CommentRepository commentRepo, PostRepository postRepository) {
        this.userRepository = userRepository
        this.likeService = likeService
        this.commentRepo = commentRepo
        this.postRepository = postRepository
    }

    def createUser(User user) {
        return userRepository.save(user)
    }

    def findUserById(id) {
        return userRepository.findById(id)
    }

    def updateUser(userId, updatedUser) {
        def existingUser = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User with id= $userId not found")
        )

        def handledUser = UserHandler.userUpdateHandler(existingUser, updatedUser)
        return userRepository.save(handledUser)
    }

    def deleteUser(userId) {
        userRepository.deleteById(userId)
        return !userRepository.existsById(userId)
    }

    def findAll() {
        return userRepository.findAll()
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

        return result.toString()
    }

    def findAllFollowersPostsByUserId(String id) {
        def foundUser = userRepository.findById(id).orElseThrow(RuntimeException::new)
        def postsList = []

        foundUser.followerIdSet.forEach(
                followerId -> {
                    def foundPost = postRepository.findById(followerId).orElseThrow(RuntimeException::new)
                    postsList.add(foundPost)
                }
        )

        return postsList
    }

    def subscribeUser(String followerId, String followingId) {
        def follower = userRepository.findById(followerId).orElseThrow(RuntimeException::new)
        def following = userRepository.findById(followingId).orElseThrow(RuntimeException::new)

        if (follower.followingIdSet.contains(following.id)) {
            throw new RuntimeException("User with id: $followerId is already subscribed to user $followingId")
        }

        follower.followingIdSet.add(following.id)
        following.followerIdSet.add(follower.id)

        userRepository.save(follower)
        userRepository.save(following)

        return follower
    }

    def unsubscribeUser(String followerId, String followingId) {
        def follower = userRepository.findById(followerId).orElseThrow(RuntimeException::new)
        def following = userRepository.findById(followingId).orElseThrow(RuntimeException::new)

        if (!follower.followingIdSet.contains(following.id)) {
            throw new RuntimeException("User with id: $followerId is already unsubscribed to user $followingId")
        }

        follower.followingIdSet.remove(following.id)
        following.followerIdSet.remove(follower.id)

        userRepository.save(follower)
        userRepository.save(following)

        return follower
    }
}
