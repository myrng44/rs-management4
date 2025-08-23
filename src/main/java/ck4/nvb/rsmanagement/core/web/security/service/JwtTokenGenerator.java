package ck4.nvb.rsmanagement.core.web.security.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import ck4.nvb.rsmanagement.core.web.security.service.dto.TokenResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenGenerator {

  @Value("${rs.security.jwt.expirationMillis}")
  private long jwtExpirationMillis;

  private static final String CLAIM_ROLE_ID = "roleId";
  private static final String CLAIM_STORE_ID = "storeId";

  public TokenResponseDto generateToken(UserRoleDto userRoleDto, PrivateKey privateKey) {

    Map<String, Object> claims = new HashMap<>();
    claims.put(CLAIM_ROLE_ID, userRoleDto.getRoleId());
    claims.put(CLAIM_STORE_ID, userRoleDto.getStoreId());

    TokenResponseDto response = new TokenResponseDto();
    response.setIssuedAt(System.currentTimeMillis());
    response.setExpiresIn(jwtExpirationMillis);
    response.setExpiresAt(response.getIssuedAt() + response.getExpiresIn() * 1000);
    response.setTokenType("Bearer");

    String accessToken =
        Jwts.builder()
            .claims(claims)
            .id(createJTI())
            .subject(String.valueOf(userRoleDto.getUserId()))
            .issuedAt(new Date(response.getIssuedAt()))
            .expiration(new Date(response.getExpiresAt()))
            .signWith(privateKey)
            .compact();
    response.setAccessToken(accessToken);

    return response;
  }

  private Claims parseClaims(String token, PublicKey publicKey) throws JwtException {
    try {
      return Jwts.parser()
              .verifyWith(publicKey)
              .build()
              .parseSignedClaims(token)
              .getPayload();
    } catch (JwtException e) {
      throw e;
    }
  }

  public UserRoleDto getUserDetailsFromToken(String token, PublicKey publicKey)
      throws AppException {
    try {
      Claims claims = parseClaims(token, publicKey);

      UserRoleDto userRoleDto = new UserRoleDto();
      userRoleDto.setUserId(Long.valueOf(claims.getSubject()));
      userRoleDto.setRoleId(claims.get(CLAIM_ROLE_ID, Long.class));
      userRoleDto.setStoreId(claims.get(CLAIM_STORE_ID, Long.class));
      //userRoleDto.setUserName(claims.get("username", String.class)); //add username

      return userRoleDto;
    } catch (Exception e) {
      throw new AppException("Invalid token: " + e.getMessage());
    }
  }

  /**
   * Validate token against user details
   *
   * @param token JWT token
   * @param userDetails User role details
   * @param publicKey Public key for verification
   * @return true if token is valid for the user
   */
  public Boolean validateToken(String token, UserRoleDto userDetails, PublicKey publicKey) {
    try {
      Claims claims = parseClaims(token, publicKey);
      return claims != null && claims.getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Extract username from token
   *
   * @param token JWT token
   * @param publicKey Public key for verification
   * @return Username from token
   */
  public String getUsernameFromToken(String token, PublicKey publicKey) {
    try {
      Claims claims = parseClaims(token, publicKey);
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
   * @param token JWT token
   * @param publicKey Public key for verification
   * @return User ID from token
   */
  public Long getUserIdFromToken(String token, PublicKey publicKey) {
    try {
      Claims claims = parseClaims(token, publicKey);
      return claims != null ? Long.valueOf(claims.getSubject()) : null;
    } catch (Exception e) {
      return null;
    }
  }

/*
  public boolean isTokenExpired(String token, PublicKey publicKey) {
    try {
      Claims claims = parseClaims(token, publicKey);

      return claims.getExpiration().before(new Date());
    } catch (Exception e) {
      return true;
    }
  }
*/

  private static String createJTI() {
    return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
  }
}
