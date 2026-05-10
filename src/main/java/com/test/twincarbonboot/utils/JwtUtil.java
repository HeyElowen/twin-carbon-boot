package com.test.twincarbonboot.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    /**
     * JWT 签名密钥（长度 >= 256 bits）
     * 生产环境建议放到 application.yml 里，不要硬编码
     */
    private static final String SECRET = "TwinCarbonBootSecretKeyForJWTGeneration2024!!!";

    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /** Token 有效期：24 小时 */
    private static final long EXPIRATION = 24 * 60 * 60 * 1000;

    /**
     * 生成 JWT Token
     */
    public static String generateToken(Integer userId, String username) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(expire)
                .signWith(KEY)
                .compact();
    }

    /**
     * 验证 Token 是否有效（未过期、未被篡改）
     */
    public static boolean validate(String token) {
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            // 已过期
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            // 格式错误、签名不对等
            return false;
        }
    }

    /**
     * 解析 Token，获取 Claims
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从 Token 中获取用户名
     */
    public static String getUsername(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * 从 Token 中获取用户ID
     */
    public static Integer getUserId(String token) {
        Object userId = parseToken(token).get("userId");
        return userId != null ? Integer.valueOf(userId.toString()) : null;
    }
}
