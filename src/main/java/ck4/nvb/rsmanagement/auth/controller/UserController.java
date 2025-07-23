//package ck4.nvb.rsmanagement.controller;
//
//import ck4.nvb.rsmanagement.io.ProfileResponse;
//import ck4.nvb.rsmanagement.io.ProfileRequest;
//import ck4.nvb.rsmanagement.entity.Users;
//import ck4.nvb.rsmanagement.service.UserService;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//public class UserController {
//
//    @Autowired
//    UserService userService;
//
//    @GetMapping("/")
//    public List<Users> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestBody Users user, HttpServletResponse response) {
//
//        //trả về ResponseEntity<?>
////        //verify user trả về 1 jwt token
////        String jwt = userService.verify(user);
////
////
////        // 3. Tạo HttpOnly Cookie
////        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
////                .httpOnly(true)
////                .secure(false) // Để true nếu dùng HTTPS
////                .path("/")
////                .maxAge(24 * 60 * 60) // 1 ngày
////                .sameSite("Lax")
////                .build();
////
////        // 4. Gửi cookie về client
////        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
////        // 5. Trả về thông tin user (nếu cần)
////        return ResponseEntity.ok("Đăng nhập thành công");
//        return userService.verify(user);
//    }
//
//
//
//    @PostMapping("/add")
//    public Users post(@RequestBody ProfileResponse dto) {
//        return userService.saveUserDTO(dto);
//    }
//
//
//    @GetMapping("/dto")
//    public List<ProfileRequest> getUsers() {
//        return userService.getAllUserDTO();
//    }
//}
