package com.group1.tweetservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group1.tweetservice.entity.TweetMedia;

import java.util.List;
import java.util.Optional;

public interface TweetMediaRepository extends JpaRepository<TweetMedia, Long> {
    List<TweetMedia> findByTweetId(Long tweetId);
}

