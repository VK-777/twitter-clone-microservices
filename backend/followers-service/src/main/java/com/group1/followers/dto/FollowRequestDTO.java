package com.group1.followers.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FollowRequestDTO {
	@NotNull(message="Please provide followerId")
	private Long followerId;
	@NotNull(message="Please provide followingId")
	private Long followingId;

}
