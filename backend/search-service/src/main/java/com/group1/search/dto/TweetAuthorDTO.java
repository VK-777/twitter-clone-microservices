package com.group1.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetAuthorDTO {
    private Long id;
    private String firstName;
    private String username;
    private String profilePicture;
}