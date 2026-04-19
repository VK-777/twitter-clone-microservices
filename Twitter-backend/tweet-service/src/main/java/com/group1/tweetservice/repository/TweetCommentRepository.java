package com.group1.tweetservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group1.tweetservice.entity.TweetComment;

import java.util.List;

public interface TweetCommentRepository extends JpaRepository<TweetComment, Long> {
    List<TweetComment> findByTweetIdOrderByCreatedAtDesc(Long tweetId);
}
