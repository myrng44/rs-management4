package ck4.nvb.rsmanagement.core.web.security.filter;

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

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String token = extractTokenFromRequest(request);

      if (StringUtils.hasText(token)) {
        // validate token and extract user details
        UserRoleDto userRole =
            jwtTokenGenerator.getUserDetailsFromToken(token, rsaKeyProperties.getPublicKey());

        if (userRole != null && userRole.getUserName() != null) {
          // validate token against user details
          Boolean isValid =
              jwtTokenGenerator.validateToken(token, userRole, rsaKeyProperties.getPublicKey());

          if (Boolean.TRUE.equals(isValid)) {
            // create authentication with authorities
            List<SimpleGrantedAuthority> authorities =
                userRole.getPermissions() != null
                    ? userRole.getPermissions().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
                    : List.of(new SimpleGrantedAuthority("ORDER_VIEW"));

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userRole, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug(
                "Authenticated user: {} with authorities: {}", userRole.getUserName(), authorities);
          } else {
            log.warn("Invalid token for user: {}", userRole.getUserName());
          }
        }
      }
    } catch (Exception e) {
      log.error("Error processing JWT token: {}", e.getMessage());
      // Don't throw exception, just continue with unauthenticated request
    }

    filterChain.doFilter(request, response);
  }

  private String extractTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }

    return null;
  }
}
