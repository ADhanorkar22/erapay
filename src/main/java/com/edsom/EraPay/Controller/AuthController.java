package com.edsom.EraPay.Controller;

import com.edsom.EraPay.Dtos.SignInDto;
import com.edsom.EraPay.Service.UserService;
import com.edsom.EraPay.ServiceImpl.AuthService;
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
    public ResponseEntity<String> resetPassword(@RequestBody Map<String,String> data) {
        System.out.println(data);
        userService.resetPassword(data.get("token"), data.get("newPassword"));
        return ResponseEntity.ok("Password successfully reset.");
    }
}
