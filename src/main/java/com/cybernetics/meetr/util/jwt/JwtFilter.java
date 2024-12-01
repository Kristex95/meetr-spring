package com.cybernetics.meetr.util.jwt;

import com.cybernetics.meetr.entity.User;
import com.cybernetics.meetr.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");

		String username = null;
		String jwt = null;

		System.out.println("Authorization: " + authorizationHeader);
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			System.out.println("JWT: " + authorizationHeader);
			try {
				Claims claims = jwtUtil.parseToken(jwt); // Parse the token to get claims
				username = claims.getSubject();
			} catch (SignatureException e) {
				// Invalid JWT signature
				System.out.println("Invalid JWT signature");
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			User user = userService.getByUsername(username); // Retrieve User entity directly

			if (JwtUtil.validateToken(jwt, user.getUsername())) { // Validate token
				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(user, null, null); // No authorities

				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken); // Set authentication
			}
		}
		chain.doFilter(request, response);
	}

}
