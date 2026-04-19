package com.group1.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.group1.gateway.util.JwtUtil;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private RouteValidator routeValidator;
	public AuthenticationFilter() {
		super(Config.class);
	}
	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange,chain)->{
			if(routeValidator.isSecured.test(exchange.getRequest())) {
				if(!exchange.getRequest().getHeaders().containsHeader(HttpHeaders.AUTHORIZATION)) {
					throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Missing authorization header");
				}
				String authHeader=exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				if(authHeader!=null&&authHeader.startsWith("Bearer ")) {
					authHeader=authHeader.substring(7);
				}
				try {
					jwtUtil.validateToken(authHeader);
					String loggedInUserEmail=jwtUtil.extractEmail(authHeader);
					ServerHttpRequest modifiedRequest=exchange.getRequest().mutate()
							.header("X-Logged-In-User", loggedInUserEmail).build();
					return chain.filter(exchange.mutate().request(modifiedRequest).build());
				}
				catch(Exception e) {
					throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Unauthorized access to application");
					
				}
			}
			return chain.filter(exchange);
		});
	}
	public static class Config{
		
	}
}
