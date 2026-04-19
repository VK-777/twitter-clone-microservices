package com.group1.tweetservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetAuthorDto {
    private Long id;
    private String firstName;
    private String username;
    private String profilePicture;
}