package com.edsom.EraPay.Controller;

import com.edsom.EraPay.Dtos.FundTransferDto;
import com.edsom.EraPay.Dtos.UserUpdateDto;
import com.edsom.EraPay.EasbuzzUtil.PaymentRequest;
import com.edsom.EraPay.Security.JwtUtils;
import com.edsom.EraPay.Service.EaseBuzzPayInService;
import com.edsom.EraPay.Service.UserService;
import com.edsom.EraPay.ServiceImpl.EmailServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    UserService userService;

    @Autowired
    EaseBuzzPayInService easeBuzzPayInService;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/checkemail")
    public ResponseEntity<?> checkEmail(@RequestHeader String email) {
        System.out.println(email);
        return userService.availableEmail(email);
    }

    @GetMapping("/checkmobile")
    public ResponseEntity<?> checkMobile(@RequestHeader String mobile) {
        return userService.availableMobile(mobile);
    }

    @GetMapping("/checkadhaar")
    public ResponseEntity<?> checkAdhaar(@RequestHeader String adhaar) {
        return userService.availableAdhaar(adhaar);
    }

    @GetMapping("/checkpan")
    public ResponseEntity<?> checkPan(@RequestHeader String pan) {
        return userService.availablePan(pan);
    }

    @PostMapping("/fundtransfer")
    public ResponseEntity<?> fundTransfer(@RequestHeader("Authorization") String token, @RequestBody FundTransferDto dto) {
        String t = token.substring(7);
        Claims claims = jwtUtils.extractAllClaims(t);
        String userid = claims.get("userid", String.class);
        return userService.fundTransfer(userid, dto);
    }

    @PostMapping("/payin")
    public ResponseEntity<?> payIn(@RequestHeader("Authorization") String token,@RequestBody PaymentRequest req){
        return easeBuzzPayInService.initiatePayment(req);
    }

    @PostMapping("/cardapply")
    public ResponseEntity<?> cardApply(@RequestHeader(value="cardtype") String cardtype, @RequestHeader("Authorization") String token){
        String t = token.substring(7);
        Claims claims = jwtUtils.extractAllClaims(t);
        String userid = claims.get("userid", String.class);
        return userService.cardApply(userid,cardtype);
    }

    @GetMapping("/payinreport")
    public ResponseEntity<?> payIn(@RequestHeader("Authorization") String token, @RequestHeader(value = "currPage") Integer currPage, @RequestHeader(value = "pageSize") Integer pageSize){
        String t = token.substring(7);
        Claims claims = jwtUtils.extractAllClaims(t);
        String userid = claims.get("userid", String.class);
        return userService.payinReports(userid,currPage,pageSize);
    }

    @PostMapping("/updateuser")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UserUpdateDto dto){
        return userService.updateUser(dto);
    }

    @GetMapping("/fetchbalance")
    public ResponseEntity<?> fetchBalane(@RequestHeader("Authorization") String token){
        String t = token.substring(7);
        Claims claims = jwtUtils.extractAllClaims(t);
        String userid = claims.get("userid", String.class);
        return userService.fetchBalance(userid);
    }

    @GetMapping("/mydetails")
    public ResponseEntity<?> fetchMyInfo(@RequestHeader("Authorization") String token){
        String t = token.substring(7);
        Claims claims = jwtUtils.extractAllClaims(t);
        String userid = claims.get("userid", String.class);
        return userService.myInfo(userid);
    }

    @GetMapping("/mycard")
    public ResponseEntity<?> myCard(@RequestHeader("Authorization") String token){
        String t = token.substring(7);
        Claims claims = jwtUtils.extractAllClaims(t);
        String userid = claims.get("userid", String.class);
        return userService.myCard(userid);
    }

    @GetMapping(path="/getcount")
    public ResponseEntity<?>getUsersCount(){
    return userService.getUsersCount();
}

    @PostMapping("/depositcoins")
    public ResponseEntity<?> depositCoins(@RequestHeader("Authorization") String token, @Valid @RequestBody UserUpdateDto dto){
        String t = token.substring(7);
        Claims claims = jwtUtils.extractAllClaims(t);
        String userid = claims.get("userid", String.class);
        return userService.updateUser(dto);
    }
}
