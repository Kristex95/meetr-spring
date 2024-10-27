package com.cybernetics.meetr.util.jwt;

import com.cybernetics.meetr.entity.User;
import com.cybernetics.meetr.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

	private static final String SECRET_KEY = "meetrSecret";
	private static final Integer TOKEN_VALID_TIME = 1000 * 60 * 60 * 10; //10 hours

	public static String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALID_TIME))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

	public static Claims parseToken(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
	}

	public static String extractUsername(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
	}

	public static Boolean isTokenExpired(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration().before(new Date());
	}

	public static Boolean validateToken(String token, String username) {
		final String tokenUsername = extractUsername(token);
		return (tokenUsername.equals(username) && !isTokenExpired(token));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
