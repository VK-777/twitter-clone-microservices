package com.group1.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetResponseDTO {
    private Long id;
    private String content;
    private TweetAuthorDTO author;
    private int likesCount;
    private int retweetsCount;
    private int repliesCount;
    private int viewsCount;
    //private boolean isLiked;
    //private boolean isRetweeted;
    private LocalDateTime createdAt;
    private Long parentTweetId;
    private List<Long> mediaIds;
    private List<String> hashtags;
    private List<TweetCommentDTO> comments;
}










