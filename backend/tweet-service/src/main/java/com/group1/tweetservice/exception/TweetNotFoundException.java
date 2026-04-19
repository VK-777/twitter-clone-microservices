package com.group1.tweetservice.exception;

public class TweetNotFoundException extends RuntimeException {
    public TweetNotFoundException(Long id) {
        super("Tweet not found with id: " + id);
    }
}
