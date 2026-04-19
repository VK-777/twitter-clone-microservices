package com.group1.search;

import com.group1.search.client.TweetServiceClient;
import com.group1.search.client.UserServiceClient;
import com.group1.search.controller.SearchController;
import com.group1.search.dto.TweetResponseDTO;
import com.group1.search.dto.UserResponseDTO;
import com.group1.search.service.SearchServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceApplicationTests {

   @Mock
   private TweetServiceClient tweetServiceClient;

   @Mock
   private UserServiceClient userServiceClient;

   @InjectMocks
   private SearchServiceImpl searchService;

   private SearchController searchController;

   @BeforeEach
   void setUp() {
       MockitoAnnotations.openMocks(this);
       searchController = new SearchController(searchService);
   }

   // ===============================
   // Test Search Tweets
   // ===============================
   @Test
   void testSearchTweets() {

       TweetResponseDTO tweet = new TweetResponseDTO();
       tweet.setId(1L);
       tweet.setContent("Learning Spring Boot");

       when(tweetServiceClient.searchTweets("spring"))
               .thenReturn(List.of(tweet));

       List<TweetResponseDTO> result =
               searchController.searchTweets("spring");

       assertEquals(1, result.size());
       assertEquals("Learning Spring Boot", result.get(0).getContent());

       verify(tweetServiceClient, times(1)).searchTweets("spring");
   }

   // ===============================
   // Test Search Users
   // ===============================
   @Test
   void testSearchUsers() {

       UserResponseDTO user = new UserResponseDTO();
       user.setId(1L);
       user.setFirstName("sudheer");

       when(userServiceClient.searchUsers("sudheer"))
               .thenReturn(List.of(user));

       List<UserResponseDTO> result =
               searchController.searchUsers("sudheer");

       assertEquals(1, result.size());
       assertEquals("sudheer", result.get(0).getFirstName());

       verify(userServiceClient, times(1)).searchUsers("sudheer");
   }

   // ===============================
   // Test Health Endpoint
   // ===============================
   @Test
   void testHealthCheck() {

       String response = searchController.healthCheck();

       assertEquals("Search Service Running", response);
   }

   // ===============================
   // Test Empty Tweet Result
   // ===============================
   @Test
   void testEmptyTweetSearch() {

       when(tweetServiceClient.searchTweets("unknown"))
               .thenReturn(List.of());

       List<TweetResponseDTO> result =
               searchController.searchTweets("unknown");

       assertTrue(result.isEmpty());
   }

   // ===============================
   // Test Empty User Result
   // ===============================
   @Test
   void testEmptyUserSearch() {

       when(userServiceClient.searchUsers("unknown"))
               .thenReturn(List.of());

       List<UserResponseDTO> result =
               searchController.searchUsers("unknown");

       assertTrue(result.isEmpty());
   }
}