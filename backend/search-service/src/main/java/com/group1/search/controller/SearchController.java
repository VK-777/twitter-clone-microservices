package com.group1.search.controller;

import com.group1.search.dto.TweetResponseDTO;
import com.group1.search.dto.UserResponseDTO;
import com.group1.search.service.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

   private final SearchService searchService;

   public SearchController(SearchService searchService) {
       this.searchService = searchService;
   }

   // Search Tweets
   @GetMapping("/tweets")
   public List<TweetResponseDTO> searchTweets(@RequestParam String keyword) {
       return searchService.searchTweets(keyword);
   }

   // Search Users
   @GetMapping("/users")
   public List<UserResponseDTO> searchUsers(@RequestParam String prefix) {
       return searchService.searchUsers(prefix);
   }
   
   @GetMapping("/trending")
   public List<String> trending() {
       return searchService.getTrendingHashtags();
   }

   // Health Check
   @GetMapping("/health")
   public String healthCheck() {
       return "Search Service Running";
   }
}