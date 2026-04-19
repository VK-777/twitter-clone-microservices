package com.group1.followers.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "followers")
@Data
public class Follow {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "follower_id", nullable = false)
   private Long followerId;

   @Column(name = "following_id", nullable = false)
   private Long followingId;

   @Column(name = "created_at")
   private LocalDateTime createdAt;
}