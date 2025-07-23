package ck4.nvb.rsmanagement.auth.controller;

import ck4.nvb.rsmanagement.auth.io.ProfileRequest;
import ck4.nvb.rsmanagement.auth.io.ProfileResponse;
import ck4.nvb.rsmanagement.auth.service.ProfileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @PostMapping("/register")
    public ProfileResponse register(@RequestBody ProfileRequest profileRequest) {
        return profileService.createProfile(profileRequest);
    }

    @GetMapping("/profile")
    public ProfileResponse getProfile(@CurrentSecurityContext(expression = "authentication?.name") String username) {
        return profileService.getProfile(username);
    }
    //expression: là 1 annotation của spring security dùng để lấy thông tin hiện tại từ SecurityContext (ngữ caảnh bảo mật)
    //expression = "authentication?.name" là 1 SpEL (Spring expression Language.

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully");
    }
}
