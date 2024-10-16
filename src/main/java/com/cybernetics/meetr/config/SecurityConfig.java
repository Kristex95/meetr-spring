package com.cybernetics.meetr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authorizeRequests -> authorizeRequests
					.anyRequest().permitAll()  // Permit all endpoints for development
			)
			.formLogin(form -> form
					.loginPage("/login")  // Custom login page
					.defaultSuccessUrl("/main", true)  // Redirect to main page after successful login
					.permitAll()
			)
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login?logout")  // Redirect to login page after logout
					.permitAll()
			);

		return http.build(); // Build the SecurityFilterChain
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}