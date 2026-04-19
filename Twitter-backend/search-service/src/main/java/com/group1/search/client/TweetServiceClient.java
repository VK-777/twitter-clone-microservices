package com.group1.search.client;

import com.group1.search.dto.TweetResponseDTO;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "tweet-service")
public interface TweetServiceClient {

    @GetMapping("/api/v1/tweets/search")
    List<TweetResponseDTO> searchTweets(@RequestParam("keyword") String keyword);
    
    @GetMapping("/api/v1/tweets/trending")
    List<String> getTrendingHashtags();

}