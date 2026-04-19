package com.group1.followers.service.impl;

import com.group1.followers.dto.FollowRequestDTO;
import com.group1.followers.entity.Follow;
import com.group1.followers.repository.FollowRepository;
import com.group1.followers.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

   private final FollowRepository repository;

   @Override
   public void followUser(FollowRequestDTO dto) {

      if(repository.existsByFollowerIdAndFollowingId(
              dto.getFollowerId(),
              dto.getFollowingId())) {

          throw new RuntimeException("Already following this user");
      }

      Follow follow = new Follow();
      follow.setFollowerId(dto.getFollowerId());
      follow.setFollowingId(dto.getFollowingId());
      follow.setCreatedAt(LocalDateTime.now());

      repository.save(follow);
   }

   @Override
   public void unfollowUser(FollowRequestDTO dto) {

       repository.deleteByFollowerIdAndFollowingId(
               dto.getFollowerId(),
               dto.getFollowingId()
       );
   }

   @Override
   public List<Long> getFollowers(Long userId) {

       return repository.findByFollowingId(userId)
               .stream()
               .map(Follow::getFollowerId)
               .collect(Collectors.toList());
   }

   @Override
   public List<Long> getFollowing(Long userId) {

       return repository.findByFollowerId(userId)
               .stream()
               .map(Follow::getFollowingId)
               .collect(Collectors.toList());
   }

   @Override
   public long getFollowersCount(Long userId) {

       return repository.countByFollowingId(userId);
   }

   @Override
   public long getFollowingCount(Long userId) {

       return repository.countByFollowerId(userId);
   }
   
   @Override
   public boolean isFollowing(Long followerId,Long followingId) {
	   return repository.existsByFollowerIdAndFollowingId(followerId, followingId);
   }
}