package com.group1.tweetservice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.group1.tweetservice.entity.Tweet;
import com.group1.tweetservice.repository.TweetRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TweetScheduler {

    private final TweetRepository tweetRepository;
    private static final Logger log = LoggerFactory.getLogger(TweetScheduler.class);

    @Scheduled(fixedRate = 60000) // Runs every minute
    @Transactional
    public void publishScheduledTweets() {
        LocalDateTime now = LocalDateTime.now();
        List<Tweet> scheduledTweets = tweetRepository.findByIsScheduledTrueAndScheduledAtBefore(now);

        if (!scheduledTweets.isEmpty()) {
            for (Tweet tweet : scheduledTweets) {
                tweet.setIsScheduled(false);
                tweet.setCreatedAt(now); 
            }
            tweetRepository.saveAll(scheduledTweets);
            log.info("Published {} scheduled tweets at {}", scheduledTweets.size(), now);
        }
    }
}
