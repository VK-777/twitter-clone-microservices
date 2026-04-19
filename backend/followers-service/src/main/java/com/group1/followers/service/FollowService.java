package com.group1.followers.service;

import com.group1.followers.dto.FollowRequestDTO;
import java.util.List;

public interface FollowService {

   void followUser(FollowRequestDTO dto);

   void unfollowUser(FollowRequestDTO dto);

   List<Long> getFollowers(Long userId);

   List<Long> getFollowing(Long userId);

   long getFollowersCount(Long userId);

   long getFollowingCount(Long userId);

   boolean isFollowing(Long followerId, Long followingId);
}