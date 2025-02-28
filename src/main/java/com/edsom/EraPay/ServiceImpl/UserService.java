package com.edsom.EraPay.ServiceImpl;

import com.edsom.EraPay.Dtos.FundTransferDto;
import com.edsom.EraPay.Dtos.UserUpdateDto;
import com.edsom.EraPay.Entities.Card;
import com.edsom.EraPay.Entities.CardApply;
import com.edsom.EraPay.Entities.EasebuzzPayin;
import com.edsom.EraPay.Entities.User;
import com.edsom.EraPay.GlobalUtils.ResponseUtil;
import com.edsom.EraPay.Repos.CardApplyRepo;
import com.edsom.EraPay.Repos.CardRepo;
import com.edsom.EraPay.Repos.EasebuzzPayinRepo;
import com.edsom.EraPay.Repos.UserRepo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.edsom.EraPay.ServiceImpl.EmailServiceImpl.parseToken;


@Service
public class UserService implements com.edsom.EraPay.Service.UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    CardApplyRepo cardApplyRepo;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    EasebuzzPayinRepo payinRepo;

    @Autowired
    CardRepo cardRepo;

    @Autowired
    EmailServiceImpl emailService;

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

    @Override
    public ResponseEntity<?> payinReports(String userid, Integer currPage, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(currPage, pageSize);
        Map<String, Object> resp = new HashMap<>();
        User u = userRepo.findByUserId(userid);
        Page<EasebuzzPayin> list = payinRepo.findByUser(u, pageRequest);
        resp.put("success", true);
        resp.put("data", list);
        return ResponseUtil.buildResponse("Success", HttpStatus.OK, resp);
    }

    @Override
    public ResponseEntity<?> updateUser(UserUpdateDto dto) {
        Map<String, Object> resp = new HashMap<>();
        User u = userRepo.findByUserId(dto.getUserid());
        u.setName(dto.getName());
        u.setAdhaar(dto.getAdhaar());
        u.setPan(dto.getPan());
        u.setEmail(dto.getEmail());
        u.setMobile(dto.getMobile());
        u.setDob(dto.getDob());
        u.setWalletAddress(dto.getWalletAddress());
        try {
            User savedUser = userRepo.save(u);
            resp.put("success", true);
            resp.put("data", savedUser);
            return ResponseUtil.buildResponse("User Updated SuccessFully..!!", HttpStatus.OK, resp);
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("error", e.getLocalizedMessage());
            return ResponseUtil.buildResponse("Something went wrong..!!", HttpStatus.NOT_ACCEPTABLE, resp);
        }
    }

    @Override
    public ResponseEntity<?> fetchBalance(String userid) {
        User user = userRepo.findByUserId(userid);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("balance", user.getWallet());
        return ResponseUtil.buildResponse("Balance Fetched SuccessFully..!!", HttpStatus.OK, resp);
    }

    @Override
    public ResponseEntity<?> myInfo(String userid) {
        User user = userRepo.findByUserId(userid);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("user", user);
        return ResponseUtil.buildResponse("User Fetched SuccessFully..!!", HttpStatus.OK, resp);
    }

    @Override
    public ResponseEntity<?> myCard(String userid) {
        User user = userRepo.findByUserId(userid);
        Card card = cardRepo.findByUser(user);
        Map<String, Object> resp = new HashMap<>();
        if (card == null){
            resp.put("success", false);
            resp.put("error", "Please Apply for Card First..!!");
            return ResponseUtil.buildResponse("No Card Found", HttpStatus.OK, resp);
        }
        resp.put("success", true);
        resp.put("card", card);
        return ResponseUtil.buildResponse("Card Found", HttpStatus.OK, resp);
    }

    @Override
    public ResponseEntity<?> forgetPassword(String email) {

        Map<String, Object> resp=null;
        try {
          resp  = new HashMap<>();
            Optional<User> u = Optional.ofNullable(userRepo.findByEmail(email));

            if (u.isPresent()) {
                User user = u.get();
                 emailService.sendWelcomeEmail(user,"forget");
            } else {
                return ResponseEntity.ok(ResponseUtil.buildResponse(
                        "No account found with this email address.",
                        HttpStatus.NOT_FOUND,
                        null
                ));
            }


        }
        catch (Exception e) {
            resp.put("success", false);
            resp.put("error", e.getLocalizedMessage());
            return ResponseUtil.buildResponse("Something went wrong..!!", HttpStatus.NOT_ACCEPTABLE, resp);
        }
        return ResponseEntity.ok(ResponseUtil.buildResponse(
                "Password reset link has been sent to your email. Please check your inbox.",
                HttpStatus.OK,
                null
        ));
    }

    @Override
    public ResponseEntity<?> getUsersCount() {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = userRepo.count();
            response.put("count", count);
            return ResponseUtil.buildResponse("User count retrieved successfully", HttpStatus.OK, response);
        } catch (Exception ex) {

            response.put("error", "An unexpected error occurred. Please try again later.");
            return ResponseUtil.buildResponse("Failed to fetch user count", HttpStatus.INTERNAL_SERVER_ERROR, response);
        }
    }

}



