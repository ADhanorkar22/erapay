package com.edsom.EraPay.ServiceImpl;

import com.edsom.EraPay.Dtos.SignInDto;
import com.edsom.EraPay.Entities.User;
import com.edsom.EraPay.GlobalUtils.ResponseUtil;
import com.edsom.EraPay.Repos.RoleRepo;
import com.edsom.EraPay.Repos.UserRepo;
import com.edsom.EraPay.Security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class AuthService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    JwtUtils jwtUtils;

    public ResponseEntity<?> empSignIn(SignInDto signInDto) {

        System.out.println(signInDto.getEmpId() + " == " + signInDto.getPassword());
        User employee = userRepo.findByEmail(signInDto.getEmpId());

        if (employee == null) {
            return ResponseUtil.buildResponse("Bad Credentials: Employee Not Found", HttpStatus.NOT_FOUND, null);
        }

        if (!signInDto.getPassword().equals(employee.getPassword())) {
            return ResponseUtil.buildResponse("Bad Credentials: Password Not Match", HttpStatus.NOT_ACCEPTABLE, null);
        }

        String token = jwtUtils.generateToken(employee.getUserId(), employee.getRole().getUserType().toString());


        Map<String, Object> response = new HashMap<>();


        response.put("token", token); // Add the token to the response
        response.put("emp_role", employee.getRole().getUserType().toString());

        response.put("emp_name", employee.getName());
        response.put("emp_Id", employee.getUserId());
        response.put("ststus", employee.getStatus());


        return ResponseUtil.buildResponse("SignIn Successful", HttpStatus.OK, response);
    }




}
