package com.group1.tweetservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "tweet_media")
@Data
public class TweetMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // media id of blob obtained from media microservice
    private Long mediaId; 
    private Long tweetId;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}