package com.group1.tweetservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "tweet_comments")
@Data
public class TweetComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long tweetId;
    private Long userId;
    private String commentText;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
