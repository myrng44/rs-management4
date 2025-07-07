package ck4.nvb.rsmanagement.core.web.security.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import ck4.nvb.rsmanagement.core.web.security.service.dto.TokenResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

@Component
public class JwtTokenGenerator {

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public TokenResponseDto generateToken(UserRoleDto userRoleDto, PrivateKey privateKey) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userRoleDto.getUserId());
        claims.put("roleId", userRoleDto.getRoleId());
        claims.put("storeId", userRoleDto.getStoreId());
        claims.put("username", userRoleDto.getUsername()); // Add username for validation
        if (userRoleDto.getFullName() != null) {
            claims.put("fullName", userRoleDto.getFullName());
        }

        TokenResponseDto response = new TokenResponseDto();
        response.setIssuedAt(System.currentTimeMillis());
        response.setExpiresIn(jwtExpiration);
        response.setExpiresAt(response.getIssuedAt() + response.getExpiresIn() * 1000);
        response.setTokenType("Bearer");


        String accessToken = Jwts.builder()
                .claims(claims)
                .id(createJTI())
                .subject(userRoleDto.getUserId().toString())
                .issuedAt(new Date(response.getIssuedAt()))
                .expiration(new Date(response.getExpiresAt()))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
        response.setAccessToken(accessToken);

        return response;
    }

    public UserRoleDto getUserDetailsFromToken(String token, PublicKey publicKey) throws AppException {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getPayload();

            UserRoleDto userRoleDto = new UserRoleDto();
            userRoleDto.setUserId(Long.valueOf(claims.get("userId", String.class)));
            userRoleDto.setRoleId(Long.valueOf(claims.get("roleId", String.class)));
            userRoleDto.setStoreId(Long.valueOf(claims.get("storeId", String.class)));
            userRoleDto.setUsername(claims.get("username", String.class)); // Add username

            return userRoleDto;
        } catch (Exception e) {
            throw new AppException("Invalid token: " + e.getMessage());
        }
    }

    /**
     * Validate token against user details
     *
     * @param token       JWT token
     * @param userDetails User role details
     * @param publicKey   Public key for verification
     * @return true if token is valid for the user
     */
    public Boolean validateToken(String token, UserRoleDto userDetails, PublicKey publicKey) {
        try {
            String username = getUsernameFromToken(token, publicKey);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, publicKey));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extract username from token
     *
     * @param token     JWT token
     * @param publicKey Public key for verification
     * @return Username from token
     */
    public String getUsernameFromToken(String token, PublicKey publicKey) {
        try {
            Claims claims = getClaimsFromToken(token, publicKey);
            if (claims != null) {
                // Try to get username from claims first, fallback to subject
                String username = claims.get("username", String.class);
                if (username != null) {
                    return username;
                }
                // If username not in claims, use subject (userId) and get username from service
                return claims.getSubject();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extract user ID from token
     *
     * @param token     JWT token
     * @param publicKey Public key for verification
     * @return User ID from token
     */
    public Long getUserIdFromToken(String token, PublicKey publicKey) {
        try {
            Claims claims = getClaimsFromToken(token, publicKey);
            if (claims != null) {
                String userId = claims.get("userId", String.class);
                return userId != null ? Long.valueOf(userId) : null;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenExpired(String token, PublicKey publicKey) {
        try {
            Claims claims = getClaimsFromToken(token, publicKey);

            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    private Claims getClaimsFromToken(String token, PublicKey publicKey) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(publicKey).build().parseClaimsJws(token).getPayload();
        } catch (Exception e) {
            //logger.error("Error parsing token: {}", e.getMessage());
            claims = null;
        }
        return claims;
    }

    private static String createJTI() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }
}
