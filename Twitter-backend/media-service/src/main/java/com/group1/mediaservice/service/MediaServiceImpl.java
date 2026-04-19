package com.group1.mediaservice.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.ByteArrayResource;

import org.springframework.core.io.Resource;

import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;

import org.springframework.web.multipart.MultipartFile;

import com.group1.mediaservice.config.ValidationMessages;

import com.group1.mediaservice.dto.MediaResponse;

import com.group1.mediaservice.entity.Media;

import com.group1.mediaservice.entity.MediaStatus;

import com.group1.mediaservice.exception.MediaNotFoundException;

import com.group1.mediaservice.repository.MediaRepository;

import java.io.IOException;

import java.nio.file.*;

import java.time.LocalDateTime;

import java.util.List;

import java.util.UUID;

@Service

@RequiredArgsConstructor

public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;

    private final ValidationMessages validationMessages;

    @Value("${media.storage.location}")

    private String mediaStorageLocation;

    private final String[] allowedImageTypes = {"image/jpeg", "image/png", "image/gif"};

    private final String[] allowedVideoTypes = {"video/mp4"};

    @Override

    public MediaResponse uploadMedia(MultipartFile file, Long userId, Long tweetId) throws IOException {

        if (userId == null) {

            throw new MediaNotFoundException(

                    validationMessages.getMessage("media.user.id.required"));

        }

        if (file == null || file.isEmpty()) {

            throw new MediaNotFoundException(

                    validationMessages.getMessage("media.file.required"));

        }

        String contentType = file.getContentType();

        if (contentType == null || !isAllowed(contentType)) {

            throw new MediaNotFoundException(

                    validationMessages.getMessage("media.invalid.type"));

        }

        long maxSize = contentType.startsWith("image")

                ? 5 * 1024 * 1024

                : 20 * 1024 * 1024;

        if (file.getSize() > maxSize) {

            throw new MediaNotFoundException(

                    validationMessages.getMessage("media.size.exceeded"));

        }

        String originalFileName = file.getOriginalFilename();

        String extension = StringUtils.getFilenameExtension(originalFileName);

        String storedFileName = UUID.randomUUID() + "." + extension;

        String folder = contentType.startsWith("image") ? "images" : "videos";

        Path storagePath = Paths.get(mediaStorageLocation, folder);

        Files.createDirectories(storagePath);

        Path filePath = storagePath.resolve(storedFileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Media media = Media.builder()

                .originalName(originalFileName)

                .storedName(storedFileName)

                .fileType(contentType)

                .fileSize(file.getSize())

                .storagePath(filePath.toString())

                .userId(userId)

                .tweetId(tweetId)

                .status(MediaStatus.ACTIVE)

                .createdAt(LocalDateTime.now())

                .build();

        Media savedMedia = mediaRepository.save(media);

        return mapToResponse(savedMedia);

    }

    private boolean isAllowed(String contentType) {

        for (String type : allowedImageTypes) {

            if (type.equalsIgnoreCase(contentType)) {

                return true;

            }

        }

        for (String type : allowedVideoTypes) {

            if (type.equalsIgnoreCase(contentType)) {

                return true;

            }

        }

        return false;

    }

    @Override

    public Resource getMediaById(Long id) throws IOException {

        if (id == null) {

            throw new MediaNotFoundException(

                    validationMessages.getMessage("media.id.required"));

        }

        Media media = mediaRepository

                .findByIdAndStatus(id, MediaStatus.ACTIVE)

                .orElseThrow(() -> new MediaNotFoundException(

                        validationMessages.getMessage("media.not.found")));

        Path path = Path.of(media.getStoragePath());

        if (!Files.exists(path)) {

            throw new MediaNotFoundException(

                    validationMessages.getMessage("media.file.not.found"));

        }

        return new ByteArrayResource(Files.readAllBytes(path));

    }

    @Override

    public List<MediaResponse> getAllMediaByUserId(Long userId) {

        List<Media> mediaList =

                mediaRepository.findByUserIdAndStatus(userId, MediaStatus.ACTIVE);

        if (mediaList.isEmpty()) {

            throw new MediaNotFoundException(

                    validationMessages.getMessage("media.user.not.found"));

        }

        return mediaList.stream()

                .map(this::mapToResponse)

                .toList();

    }

    @Override

    public List<MediaResponse> getMediaByTweetId(Long tweetId) {

        List<Media> mediaList =

                mediaRepository.findByTweetIdAndStatus(tweetId, MediaStatus.ACTIVE);

        if (mediaList.isEmpty()) {

            throw new MediaNotFoundException(

                    validationMessages.getMessage("media.not.found"));

        }

        return mediaList.stream()

                .map(this::mapToResponse)

                .toList();

    }

    @Override

    public void deleteMedia(Long id) {

        Media media = mediaRepository

                .findByIdAndStatus(id, MediaStatus.ACTIVE)

                .orElseThrow(() -> new MediaNotFoundException(

                        validationMessages.getMessage("media.not.found")));

        media.setStatus(MediaStatus.DELETED);

        mediaRepository.save(media);

    }

    private MediaResponse mapToResponse(Media media) {

        return MediaResponse.builder()

                .id(media.getId())

                .fileName(media.getOriginalName())

                .fileType(media.getFileType())

                .fileSize(media.getFileSize())

                .fileUrl("/media/view/" + media.getId())

                .userId(media.getUserId())

                .tweetId(media.getTweetId())

                .build();

    }

}