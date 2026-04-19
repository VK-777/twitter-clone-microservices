package com.group1.mediaservice.api;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import java.util.List;

import org.springframework.core.io.Resource;

import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.group1.mediaservice.dto.MediaResponse;

import com.group1.mediaservice.service.MediaService;

@RestController
@RequestMapping("api/v1/media")
@RequiredArgsConstructor

public class MediaController {
    private final MediaService mediaService;
    @PostMapping("/upload")
    public ResponseEntity<MediaResponse> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long userId,
            @RequestParam Long tweetId) throws IOException {
        MediaResponse response =
                mediaService.uploadMedia(file, userId, tweetId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> getMedia(@PathVariable Long id) throws IOException {
        Resource resource = mediaService.getMediaById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MediaResponse>> getMediaByUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(
                mediaService.getAllMediaByUserId(userId)
        );
    }
    @GetMapping("/tweet/{tweetId}")
    public ResponseEntity<List<MediaResponse>> getMediaByTweet(
            @PathVariable Long tweetId) {
        return ResponseEntity.ok(
                mediaService.getMediaByTweetId(tweetId)
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.ok("Media deleted successfully");
    }
}