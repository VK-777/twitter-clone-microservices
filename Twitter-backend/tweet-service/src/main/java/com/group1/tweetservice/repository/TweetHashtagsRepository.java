package com.group1.tweetservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.group1.tweetservice.entity.TweetHashtags;
import com.group1.tweetservice.entity.TweetMedia;

import jakarta.transaction.Transactional;

import java.util.List;

public interface TweetHashtagsRepository extends JpaRepository<TweetHashtags, Long> {
    List<TweetHashtags> findByTweetId(Long tweetId);
    List<TweetHashtags> findByTagIgnoreCase(String tag);
    
    @Query("SELECT h.tag FROM TweetHashtags h GROUP BY h.tag ORDER BY COUNT(h.tag) DESC")
    List<String> findTrendingHashtags(Pageable pageable);
    @Modifying
    @Transactional
    @Query("DELETE FROM TweetHashtags h WHERE h.tweetId = :tweetId")
    void deleteByTweetId(Long tweetId);

}
