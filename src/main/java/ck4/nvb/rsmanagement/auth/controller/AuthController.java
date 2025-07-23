package ck4.nvb.rsmanagement.auth.controller;

import ck4.nvb.rsmanagement.auth.io.AuthRequest;
import ck4.nvb.rsmanagement.auth.service.AppUsersDetailService;
import ck4.nvb.rsmanagement.auth.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    @Autowired
    private AppUsersDetailService appUsersDetailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authenticate(request.getUsername(), request.getPassword()); // xác thực username, password
            final UserDetails userDetails = appUsersDetailService.loadUserByUsername(request.getUsername()); // tải thông tin người dùng từ db trong đó UserDetails là object chuẩn trong Spring Securtiy
            final String jwtToken = jwtUtil.generateToken(userDetails);// sinh jwt token
            ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken) // tạo cooki chứa jwt token
                    .httpOnly(true) //bảo mật JS không thể đọc được cookie (chống XSS)
                    .path("/") // cookie dùng cho toàn bộ hệ thống
                    .maxAge(Duration.ofDays(1)) // thời gian sống cuủa cookie 1 ngaày
                    .sameSite("Strict") // chống CSRF, chỉ cho gửi cookie cùng origin
                    .build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())// header
                    .body(new AuthRequest(request.getUsername(), jwtToken)); // trả về response kèm cookie và token
        } catch (BadCredentialsException ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "true");
            error.put("message", "Email or password is incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (DisabledException ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "true");
            error.put("message", "Account is disabled");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "true");
            error.put("message", "Authentication failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
    //tham so của phương thức trên là 1 Authentication Request Object, ta phải tạo thêm 1 class Authentication Request Object

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        //authencicate trả về 1 đối tượng AUthentication và nhận vào 1 tham số là 1 đói tươg Authentication
        // ở đây UsernamePasswordAuthenticationToken extend AbstractAuthenticationToken là 1 implement của Authentication;
    }
}
