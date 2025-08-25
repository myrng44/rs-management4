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

      return userRoleDto;
    } catch (Exception e) {
      throw new AppException("Invalid token: " + e.getMessage());
    }
  }

  private static String createJTI() {
    return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
  }
}
