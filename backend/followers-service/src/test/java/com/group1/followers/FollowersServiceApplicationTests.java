package com.group1.followers;

import com.group1.followers.dto.FollowRequestDTO;
import com.group1.followers.entity.Follow;
import com.group1.followers.repository.FollowRepository;
import com.group1.followers.service.impl.FollowServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FollowersServiceApplicationTests {

   @Mock
   private FollowRepository repository;

   @InjectMocks
   private FollowServiceImpl service;

   @BeforeEach
   void setup() {
       MockitoAnnotations.openMocks(this);
   }

   @Test
   void contextLoads() {
       assertTrue(true);
   }

   @Test
   void testFollowUserSuccess() {

       FollowRequestDTO dto = new FollowRequestDTO();
       dto.setFollowerId(1L);
       dto.setFollowingId(5L);

       when(repository.existsByFollowerIdAndFollowingId(1L,5L))
               .thenReturn(false);

       service.followUser(dto);

       verify(repository,times(1)).save(any(Follow.class));
   }

   @Test
   void testFollowUserDuplicate() {

       FollowRequestDTO dto = new FollowRequestDTO();
       dto.setFollowerId(1L);
       dto.setFollowingId(5L);

       when(repository.existsByFollowerIdAndFollowingId(1L,5L))
               .thenReturn(true);

       RuntimeException exception =
               assertThrows(RuntimeException.class,
                       () -> service.followUser(dto));

       assertEquals("Already following this user", exception.getMessage());
   }

   @Test
   void testUnfollowUser() {

       FollowRequestDTO dto = new FollowRequestDTO();
       dto.setFollowerId(1L);
       dto.setFollowingId(5L);

       service.unfollowUser(dto);

       verify(repository).deleteByFollowerIdAndFollowingId(1L,5L);
   }

   @Test
   void testGetFollowers() {

       Follow f1 = new Follow();
       f1.setFollowerId(1L);

       Follow f2 = new Follow();
       f2.setFollowerId(2L);

       when(repository.findByFollowingId(5L))
               .thenReturn(List.of(f1,f2));

       List<Long> result = service.getFollowers(5L);

       assertEquals(2,result.size());
   }

   @Test
   void testGetFollowing() {

       Follow f1 = new Follow();
       f1.setFollowingId(3L);

       Follow f2 = new Follow();
       f2.setFollowingId(4L);

       when(repository.findByFollowerId(1L))
               .thenReturn(List.of(f1,f2));

       List<Long> result = service.getFollowing(1L);

       assertEquals(2,result.size());
   }

   @Test
   void testFollowersCount() {

       when(repository.countByFollowingId(5L))
               .thenReturn(10L);

       long count = service.getFollowersCount(5L);

       assertEquals(10,count);
   }

   @Test
   void testFollowingCount() {

       when(repository.countByFollowerId(1L))
               .thenReturn(7L);

       long count = service.getFollowingCount(1L);

       assertEquals(7,count);
   }
}