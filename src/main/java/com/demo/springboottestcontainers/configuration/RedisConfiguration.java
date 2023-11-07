package com.demo.springboottestcontainers.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.StringUtils;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

    @Autowired
    private RedisProperties redisProperties;
 
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisStandaloneConfiguration());
    }
 
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<Long, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean("jsonRedisTemplate")
    public RedisTemplate<Long, Object> jsonRedisTemplate() {
        RedisTemplate<Long, Object> template = new RedisTemplate<>();
        template.setKeySerializer(RedisSerializer.json());
        template.setValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(RedisSerializer.json());
        template.setHashKeySerializer(RedisSerializer.json());
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    private RedisStandaloneConfiguration redisStandaloneConfiguration() {

        RedisStandaloneConfiguration redisStandaloneConfiguration
                = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());

        if (StringUtils.hasText(redisProperties.getPassword())) {
            redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
        }
        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());

        return redisStandaloneConfiguration;
    }
 
}