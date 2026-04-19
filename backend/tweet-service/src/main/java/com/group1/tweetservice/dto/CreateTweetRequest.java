package com.group1.tweetservice.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class CreateTweetRequest {

    @NotBlank(message = "Content cannot be empty")
    @Size(max = 280, message = "Tweet cannot exceed 280 characters")
    private String content;

    private Long userId;
    private String userFirstName;
    private String userEmail;
    private List<Long> mediaIds;
    private Long parentTweetId;
    private LocalDateTime scheduledAt;
}
