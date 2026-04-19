package com.group1.followers.controller;

import com.group1.followers.dto.FollowRequestDTO;
import com.group1.followers.service.FollowService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/follow")
@RequiredArgsConstructor
public class FollowController {

   private final FollowService service;

   @PostMapping
   public String followUser(@Valid @RequestBody FollowRequestDTO dto) {
       service.followUser(dto);
       return "User followed successfully";
   }

   @DeleteMapping
   @Transactional
   public String unfollowUser(@Valid @RequestBody FollowRequestDTO dto) {
       service.unfollowUser(dto);
       return "User unfollowed successfully";
   }

   @GetMapping("/followers/{userId}")
   public List<Long> getFollowers(@PathVariable Long userId) {
       return service.getFollowers(userId);
   }

   @GetMapping("/following/{userId}")
   public List<Long> getFollowing(@PathVariable Long userId) {
       return service.getFollowing(userId);
   }

   @GetMapping("/followers/count/{userId}")
   public long followersCount(@PathVariable Long userId) {
       return service.getFollowersCount(userId);
   }

   @GetMapping("/following/count/{userId}")
   public long followingCount(@PathVariable Long userId) {
       return service.getFollowingCount(userId);
   }
   
   @GetMapping("/is-following")
   public boolean isFollowing(@RequestParam Long followerId,@RequestParam Long followingId) {
	   return service.isFollowing(followerId,followingId);
   }
}