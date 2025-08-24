package ck4.nvb.rsmanagement.core.web.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Value("${jwt.secret}")
  private String JWT_SECRET;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
    String token = extractToken(request);
    if (token != null && validateToken(token)) {
      Claims claims = getClaims(token);
      String username = claims.getSubject();
      List<String> roles = claims.get("roles", List.class);
      List<String> permissions = claims.get("permissions", List.class);

      if (roles == null) roles = new ArrayList<>();
      if (permissions == null) permissions = new ArrayList<>();

      List<SimpleGrantedAuthority> authorities = new ArrayList<>();
      authorities.addAll(roles.stream().map(SimpleGrantedAuthority::new).
              collect(Collectors.toList()));
      authorities.addAll(permissions.stream().map(SimpleGrantedAuthority::new).
              collect(Collectors.toList()));

      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
              username, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
    filterChain.doFilter(request, response);
  }

  private String extractToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  private boolean validateToken(String token) {
    try {
      getClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private Claims getClaims(String token) {
    // Tạo SecretKey theo cách khuyến nghị
    byte[] keyBytes = Base64.getDecoder().decode(JWT_SECRET);
    SecretKey key = Keys.hmacShaKeyFor(keyBytes);

    // Sử dụng API mới của JJWT 0.12.x
    return Jwts.parser()               // <- parser() (mới/không deprecated ở 0.12.x)
            .verifyWith(key)           // verify chữ ký
            .build()
            .parseSignedClaims(token)  // parse signed JWT
            .getPayload();
  }
}