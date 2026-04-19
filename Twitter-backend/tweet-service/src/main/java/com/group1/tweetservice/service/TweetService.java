package com.group1.tweetservice.service;

import java.util.List;

import com.group1.tweetservice.dto.CreateTweetRequest;
import com.group1.tweetservice.dto.TweetResponseDTO;
import com.group1.tweetservice.entity.TweetComment;

public interface TweetService {
    TweetResponseDTO createTweet(CreateTweetRequest request, Long currentUserId);
    TweetResponseDTO getTweetById(Long id, Long currentUserId);
    List<TweetResponseDTO> getFeed(Long currentUserId);
    List<TweetResponseDTO> getUserTweets(Long userId, Long currentUserId);
    List<TweetResponseDTO> getReplies(Long tweetId, Long currentUserId);
    TweetResponseDTO postReply(Long tweetId, CreateTweetRequest request, Long currentUserId);
    void deleteTweet(Long tweetId, Long currentUserId);
    void likeTweet(Long tweetId, Long currentUserId);
    void unlikeTweet(Long tweetId, Long currentUserId);
	TweetResponseDTO retweet(Long originalTweetId, Long currentUserId);
	void undoRetweet(Long originalTweetId, Long currentUserId);
	TweetComment addComment(Long tweetId, String text, Long currentUserId);
	List<TweetResponseDTO> getTweetByHashtag(String hashtag);
	List<String> getTop10TrendingHashtags();
	List<TweetResponseDTO> getTweetByContent(String keyword);
	TweetResponseDTO editTweet(Long tweetId, CreateTweetRequest request, Long currentUserId);
}
