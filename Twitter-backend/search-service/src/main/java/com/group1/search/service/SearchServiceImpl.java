package com.group1.search.service;

import com.group1.search.client.TweetServiceClient;
import com.group1.search.client.UserServiceClient;
import com.group1.search.dto.TweetResponseDTO;
import com.group1.search.dto.UserResponseDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

   private final TweetServiceClient tweetServiceClient;
   private final UserServiceClient userServiceClient;

   public SearchServiceImpl(TweetServiceClient tweetServiceClient,
                            UserServiceClient userServiceClient) {
       this.tweetServiceClient = tweetServiceClient;
       this.userServiceClient = userServiceClient;
   }

   @Override
   //@CircuitBreaker(name = "tweetService", fallbackMethod = "tweetFallback")
   public List<TweetResponseDTO> searchTweets(String keyword) {
       return tweetServiceClient.searchTweets(keyword);
   }
   
   @Override
   @CircuitBreaker(name = "tweetService", fallbackMethod = "trendingFallback")
   public List<String> getTrendingHashtags() {
       return tweetServiceClient.getTrendingHashtags();
   }

   @Override
   @CircuitBreaker(name = "userService", fallbackMethod = "userFallback")
   public List<UserResponseDTO> searchUsers(String prefix) {
       return userServiceClient.searchUsers(prefix);
   }

   // fallback method for tweet service
   public List<TweetResponseDTO> tweetFallback(String keyword, Throwable t) {
       throw new RuntimeException("Tweet service is currently unavailable. Please try again later.");
   }
   public List<String> trendingFallback(Throwable t) {
       throw new RuntimeException("Trending service is currently unavailable. Please try again later.");
   }

   // fallback method for user service
   public List<UserResponseDTO> userFallback(String query, Throwable t) {
       throw new RuntimeException("User service is currently unavailable. Please try again later.");
   }
}

