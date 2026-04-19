package com.group1.followers.repository;

import com.group1.followers.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

   List<Follow> findByFollowerId(Long followerId);

   List<Follow> findByFollowingId(Long followingId);

   void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

   long countByFollowingId(Long followingId);

   long countByFollowerId(Long followerId);
   
   boolean existsByFollowerIdAndFollowingId(Long followerId,Long followingId);
   
}