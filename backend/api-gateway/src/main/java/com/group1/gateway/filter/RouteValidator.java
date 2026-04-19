package com.group1.gateway.filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {

	public static final List<String> openApiEndpoints=List.of(
			"/api/v1/users/register",
			"/api/v1/users/login",
			"/api/v1/users/forgot-password",
			"/api/v1/users/reset-password",
			"/api/v1/tweets/feed",
			"/api/v1/tweets/trending",
			"/api/v1/tweets/search",
			"/api/v1/media/files",
			"/eureka",
			"/actuator"
			);
	public Predicate<ServerHttpRequest> isSecured=request->openApiEndpoints.stream().noneMatch(uri->request.getURI().getPath().contains(uri));
}
