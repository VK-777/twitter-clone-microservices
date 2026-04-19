package com.group1.tweetservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "tweet_hashtags")
@Data
public class TweetHashtags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long tweetId; // Link to the tweet
    private String tag;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}