package com.group1.mediaservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group1.mediaservice.entity.Media;
import com.group1.mediaservice.entity.MediaStatus;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
	List<Media>findByUserIdAndStatus(Long userId, MediaStatus status);
	Optional<Media> findByIdAndStatus(Long id, MediaStatus status);
	List<Media> findByTweetIdAndStatus(Long tweetId, MediaStatus status);
	Page<Media> findByUserIdAndStatus(Long userId, MediaStatus status, Pageable pageable);
	

}
