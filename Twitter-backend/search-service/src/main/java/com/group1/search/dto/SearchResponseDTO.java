package com.group1.search.dto;

import lombok.Data;
import java.util.List;

@Data
public class SearchResponseDTO {

   private List<TweetResponseDTO> tweets;
   private List<UserResponseDTO> users;

}