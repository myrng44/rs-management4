package ck4.nvb.rsmanagement.core.auth.filter;
import ck4.nvb.rsmanagement.core.auth.service.AppUsersDetailService;
import ck4.nvb.rsmanagement.core.auth.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

/**
 * lớp này là 1 custom filer mở rộng OncePerRequestFilter được spring gọi 1 lần trên mỗi request. Nó dùng để
 * đọc JWT từ header hoặc cookie
 * giải mã xác thực jwt
 * nếu hợp lễ tạo ra 1 authentication và set vào SecurityContextHolder
 */

@Component
public class JwtFilterRequest extends OncePerRequestFilter {

    /**
     * dùng để lấy thông tin người dùng từ db
     */
    @Autowired
    private AppUsersDetailService appUserDetailService;

    /**
     * để (phân tích)parseJWT, trích xuất email, validate token
     */
    @Autowired
    private JwtUtil jwtUtil;

    private static final List<String> PUBLIC_URLS = List.of("/login", "/register", "/send-reset-otp", "reset-password", "/logout");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /**
         * neeu request nằm trogn danh sách public_URL, bỏ qua xử lý filter chuyển sang filter tiếp
         */
        String path = request.getServletPath();
            if (PUBLIC_URLS.contains(path)) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = null;
            String email = null;

            //1. check the authorization header
        /**
         * tìm jwt ở Authorization
         */
            final String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer  ")) {
                jwt = authorizationHeader.substring(7);
            }

            //2. if not found in header, check cookies
        /**
         * tìm trong cookie nếu không có trong header
         */
            if (jwt == null) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if ("jwt".equals(cookie.getName())) {
                            jwt = cookie.getValue();
                            break;
                        }
                    }
                }
            }

            //3. validate the token and set security context
        /**
         * giải mã jwt và xác thực
         */
            if (jwt != null) {
                /// giải mã email
                email = jwtUtil.extractEmail(jwt);

                /// kiểm tra user xác thực chưa nếu contextholder rỗng thì mới set
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    /// lấy userData từ db
                    UserDetails userDetails = appUserDetailService.loadUserByUsername(email);
                    /// kiểm tra jwt
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        /// tạo 1 authentication set vào context holder
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,  null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
            /// kết thúc filter
            filterChain.doFilter(request, response);
    }
}