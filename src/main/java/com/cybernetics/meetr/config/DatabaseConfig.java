package com.cybernetics.meetr.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.cybernetics.meetr.entity")
@EnableJpaRepositories("com.cybernetics.meetr.repository")
public class DatabaseConfig {
}
