package com.group1.user.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;
	public String generateToken(String email) {
		Map<String,Object> claims=new HashMap<>();
		return createToken(claims,email);
	}
	public String createToken(Map<String,Object> claims,String email) {
		return Jwts.builder().setClaims(claims).setSubject(email).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
				.signWith(getSignKey(),SignatureAlgorithm.HS256)
				.compact();
	}
	private Key getSignKey(){
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}
}
