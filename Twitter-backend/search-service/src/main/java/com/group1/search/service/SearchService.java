package com.group1.search.service;

import com.group1.search.dto.TweetResponseDTO;
import com.group1.search.dto.UserResponseDTO;

import java.util.List;

public interface SearchService {

   List<TweetResponseDTO> searchTweets(String keyword);

   List<UserResponseDTO> searchUsers(String query);
   List<String> getTrendingHashtags();


}