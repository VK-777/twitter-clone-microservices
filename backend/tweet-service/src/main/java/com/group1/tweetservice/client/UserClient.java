package com.group1.tweetservice.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.group1.tweetservice.dto.UserProfileDto;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Map;
import java.util.HashMap;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/v1/users/{id}/profile")
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackProfile")
    UserProfileDto getUserProfile(@PathVariable Long id);

    default UserProfileDto fallbackProfile(Long id, Throwable t) {
    	UserProfileDto fallback = new UserProfileDto();
    	fallback.setFirstName("Twitter User");
        return fallback;
    }
}