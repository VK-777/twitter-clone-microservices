package com.group1.search.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;


@Data
public class TweetCommentDTO {
   
    private Long id;
    
    private Long tweetId;
    private Long userId;
    private String commentText;
    private LocalDateTime createdAt;
}