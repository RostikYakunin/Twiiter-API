package com.twitter.demo.likes

import com.twitter.demo.posts.PostRepository
import com.twitter.demo.users.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class LikeService {
    private LikeRepository likeRepository
    private UserRepository userRepo
    private PostRepository postRepo

    LikeService(LikeRepository likeRepository, UserRepository userRepo, PostRepository postRepo) {
        this.likeRepository = likeRepository
        this.userRepo = userRepo
        this.postRepo = postRepo
    }

    def likePost(userId, postId) {
        def userById = userRepo.findById(userId)
        def postById = postRepo.findById(postId)

        if (userById.isEmpty() || postById.isEmpty()) {
            throw new RuntimeException("User or Post is not exists")
        }

        Like like = new Like(userId, postId)
        likeRepository.save(like)
    }

    def unlikePost(likeId) {
        likeRepository.deleteById(likeId)

        return !likeRepository.existsById(likeId)
    }

    def findAllLikes() {
        return likeRepository.findAll()
    }

    def findLikesByUserId(id) {
        likeRepository.findLikesByUserId(id)
    }
}
