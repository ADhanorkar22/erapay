package com.edsom.EraPay.handlers;

import com.edsom.EraPay.Entities.User;
import com.edsom.EraPay.Repos.UserRepo;
import com.edsom.EraPay.Security.JwtUtils;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component

public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    UserRepo userService;


    @Autowired
    JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken tokenn = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) tokenn.getPrincipal();

        String email = oAuth2User.getAttribute("email");

        Optional<User> u = Optional.ofNullable(userService.findByEmail(email));
        User user = u.orElse(null);

        String token = null;
        if (user==null){
            System.out.println("hbdshbdshbdsuhdnshndsbhjdbshjdsb");

            String frontEndUrl = "http://localhost:3000/notregistered"
                    ;
            response.sendRedirect(frontEndUrl);
            return;
        } else {

            token = jwtUtils.generateToken(user.getUserId(), user.getRole().getUserType().toString());
            System.out.println("========================>"+token);

        }
////        Cookie cookie = new Cookie("refreshToken", refreshToken);
//        cookie.setHttpOnly(true);
//        cookie.setSecure("production".equals(deployEnv));
//        response.addCookie(cookie);

        String frontEndUrl = "http://localhost:3000/dashboard?token=" + token +
                "&emp_role=" + user.getRole().getUserType().toString() +
                "&status=" + user.getStatus() +
                "&emp_name=" + user.getName() +
                "&emp_Id=" + user.getUserId();


//        getRedirectStrategy().sendRedirect(request, response, frontEndUrl);

        response.sendRedirect(frontEndUrl);
    }

}
