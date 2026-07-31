package com.careeros.auth;

import com.careeros.auth.dto.event.AuthEvent;
import com.careeros.auth.repository.RefreshTokenRepository;
import com.careeros.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceApplicationTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @MockBean(name = "redisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @MockBean
    private KafkaTemplate<String, AuthEvent> kafkaTemplate;

    @Test
    void contextLoads() {
    }
}
