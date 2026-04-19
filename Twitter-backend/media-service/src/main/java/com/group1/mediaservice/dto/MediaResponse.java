package com.group1.mediaservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaResponse {
	private Long id;
	private String fileName;
	private String fileType;
	private String fileUrl;
	private Long fileSize;
	private Long tweetId;
	private Long userId;
	

}
