package com.edsom.EraPay.ServiceImpl;

import com.edsom.EraPay.Entities.CardApply;
import com.edsom.EraPay.Entities.User;
import com.edsom.EraPay.Enums.UserStatus;
import com.edsom.EraPay.Enums.UserType;
import com.edsom.EraPay.GlobalUtils.ResponseUtil;
import com.edsom.EraPay.Repos.CardApplyRepo;
import com.edsom.EraPay.Repos.UserRepo;
import com.edsom.EraPay.Service.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class Adminimpl implements Admin {
    @Autowired
    UserRepo userRepo;

    @Autowired
    CardApplyRepo applyRepo;

    @Override
    public ResponseEntity<?> userList(String userid, Integer currPage, Integer pageSize) {
        Optional<User> user = userRepo.findById(userid);
        if (user.isPresent()) {
            User currUser = user.get();
            System.out.println(currUser);
            Map<String, Object> resp = new HashMap<>();
            if (!UserType.ADMIN.equals(currUser.getRole().getUserType())) {
                resp.put("success", false);
                resp.put("message", "User not Authorized..!!");
                return ResponseUtil.buildResponse("Constraint Violation: ", HttpStatus.NOT_ACCEPTABLE, Map.of("error", resp));
            }
            PageRequest pageRequest = PageRequest.of(currPage, pageSize); // Create pagination request
            Page<User> userList = userRepo.findAll(pageRequest);
            resp.put("success", true);
            resp.put("data", userList);
            return ResponseUtil.buildResponse("success", HttpStatus.OK, resp);
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", false);
        resp.put("message", "User not Found..!!");
        return ResponseUtil.buildResponse("Something went wrong", HttpStatus.NOT_ACCEPTABLE, Map.of("error", resp));
    }

    @Override
    public ResponseEntity<?> allUsersBalance(String userid) {
        Optional<User> user = userRepo.findById(userid);
        if (user.isPresent()) {
            User currUser = user.get();
            System.out.println(currUser);
            Map<String, Object> resp = new HashMap<>();
            if (!UserType.ADMIN.equals(currUser.getRole().getUserType())) {
                resp.put("success", false);
                resp.put("message", "User not Authorized..!!");
                return ResponseUtil.buildResponse("Constraint Violation: ", HttpStatus.NOT_ACCEPTABLE, Map.of("error", resp));
            }

            Double totalAmt = userRepo.getTotalWalletBalance();
            resp.put("success", true);
            resp.put("totalAmount", totalAmt);
            return ResponseUtil.buildResponse("Success", HttpStatus.OK, Map.of("data", resp));
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", false);
        resp.put("message", "User not Found..!!");
        return ResponseUtil.buildResponse("Something went wrong", HttpStatus.NOT_ACCEPTABLE, Map.of("error", resp));
    }

    @Override
    public ResponseEntity<?> allCardApplications(Integer currPage, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(currPage,pageSize);
        Page<CardApply> list = applyRepo.findAll(pageRequest);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("data", list);
        return ResponseUtil.buildResponse("success", HttpStatus.OK, resp);
    }
}
