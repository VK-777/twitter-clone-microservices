package com.group1.tweetservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group1.tweetservice.entity.Like;
import com.group1.tweetservice.entity.Tweet;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);
    Optional<Like> findByUserIdAndTweetId(Long userId, Long tweetId);
}
