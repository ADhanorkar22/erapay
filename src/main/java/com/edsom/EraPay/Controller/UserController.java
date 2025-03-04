package com.edsom.EraPay.Controller;

import com.edsom.EraPay.Dtos.DepositCoinsDto;
import com.edsom.EraPay.Dtos.FundTransferDto;
import com.edsom.EraPay.Dtos.UserUpdateDto;
import com.edsom.EraPay.EasbuzzUtil.PaymentRequest;
import com.edsom.EraPay.GlobalUtils.ResponseUtil;
import com.edsom.EraPay.Security.JwtUtils;
import com.edsom.EraPay.Service.EaseBuzzPayInService;
import com.edsom.EraPay.Service.UserService;
import com.edsom.EraPay.ServiceImpl.EmailServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


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

    private static final String UPLOADED_FOLDER = "uploads/tickets/";
    private Long ticketId;

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
    public ResponseEntity<?> depositCoins(@RequestHeader("Authorization") String token, @Valid @RequestBody DepositCoinsDto depositDto,
                                          @RequestPart("screenshot") MultipartFile file) {
        String t = token.substring(7);
        Claims claims = jwtUtils.extractAllClaims(t);
        String userid = claims.get("userid", String.class);
        try {
            File directory = new File(UPLOADED_FOLDER);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // Get the file name and path
            String fileName = file.getOriginalFilename();
            Path uploadbankReceipt = Paths.get(UPLOADED_FOLDER + fileName); // Save the file to the specified

            // Save file path
            Files.write(uploadbankReceipt, file.getBytes());

            return userService.depositCoins(userid, depositDto, fileName);
        } catch (Exception e) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", false);
            resp.put("error", e.getLocalizedMessage());
            return ResponseUtil.buildResponse("Something went wrong..!!", HttpStatus.INTERNAL_SERVER_ERROR, resp);
        }
    }

    @GetMapping("/mydeposits")
    public ResponseEntity<?> myDeposits(@RequestHeader("Authorization") String token, @RequestHeader(value = "currPage") Integer currPage, @RequestHeader(value = "pageSize") Integer pageSize){
        String t = token.substring(7);
        Claims claims = jwtUtils.extractAllClaims(t);
        String userid = claims.get("userid", String.class);
        return userService.myDeposits(userid,currPage,pageSize);
    }
}
