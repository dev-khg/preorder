package com.example.preorder.utils;

import com.example.preorder.common.utils.RedisKeyGenerator;
import com.example.preorder.global.jwt.TokenProvider;
import com.example.preorder.global.redis.RedisManager;
import com.example.preorder.user.core.entity.UserEntity;
import com.example.preorder.user.core.repository.UserRepository;
import com.example.preorder.user.core.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.preorder.common.utils.RedisKeyGenerator.RedisKeyType.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Disabled
@AutoConfigureMockMvc
@Transactional
public abstract class IntegrationTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected RedisManager redisManager;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected TokenProvider tokenProvider;
    @PersistenceContext
    protected EntityManager em;

    protected UserEntity userEntity;
    protected String password;
    protected UserEntity otherUserEntityA;
    protected UserEntity otherUserEntityB;

    @BeforeEach
    void beforeEach() {
        password = "ABCDEFGH";
        userEntity = UserEntity.createUser(
                "ABCEEFG@email.com",
                createRandomUUID(),
                passwordEncoder.encode(password),
                createRandomUUID()
        );
        otherUserEntityA = UserEntity.createUser(
                "ABCEEFG1@email.com",
                createRandomUUID(),
                passwordEncoder.encode(password),
                createRandomUUID()
        );
        otherUserEntityB = UserEntity.createUser(
                "ABCEEFG2@email.com",
                createRandomUUID(),
                passwordEncoder.encode(password),
                createRandomUUID()
        );
        userEntity = userRepository.save(userEntity);
        otherUserEntityA = userRepository.save(otherUserEntityA);
        otherUserEntityB = userRepository.save(otherUserEntityB);
        accessToken = tokenProvider.createAccessToken(userEntity.getEmail());
        refreshToken = tokenProvider.createRefreshToken(userEntity.getEmail());
        redisManager.putValue(REFRESH_TOKEN, userEntity.getEmail(), refreshToken);
        em.flush();
        em.clear();
    }

    protected String accessToken;
    protected String refreshToken;

    protected static String createRandomUUID() {
        return UUID.randomUUID().toString();
    }

    protected MockMultipartFile objectToMultiPart(String name, Object object) throws JsonProcessingException {
        String jsonBody = objectMapper.writeValueAsString(object);

        return new MockMultipartFile(name, "", "application/json", jsonBody.getBytes(StandardCharsets.UTF_8));
    }

    protected void checkHasHeader(MvcResult mvcResult, String name) {
        assertNotEquals(mvcResult.getResponse().getHeaders(name).size(), 0);
    }

    protected void checkHeaderNull(MvcResult mvcResult, String name) {
        assertEquals(mvcResult.getResponse().getHeaders(name).size(), 0);
    }

    protected void checkHasCookie(MvcResult mvcResult, String name) {
        assertNotNull(mvcResult.getResponse().getCookie(name));
    }

    protected void checkCookieNull(MvcResult mvcResult, String name) {
        assertNull(mvcResult.getResponse().getCookie(name));
    }

    private void init() {

    }
}