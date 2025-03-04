package com.edsom.EraPay.ServiceImpl;

import com.edsom.EraPay.Dtos.UserRegDto;
import com.edsom.EraPay.Entities.*;
import com.edsom.EraPay.Enums.CardNetwork;
import com.edsom.EraPay.Enums.CardStatus;
import com.edsom.EraPay.Enums.UserStatus;
import com.edsom.EraPay.Enums.UserType;
import com.edsom.EraPay.GlobalUtils.ResponseUtil;
import com.edsom.EraPay.Repos.*;
import com.edsom.EraPay.Service.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.*;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import org.springframework.data.jpa.domain.Specification;

@Service
public class Adminimpl implements Admin {
    @Autowired
    UserRepo userRepo;

    @Autowired
    CardApplyRepo applyRepo;

    private final SecureRandom random = new SecureRandom();
    @Autowired
    EmailServiceImpl emailService;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    EasebuzzPayinRepo payinRepo;
    @Autowired
    CardRepo cardRepo;
    @Autowired
    CardTypeRepo typeRepo;

    private String generateUserId(String adhaar) {
        if (adhaar == null || adhaar.length() != 12 || !adhaar.matches("\\d{12}")) {
            throw new IllegalArgumentException("Invalid Aadhaar number");
        }

        Random random = new Random();
        int startIndex = random.nextInt(5); // Random index between 0 and 2 (inclusive)
        String randomDigits = adhaar.substring(startIndex, startIndex + 6);
        return "ERA" + randomDigits; // Prefix with "UID" (you can change it as needed)
    }

    @Override
    public ResponseEntity<?> register(UserRegDto dto) {
        User checkUser = userRepo.findByEmailOrMobileOrAdhaarOrPan(dto.getEmail(), dto.getMobile(), dto.getAdhaar(), dto.getPan());
        Map<String, Object> resp = new HashMap<>();
        if (checkUser != null) {
            resp.put("success", false);
            resp.put("message", "User already present..!! Try resetting you password..");
            return ResponseUtil.buildResponse("Constraint Violation: ", HttpStatus.NOT_ACCEPTABLE, Map.of("error", resp));
        }
        String userid = generateUserId(dto.getAdhaar());
        Optional<Role> roleOpt = roleRepo.findById(2);
        Role role = roleOpt.get();
        User newUser = new User(userid, dto.getName(), dto.getEmail(), dto.getPan(), dto.getMobile(), dto.getAdhaar(), UserStatus.ACTIVE, LocalDateTime.now(), dto.getDob(), dto.getWalletAddress(), 0.0, role);
        User savedUser = userRepo.save(newUser);
        CardType cardType = new CardType();
        cardType.setPhysical(false);
        cardType.setVirtual(false);
        cardType.setUser(savedUser);
        typeRepo.save(cardType);
        emailService.sendWelcomeEmail(savedUser,"register");
        resp.put("success", true);
        resp.put("data", savedUser);
        return ResponseUtil.buildResponse("Registration Completed... Check Email Inbox or try login using Google...", HttpStatus.CREATED, resp);
    }

    @Override
    public ResponseEntity<?> userList(Integer currPage, Integer pageSize,String searchBy) {

        try {
            Specification<User> spec = searchByValue(searchBy);
            Map<String, Object> resp = new HashMap<>();

            PageRequest pageRequest = PageRequest.of(currPage, pageSize); // Create pagination request
            Page<User> userList = userRepo.findAll(spec,pageRequest);
            resp.put("success", true);
            resp.put("data", userList);
            return ResponseUtil.buildResponse("success", HttpStatus.OK, resp);
        }catch (Exception e) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", false);
            resp.put("message", "User not Found..!!");

            return ResponseUtil.buildResponse("Something went wrong", HttpStatus.NOT_ACCEPTABLE, Map.of("error", resp));
        }


    }

    public  Specification<User> searchByValue(String searchValue) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (searchValue == null || searchValue.isEmpty()) {
                return criteriaBuilder.conjunction(); // No filter applied
            }

            String likePattern = "%" + searchValue + "%";

            try {
                // Try to convert searchValue to Double for wallet field
                Double walletValue = Double.parseDouble(searchValue);
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("userId"), likePattern),
                        criteriaBuilder.like(root.get("name"), likePattern),
                        criteriaBuilder.like(root.get("email"), likePattern),
                        criteriaBuilder.like(root.get("mobile"), likePattern),
                        criteriaBuilder.like(root.get("adhaar"), likePattern),
                        criteriaBuilder.like(root.get("walletAddress"), likePattern),
                        criteriaBuilder.equal(root.get("wallet"), walletValue) // Exact match for Double
                );
            } catch (NumberFormatException e) {
                // If not a number, only perform LIKE searches
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("userId"), likePattern),
                        criteriaBuilder.like(root.get("name"), likePattern),
                        criteriaBuilder.like(root.get("email"), likePattern),
                        criteriaBuilder.like(root.get("mobile"), likePattern),
                        criteriaBuilder.like(root.get("adhaar"), likePattern),
                        criteriaBuilder.like(root.get("walletAddress"), likePattern)
                );
            }
        };
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

    @Override
    public ResponseEntity<?> allPayinReport(Integer currPage, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(currPage, pageSize, Sort.by(Sort.Direction.DESC, "addedon"));
        Page<EasebuzzPayin> list = payinRepo.findAll(pageRequest);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("data", list);
        return ResponseUtil.buildResponse("Data Fetched..!!", HttpStatus.OK, resp);
    }

    @Override
    public ResponseEntity<?> allCards(Integer currPage, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(currPage,pageSize,Sort.by(Sort.Direction.DESC,"id"));
        Page<Card> list = cardRepo.findAll(pageRequest);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("data", list);
        return ResponseUtil.buildResponse("Data Fetched..!!", HttpStatus.OK, resp);
    }

    @Override
    public ResponseEntity<?> updateUserWallet(String userid,double amount) {
        User user = userRepo.findByUserId(userid);
        double currentBalance = user.getWallet();
        double newbalance = currentBalance+amount;
        user.setWallet(newbalance);
        userRepo.save(user);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        return ResponseUtil.buildResponse("Wallet Updated", HttpStatus.OK, resp);
    }

    // Generate a random 16-digit card number (Luhn Algorithm is not applied here)
    private String generateCardNumber(CardNetwork network) {
        StringBuilder cardNumber = new StringBuilder(); // Start with '4' for Visa
        switch (network) {
            case VISA:
                cardNumber.append("4"); // Visa starts with 4
                break;
            case MASTERCARD:
                cardNumber.append("5"); // MasterCard starts with 5
                break;
            case AMEX:
                cardNumber.append("3"); // Amex starts with 3
                break;
            case RUPAY:
                cardNumber.append("6"); // Discover starts with 6
                break;
        }

        // Generate the remaining 15 digits (for a total of 16)
        while (cardNumber.length() < 16) {
            cardNumber.append(random.nextInt(10));
        }

        return cardNumber.toString();
    }

    // Generate a random 3-digit CVV
    private String generateCVV() {
        return String.format("%03d", random.nextInt(1000));
    }

    // Generate a future expiration date in MM/YY format
    private LocalDate generateExpiryDate() {
        return LocalDate.now().plusYears(3); // Valid for 3 years
    }

    // Generate a future start date in MM/YY format
    private LocalDate generateStartDate() {
        return LocalDate.now();
    }

    // Generate a random card network
    private CardNetwork getRandomCardNetwork() {
        CardNetwork[] networks = CardNetwork.values();
        return networks[random.nextInt(networks.length)];
    }

    // Main method to generate full card details
    public void generateCardInfo(User user) {
        CardNetwork cardNetwork = getRandomCardNetwork();
        String cardNumber = generateCardNumber(cardNetwork);
        String cvv = generateCVV();
        LocalDate expiryDate = generateExpiryDate();
        LocalDate startDate = generateStartDate();
        Card card = new Card();
        card.setName(user.getName());
        card.setCardNumber(cardNumber);
        card.setCvv(cvv);
        card.setCardNetwork(cardNetwork);
        card.setEnd(expiryDate);
        card.setStart(startDate);
        card.setUser(user);
        cardRepo.save(card);
    }

    @Override
    public ResponseEntity<?> changeApplicationStatus(CardStatus status, String userId) {
        Optional<User> user = userRepo.findById(userId);
        Map<String, Object> resp = new HashMap<>();
        if (user.isPresent()) {
            User currUser = user.get();
            CardApply cardApply = applyRepo.findByUser(currUser);
            if (cardApply.getApplyFor().equals("PHYSICAL") && currUser.getWallet()<101){
                resp.put("success", false);
                return ResponseUtil.buildResponse("User Wallet is Low..!!", HttpStatus.NOT_ACCEPTABLE, resp);
            }
            if (cardApply.getApplyFor().equals("VIRTUAL") && currUser.getWallet()<11){
                resp.put("success", false);
                return ResponseUtil.buildResponse("User Wallet is Low..!!", HttpStatus.NOT_ACCEPTABLE, resp);
            }
            if (cardApply.getStatus().equals(CardStatus.COMPLETED)) {
                resp.put("success", false);
                resp.put("message", "Your Request is already Completed");
                return ResponseUtil.buildResponse("Can't Process this Request", HttpStatus.NOT_ACCEPTABLE, resp);
            }
            cardApply.setStatus(status);
            applyRepo.save(cardApply);
            if (status.equals(CardStatus.COMPLETED)) {
                Card checkCard = cardRepo.findByUser(currUser);
                if (checkCard == null) {
                    generateCardInfo(currUser);
                    if (cardApply.getApplyFor().equals("PHYSICAL")) {
                        CardType type = typeRepo.findByUser(currUser);
                        type.setVirtual(true);
                        type.setPhysical(true);
                        typeRepo.save(type);
                        currUser.setWallet(currUser.getWallet()-101);
                        userRepo.save(currUser);
                    }
                    if (cardApply.getApplyFor().equals("VIRTUAL")) {
                        CardType type = typeRepo.findByUser(currUser);
                        type.setVirtual(true);
                        typeRepo.save(type);
                        currUser.setWallet(currUser.getWallet()-11);
                        userRepo.save(currUser);
                    }
                    applyRepo.delete(cardApply);
                } else {
                    CardType type = typeRepo.findByUser(currUser);

                }
            }
            resp.put("success", true);
            resp.put("message", "Application Status Changed Successfully..!!");
            return ResponseUtil.buildResponse("Success", HttpStatus.OK, resp);
        }
        resp.put("success", false);
        resp.put("message", "User not Found..!!");
        return ResponseUtil.buildResponse("Something went wrong", HttpStatus.NOT_ACCEPTABLE, Map.of("error", resp));
    }
}
