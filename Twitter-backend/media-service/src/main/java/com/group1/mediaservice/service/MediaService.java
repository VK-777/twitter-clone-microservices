package com.group1.mediaservice.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.group1.mediaservice.dto.MediaResponse;

//import jakarta.annotation.Resource;
import org.springframework.core.io.Resource;


public interface MediaService {
	MediaResponse uploadMedia(MultipartFile file, Long userId, Long tweetId) throws IOException;
	Resource getMediaById(Long id) throws IOException;
	List<MediaResponse> getAllMediaByUserId(Long userId);
	List<MediaResponse> getMediaByTweetId(Long tweetId);
	void deleteMedia(Long id);
	

}
