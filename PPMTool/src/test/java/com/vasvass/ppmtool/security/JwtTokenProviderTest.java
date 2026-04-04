package com.vasvass.ppmtool.security;

import com.vasvass.ppmtool.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenProviderTest {

    // Must be >= 32 chars (256 bits) for HS256
    private static final String SECRET = "TestSecretKeyForJWTTokenProviderTesting123456";
    private static final int EXPIRATION_MS = 3_600_000;

    private JwtTokenProvider tokenProvider;
    private User user;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret", SECRET);
        ReflectionTestUtils.setField(tokenProvider, "jwtExpirationInMs", EXPIRATION_MS);

        user = new User();
        user.setId(42L);
        user.setUsername("user@example.com");
        user.setFullName("Test User");
        user.setPassword("encoded");
    }

    private Authentication mockAuthentication() {
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);
        return auth;
    }

    @Test
    void generateToken_returnsNonEmptyToken() {
        String token = tokenProvider.generateToken(mockAuthentication());

        assertThat(token).isNotBlank();
    }

    @Test
    void generateToken_validToken_validateReturnsTrue() {
        String token = tokenProvider.generateToken(mockAuthentication());

        assertThat(tokenProvider.validateToken(token)).isTrue();
    }

    @Test
    void getUserIdFromJWT_returnsCorrectUserId() {
        String token = tokenProvider.generateToken(mockAuthentication());

        Long userId = tokenProvider.getUserIdFromJWT(token);

        assertThat(userId).isEqualTo(42L);
    }

    @Test
    void validateToken_invalidToken_returnsFalse() {
        assertThat(tokenProvider.validateToken("not.a.valid.token")).isFalse();
    }

    @Test
    void validateToken_tamperedToken_returnsFalse() {
        String token = tokenProvider.generateToken(mockAuthentication());
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";

        assertThat(tokenProvider.validateToken(tampered)).isFalse();
    }

    @Test
    void validateToken_emptyString_returnsFalse() {
        assertThat(tokenProvider.validateToken("")).isFalse();
    }

    @Test
    void generateToken_differentUsersProduceDifferentTokens() {
        String token1 = tokenProvider.generateToken(mockAuthentication());

        User user2 = new User();
        user2.setId(99L);
        user2.setUsername("other@example.com");
        user2.setFullName("Other User");
        user2.setPassword("encoded");

        Authentication auth2 = mock(Authentication.class);
        when(auth2.getPrincipal()).thenReturn(user2);
        String token2 = tokenProvider.generateToken(auth2);

        assertThat(token1).isNotEqualTo(token2);
    }
}
