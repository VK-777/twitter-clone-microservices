package com.group1.tweetservice.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.group1.tweetservice.dto.CreateTweetRequest;
import com.group1.tweetservice.dto.TweetResponseDTO;
import com.group1.tweetservice.entity.TweetComment;
import com.group1.tweetservice.service.TweetService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tweets")
@RequiredArgsConstructor

public class TweetController {

    private final TweetService tweetService;

    @GetMapping("/feed")
    public ResponseEntity<List<TweetResponseDTO>> getFeed(
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        return ResponseEntity.ok(tweetService.getFeed(userId));
    }

    @PostMapping
    public ResponseEntity<TweetResponseDTO> createTweet(
            @Valid @RequestBody CreateTweetRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Logged-In-User", required = false) String email) {
        if (request.getUserId() == null) request.setUserId(userId);
        if (request.getUserEmail() == null) request.setUserEmail(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(tweetService.createTweet(request, userId));
    }
    @PostMapping("/{id}/retweet")
    public ResponseEntity<TweetResponseDTO> retweet(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id") Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tweetService.retweet(id, userId));
    }
    
    @DeleteMapping("/{id}/retweet")
    public ResponseEntity<String> undoRetweet(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id") Long userId) {
    	tweetService.undoRetweet(id, userId);
        return ResponseEntity.ok("Retweet Removed");
    } 

    @PostMapping("/{id}/comments")
    public ResponseEntity<TweetComment> addComment(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody,
            @RequestHeader(value = "X-User-Id") Long userId) {
        
        String commentText = requestBody.get("text");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tweetService.addComment(id, commentText, userId));
    }
    @GetMapping("/{id}")
    public ResponseEntity<TweetResponseDTO> getTweetById(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        return ResponseEntity.ok(tweetService.getTweetById(id, userId));
    }
    @GetMapping("/hashtag/{tag}")
    public ResponseEntity<List<TweetResponseDTO>> getTweetByHashtag(
            @PathVariable String tag) {
        return ResponseEntity.ok(tweetService.getTweetByHashtag(tag));
    }
    @GetMapping("/search")
    public ResponseEntity<List<TweetResponseDTO>> getTweets(@RequestParam String keyword) {
        return ResponseEntity.ok(tweetService.getTweetByContent(keyword));
    }
    @GetMapping("/trending")
    public ResponseEntity<List<String>> getTrendingHashtags() {
        return ResponseEntity.ok(tweetService.getTop10TrendingHashtags());
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TweetResponseDTO>> getUserTweets(
            @PathVariable Long userId,
            @RequestHeader(value = "X-User-Id", required = false) Long currentUserId) {
        return ResponseEntity.ok(tweetService.getUserTweets(userId, currentUserId));
    }

    
    @GetMapping("/{id}/replies")
    public ResponseEntity<List<TweetResponseDTO>> getReplies(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        return ResponseEntity.ok(tweetService.getReplies(id, userId));
    }
    
    

    @PostMapping("/{id}/replies")
    public ResponseEntity<TweetResponseDTO> postReply(
            @PathVariable Long id,
            @Valid @RequestBody CreateTweetRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Logged-In-User", required = false) String email) {
        if (request.getUserId() == null) request.setUserId(userId);
        if (request.getUserEmail() == null) request.setUserEmail(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(tweetService.postReply(id, request, userId));
    }
    @PutMapping("/{id}")
    public ResponseEntity<TweetResponseDTO> editTweet(
            @PathVariable Long id,
            @RequestBody CreateTweetRequest request,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-Logged-In-User", required = false) String email) {
    	if (request.getUserId() == null) request.setUserId(userId);
        if (request.getUserEmail() == null) request.setUserEmail(email);
        return new ResponseEntity<>(tweetService.editTweet(id, request, userId), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTweet(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        tweetService.deleteTweet(id, userId);
        return ResponseEntity.ok("Tweet deleted successfully");
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<String> likeTweet(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        tweetService.likeTweet(id, userId);
        return ResponseEntity.ok("Tweet liked");
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<String> unlikeTweet(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        tweetService.unlikeTweet(id, userId);
        return ResponseEntity.ok("Tweet unliked");
    }
}

