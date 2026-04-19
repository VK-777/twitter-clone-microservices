package com.group1.tweetservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group1.tweetservice.entity.Tweet;
import com.group1.tweetservice.entity.TweetType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findByParentTweetIdIsNullOrderByCreatedAtDesc();
    List<Tweet> findByUserIdAndParentTweetIdIsNullOrderByCreatedAtDesc(Long userId);
    List<Tweet> findByParentTweetIdOrderByCreatedAtAsc(Long parentTweetId);
    List<Tweet> findByContentContainingIgnoreCaseAndIsDeletedFalseOrderByCreatedAtDesc(String keyword);
    
    Optional<Tweet> findByParentTweetIdAndUserIdAndTweetType(Long parentTweetId, Long userId, TweetType tweetType);
    boolean existsByParentTweetIdAndUserIdAndTweetType(Long parentTweetId, Long userId, TweetType tweetType);
    List<Tweet> findByUserIdAndTweetTypeOrderByCreatedAtDesc(Long userId, TweetType tweetType);
    List<Tweet> findByIsScheduledTrueAndScheduledAtBefore(LocalDateTime currentTime);
    List<Tweet> findByIsScheduledFalseAndParentTweetIdIsNullAndIsDeletedFalseOrderByCreatedAtDesc();
}
