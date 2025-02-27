package com.edsom.EraPay.ServiceImpl;

import com.edsom.EraPay.Dtos.FundTransferDto;
import com.edsom.EraPay.Dtos.UserRegDto;
import com.edsom.EraPay.Entities.CardApply;
import com.edsom.EraPay.Entities.Role;
import com.edsom.EraPay.Entities.User;
import com.edsom.EraPay.Enums.UserStatus;
import com.edsom.EraPay.GlobalUtils.ResponseUtil;
import com.edsom.EraPay.Repos.CardApplyRepo;
import com.edsom.EraPay.Repos.RoleRepo;
import com.edsom.EraPay.Repos.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static com.edsom.EraPay.ServiceImpl.EmailServiceImpl.parseToken;


@Service
public class UserService implements com.edsom.EraPay.Service.UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    CardApplyRepo cardApplyRepo;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    private String generateUserId(String adhaar) {
        if (adhaar == null || adhaar.length() != 12 || !adhaar.matches("\\d{12}")) {
            throw new IllegalArgumentException("Invalid Aadhaar number");
        }

        Random random = new Random();
        int startIndex = random.nextInt(4); // Random index between 0 and 4 (inclusive)
        String randomDigits = adhaar.substring(startIndex, startIndex + 6);
        return "ERA" + randomDigits; // Prefix with "UID" (you can change it as needed)
    }

    @Override
    public ResponseEntity<?> register(UserRegDto dto) {
        String userid = generateUserId(dto.getAdhaar());
        Optional<Role> roleOpt = roleRepo.findById(2);
        Role role = roleOpt.get();
        User checkUser = userRepo.findByEmailOrMobileOrAdhaarOrPan(dto.getEmail(), dto.getMobile(), dto.getAdhaar(), dto.getPan());
        Map<String, Object> resp = new HashMap<>();
        if (checkUser != null) {
            resp.put("success", false);
            resp.put("message", "User already present..!! Try resetting you password..");
            return ResponseUtil.buildResponse("Constraint Violation: ", HttpStatus.NOT_ACCEPTABLE, Map.of("error", resp));
        }
        User newUser = new User(userid, dto.getName(), dto.getEmail(), dto.getPan(), dto.getMobile(), dto.getAdhaar(), UserStatus.ACTIVE, LocalDateTime.now(), dto.getDob(), dto.getWalletAddress(), 0.0, role);
        User savedUser = userRepo.save(newUser);
        resp.put("success", true);
        resp.put("user", savedUser);
        return ResponseUtil.buildResponse("User registered successfully.", HttpStatus.CREATED, Map.of("response", resp));
    }

    @Override
    public ResponseEntity<?> checkEmail(String email) {
        try {
            User u = userRepo.findByEmail(email);
            Map<String, Boolean> response = new HashMap<>();
            if (u != null && (!"null".equals(u.getPassword()))) {
                response.put("emailValidated", true);
                response.put("setPassword", true);
                return ResponseUtil.buildResponse("success", HttpStatus.OK,
                        Map.of("data", response)
                );
            } else if (u != null && ("null".equals(u.getPassword()))) {
                response.put("emailValidated", true);
                response.put("setPassword", false);
                return ResponseUtil.buildResponse("success", HttpStatus.OK,
                        Map.of("data", response)
                );
            } else {
                response.put("emailValidated", false);
                response.put("setPassword", false);
                return ResponseUtil.buildResponse("success", HttpStatus.OK,
                        Map.of("data", response)
                );
            }
        } catch (Exception e) {
            return ResponseUtil.buildResponse("Something went wrong please try again", HttpStatus.BAD_REQUEST, Map.of("error", e.getLocalizedMessage())
            );
        }
    }

    @Override
    public ResponseEntity<?> availableEmail(String email) {
        User u = userRepo.findByEmail(email);
        Map<String, Object> resp = new HashMap<>();
        if (u != null) {
            resp.put("success", false);
            resp.put("message", "User already present..!! Try resetting you password..");
            return ResponseUtil.buildResponse("Constraint Violation: ", HttpStatus.NOT_ACCEPTABLE, Map.of("error", resp));
        }

        resp.put("success", true);
        return ResponseUtil.buildResponse("Success ", HttpStatus.OK, Map.of("data", resp));
    }

    @Override
    public ResponseEntity<?> availableMobile(String mobile) {
        User u = userRepo.findByMobile(mobile);
        Map<String, Object> resp = new HashMap<>();
        if (u != null) {
            resp.put("success", false);
            resp.put("message", "User already present..!! Try resetting you password..");
            return ResponseUtil.buildResponse("Constraint Violation: ", HttpStatus.NOT_ACCEPTABLE, Map.of("error", resp));
        }

        resp.put("success", true);
        return ResponseUtil.buildResponse("Success", HttpStatus.OK, Map.of("data", resp));
    }

    @Override
    public ResponseEntity<?> availablePan(String pan) {
        User u = userRepo.findByPan(pan);
        Map<String, Object> resp = new HashMap<>();
        if (u != null) {
            resp.put("success", false);
            resp.put("message", "User already present..!! Try resetting you password..");
            return ResponseUtil.buildResponse("Constraint Violation: ", HttpStatus.NOT_ACCEPTABLE, Map.of("error", resp));
        }

        resp.put("success", true);
        return ResponseUtil.buildResponse("Success", HttpStatus.OK, Map.of("data", resp));
    }

    @Override
    public ResponseEntity<?> availableAdhaar(String adhaar) {
        User u = userRepo.findByAdhaar(adhaar);
        Map<String, Object> resp = new HashMap<>();
        if (u != null) {
            resp.put("success", false);
            resp.put("message", "User already present..!! Try resetting you password..");
            return ResponseUtil.buildResponse("Constraint Violation: ", HttpStatus.NOT_ACCEPTABLE, Map.of("error", resp));
        }

        resp.put("success", true);
        return ResponseUtil.buildResponse("Success", HttpStatus.OK, Map.of("data", resp));
    }

    @Override
    public ResponseEntity<?> fundTransfer(String userid, FundTransferDto dto) {
        Optional<User> user = userRepo.findById(userid);
        if (user.isPresent()) {
            Map<String, Object> resp = new HashMap<>();
            User currUser = user.get();
            if (dto.getAmount()>currUser.getWallet()){
                resp.put("success", false);
                resp.put("message", "Insufficient Balance");
                return ResponseUtil.buildResponse("Low Balance", HttpStatus.NOT_ACCEPTABLE, Map.of("error", resp));
            }
            User reciever = userRepo.findByMobile(dto.getRecieverMobile());
            Double availableBalance = currUser.getWallet();
            Double newbalance = availableBalance - dto.getAmount();
            currUser.setWallet(newbalance);
            userRepo.save(currUser);
            Double reciverBalance = reciever.getWallet();
            Double newreciverBalance = reciverBalance+dto.getAmount();
            currUser.setWallet(newreciverBalance);
            userRepo.save(currUser);
            resp.put("success", true);
            resp.put("message", "Amount Transferred Successfully");
            return ResponseUtil.buildResponse("Success", HttpStatus.OK, Map.of("data", resp));
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", false);
        resp.put("message", "User not Found..!!");
        return ResponseUtil.buildResponse("Something went wrong", HttpStatus.NOT_ACCEPTABLE, Map.of("error", resp));
    }

    @Override
    public ResponseEntity<?> sendOtpFundTransfer(String userid) {
        return null;
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        System.out.println("token is ====>"+token);
        Claims claims = parseToken(token);
        String userId = claims.get("userId", String.class);
        System.out.println("userId is ====>"+userId);
        String email = claims.get("email", String.class);
        System.out.println("email is ====>"+email);

        User user= userRepo.findByEmail(email);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

    }

    @Override
    public ResponseEntity<?> cardApply(String userid, String cardtype) {
        Map<String, Object> resp = new HashMap<>();
        Optional<User> user = userRepo.findById(userid);
        if (user.isPresent()) {
            User currUser = user.get();
            CardApply checkApply = cardApplyRepo.findByUser(currUser);
            if(checkApply != null){
                resp.put("success", false);
                resp.put("message", "Your request is  "+checkApply.getStatus());
                return ResponseUtil.buildResponse("Request Already Present..!!", HttpStatus.OK, Map.of("error", resp));
            }
            CardApply apply = new CardApply();
            apply.setApplyFor(cardtype);
            apply.setUser(currUser);
            cardApplyRepo.save(apply);
            resp.put("success", true);
            resp.put("message", "Your request is Recorded..!!");
            return ResponseUtil.buildResponse("Success", HttpStatus.OK, Map.of("data", resp));

        }

        resp.put("success", false);
        resp.put("message", "User not Found..!!");
        return ResponseUtil.buildResponse("Something went wrong", HttpStatus.NOT_ACCEPTABLE, Map.of("error", resp));
    }

}



