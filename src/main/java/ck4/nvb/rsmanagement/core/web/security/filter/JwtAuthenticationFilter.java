package ck4.nvb.rsmanagement.core.web.security.filter;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.module.users.user.service.impl.UserGetServiceWithRoleImpl;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import ck4.nvb.rsmanagement.core.web.security.service.JwtTokenGenerator;
import ck4.nvb.rsmanagement.core.web.security.service.rsa.RSAKeyProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenGenerator jwtTokenGenerator;
  private final RSAKeyProperties rsaKeyProperties;
  @Autowired
  private UserGetServiceWithRoleImpl userGetServiceWithRole;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String token = extractTokenFromRequest(request);

      if (StringUtils.hasText(token)) {
        authenticateUser(token);
      }
    } catch (Exception e) {
      log.error("Authentication error: {}", e.getMessage());
      SecurityContextHolder.clearContext();
      // Không throw exception, continue with unauthenticated request
    }

    filterChain.doFilter(request, response);
  }

  private String extractTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7).trim();
    }

    return null;
  }

  private void authenticateUser(String token) throws AppException {
    // Lấy basic info từ token
    UserRoleDto tokenUserRole = jwtTokenGenerator.getUserDetailsFromToken(
            token, rsaKeyProperties.getPublicKey());

    if (tokenUserRole.getUserId() == null) {
      throw new AppException("Invalid token");
    }

    // Get complete user info with permissions from database
    UserRoleDto fullUserRole = getUserRoleFromDatabase(tokenUserRole);

    // Create Spring Security authentication with permissions
    List<SimpleGrantedAuthority> authorities = fullUserRole.getPermissions()
            .stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    // Always ensure user has basic role
    if (authorities.isEmpty()) {
      authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(fullUserRole, null, authorities);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    log.debug("User {} authenticated with {} permissions at store {}",
            fullUserRole.getUserName(), authorities.size(), fullUserRole.getStoreId());
  }

  private UserRoleDto getUserRoleFromDatabase(UserRoleDto tokenUserRole) throws AppException {
    try {
      // if token have storeId, lấy role cho specific store
      if (tokenUserRole.getStoreId() != null) {
        return userGetServiceWithRole.getUserSession(
                tokenUserRole.getUserId(), tokenUserRole.getStoreId());
      } else {
        // Otherwise lại lấy primary role
        return userGetServiceWithRole.get(tokenUserRole.getUserId());
      }
    } catch (AppException e) {
      log.warn("Failed to get user role from database: {}", e.getMessage());
      throw new AppException("User authentication failed: " + e.getMessage());
    }
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();

    // Skip filter cho public endpoints
    return path.equals("/public/rest/v1/auth/login")
            || path.equals("/public/rest/v1/auth/register")
            || path.equals("/public/rest/v1/auth/refresh")
            || path.startsWith("/actuator/")
            || path.equals("/health");
  }
}
