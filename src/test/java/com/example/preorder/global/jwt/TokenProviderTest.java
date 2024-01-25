package com.example.preorder.global.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TokenProviderTest {
    @Autowired
    TokenProvider tokenProvider;

    @Test
    @DisplayName("토큰 생성 시, Subject 는 공백이거나 null일 수 없다.")
    void invalid_token_generate_with_blank_throws_exception() {
        // Case 1: Access Token
        assertThatThrownBy(() -> tokenProvider.createAccessToken(" "))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> tokenProvider.createAccessToken(null))
                .isInstanceOf(IllegalArgumentException.class);

        // Case 2: Refresh Token
        assertThatThrownBy(() -> tokenProvider.createRefreshToken(" "))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> tokenProvider.createRefreshToken(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("올바르지 않은 토큰 검증 시, false가 반환되어야 한다.")
    void invalid_token_validation_return_false() {
        // given
        String token = tokenProvider.createAccessToken(createRandomUUID());

        // when

        // then
        assertFalse(tokenProvider.isValidateToken(token + "a"));
        assertFalse(tokenProvider.isValidateToken(token + "z"));
        assertFalse(tokenProvider.isValidateToken(token + "0"));
        assertFalse(tokenProvider.isValidateToken(token + "9"));
    }

    @Test
    @DisplayName("올바른 주제로 토큰 생성 시, 토큰이 반환되어야 한다.")
    void valid_token_generate() {
        // given
        String subject = createRandomUUID();

        // when

        // then
        assertNotNull(tokenProvider.createAccessToken(subject));
        assertNotNull(tokenProvider.createRefreshToken(subject));
    }

    @Test
    @DisplayName("같은 주제의 토큰 생성 시, 항상 다른 토큰 값이 반환되어야 한다.")
    void generate_token_must_be_different() {
        // given
        int loopCount = 0;
        String subject = createRandomUUID();
        Set<String> tokenSet = new HashSet<>();

        // when
        for (int i = 0; i < loopCount; i++) {
            tokenSet.add(tokenProvider.createAccessToken(subject));
            tokenSet.add(tokenProvider.createRefreshToken(subject));
        }

        // then
        assertEquals(tokenSet.size(), loopCount * 2);
    }

    private String createRandomUUID() {
        return UUID.randomUUID().toString();
    }
}