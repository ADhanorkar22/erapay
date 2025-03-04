package com.edsom.EraPay.Controller;

import com.edsom.EraPay.Dtos.SignInDto;
import com.edsom.EraPay.Dtos.UserRegDto;
import com.edsom.EraPay.Service.UserService;
import com.edsom.EraPay.ServiceImpl.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    com.edsom.EraPay.Service.Admin adminService;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInDto signInDto)
    {
        return ResponseEntity.ok( authService.empSignIn(signInDto));
    }

    @GetMapping("/hello")
    public String systemCheck() {
        return "Yess Hello..!!";
    }

    @GetMapping("/checkpassword")
    public ResponseEntity<?> checkPassword(@RequestHeader String email) {
        return userService.checkEmail(email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String,String> data) {
        System.out.println(data);
        return userService.resetPassword(data.get("token"), data.get("newPassword"));
    }

    @PostMapping(path="/forget-password")
    public ResponseEntity<?>forgetPassword(@RequestHeader String email) {
        return userService.forgetPassword(email);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegDto dto) {
        return adminService.register(dto);
    }

}
