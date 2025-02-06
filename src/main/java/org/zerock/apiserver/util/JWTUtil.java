package org.zerock.apiserver.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

public class JWTUtil {
    // 비밀 키 정의 (HMAC-SHA 기반)
    private static final String key = "1234567890123456789012345678901234567890";

    // JWT 토큰 생성 메서드
    public static String generateToken(Map<String, Object> valueMap, int min) {
        SecretKey secretKey;
        try {
            // HMAC-SHA 키 생성
            secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate key: " + e.getMessage());
        }

        // JWT 생성
        return Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setClaims(valueMap)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(secretKey)
                .compact();
    }

    // JWT 토큰 검증 메서드
    public static Map<String, Object> validateToken(String token) {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

            // 토큰 파싱 및 검증
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token) // 파싱 및 검증, 실패시 에러
                    .getBody();
        } catch (MalformedJwtException e) {
            throw new CustomJWTException("Malformed JWT token");
        } catch (ExpiredJwtException e) {
            throw new CustomJWTException("JWT token has expired");
        } catch (UnsupportedJwtException e) {
            throw new CustomJWTException("Unsupported JWT token");
        } catch (JwtException e) {
            throw new CustomJWTException("Invalid JWT token");
        } catch (Exception e) {
            throw new CustomJWTException("Unknown error occurred while validating JWT");
        }
    }
}

