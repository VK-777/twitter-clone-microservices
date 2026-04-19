package com.group1.tweetservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tweets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_first_name", nullable = false)
    private String userFirstName;

    @Column(nullable = false, length = 280)
    private String content;

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name = "likes_count")
    private int likesCount = 0;

    @Column(name = "retweets_count")
    private int retweetsCount = 0;

    @Column(name = "replies_count")
    private int repliesCount = 0;

    @Column(name = "views_count")
    private int viewsCount = 0;

    @Column(name = "parent_tweet_id")
    private Long parentTweetId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "is_deleted")
    private Boolean isDeleted=false;

    @Column(name = "is_scheduled")
    private Boolean isScheduled=false;
    
    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tweet_type")
    private TweetType tweetType = TweetType.TWEET; 

    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

