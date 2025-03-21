package com.edsom.EraPay.ServiceImpl;


import com.edsom.EraPay.Entities.User;
import com.edsom.EraPay.Repos.RoleRepo;
import com.edsom.EraPay.Repos.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Service
public class EmailServiceImpl{

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    UserRepo userRepo;


    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${support.email}") // Load recipient email from application.properties
    private String supportEmail;

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final static Key SECRET_KEY = Keys.hmacShaKeyFor("hjdsbhsbhjskjnbhjsjhsbhjsbhjsbhbshsbhjsbhjxbshbhbshbshbhsbshgshavhgsvghsvhgsvbhdshbdhsbdhbhjsbhjsa".getBytes(StandardCharsets.UTF_8));


    public void sendWelcomeEmail(User user, String purpose ) {
        String text=null;
        String subject=null;
        if("register".equals(purpose)) {
            String token = generateResetToken(user);

            // Password reset link (Frontend URL)
            String resetLink = "http://localhost:5173/reset-password?token=" + token;

            subject = "\uD83D\uDE80 Welcome to Erapay! Secure Your Account Now";
          text = "Hi " + user.getName() + ",\n\n" +
                    "Welcome to Erapay! We're excited to have you on board.\n\n" +
                    "Please set your password by clicking the link below:\n" +
                    resetLink + "\n\n" +
                    "If you have any questions, feel free to reach out to our support team.\n\n" +
                    "Best regards,\n" +
                    "The Erapay Team";

        } else if("forget".equals(purpose)){
            String token = generateResetToken(user);
            String resetLink = "https://https://test.erapay.biz/reset?token=" + token;
            subject = "\uD83D\uDE80  Reset Your Password - Erapay";
            text = "Hi " + user.getName() + ",\n\n" +
                    "We received a request to reset your password for your Erapay account.\n" +
                    "Please click the link below to set a new password:\n\n" +
                    resetLink + "\n\n" +
                    "If you didn't request a password reset, you can safely ignore this email.\n" +
                    "For any assistance, feel free to contact our support team.\n\n" +
                    "Best regards,\nThe Erapay Team";

        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());  // Now dynamically set from properties
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(fromEmail);

        try {
            mailSender.send(message);
            log.info("Enquiry email sent successfully from {}", fromEmail);
        } catch (Exception e) {

            System.out.println("----"+e.getMessage());
            System.out.println(e.getCause().getMessage());

            log.error("Failed to send enquiry email from {}: {}", fromEmail, e.getMessage(), e);
        }
    }

    public void sendTxnOtpMail() {

        User user=userRepo.findByEmail("rahul.sharma@example.com");
        String token = generateResetToken(user);

        // Password reset link (Frontend URL)
        String resetLink = "http://localhost:5173/reset-password?token=" + token;



        String subject = "Enquiry from Ipaisa";
        String text = "Hi " + user.getName() + ",\n\n" +
                "Welcome to Erapay! We're excited to have you on board.\n\n" +
                "Please set your password by clicking the link below:\n" +
                resetLink + "\n\n" +
                "If you have any questions, feel free to reach out to our support team.\n\n" +
                "Best regards,\n" +
                "The Erapay Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("ashishdhanorkar28@gmail.com");  // Now dynamically set from properties
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(fromEmail);

        try {
            mailSender.send(message);
            log.info("Enquiry email sent successfully to {}", supportEmail);
        } catch (Exception e) {

            System.out.println("----"+e.getMessage());
            System.out.println(e.getCause().getMessage());

            log.error("Failed to send enquiry email to {}: {}", supportEmail, e.getMessage(), e);
        }
    }


    public  static String generateResetToken(User user) {


        return Jwts.builder()
                .claim("userId", user.getUserId())
                .claim("email", user.getEmail())
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
