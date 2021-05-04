package com.witherview.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class JwtUtilsTest {
    @Autowired
    GenerateRandomId generateRandomId;

    @Test
    public void TestCreateToken() {
        String email1 = "test@test.com";
        String userId1 = generateRandomId.generateId();

        String email2 = "test2@test.com";
        String userId2 = generateRandomId.generateId();

        assertNotEquals(userId1, userId2);

        String token1 = new JwtUtils().createToken(email1, userId1);
        String token2 = new JwtUtils().createToken(email2, userId2);

        assertNotEquals(token1, token2);
    }

    @Test
    public void TestIsTokenExpired() {
        String email1 = "test@test.com";
        String userId1 = generateRandomId.generateId();

        String token1 = new JwtUtils().createToken(email1, userId1);
        assertFalse(new JwtUtils().isTokenExpired(token1));
    }
}