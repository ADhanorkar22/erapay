package com.edsom.EraPay.ServiceImpl;

import com.edsom.EraPay.EasbuzzUtil.EasbuzzUtil;
import com.edsom.EraPay.EasbuzzUtil.PaymentRequest;
import com.edsom.EraPay.Entities.EasebuzzPayin;
import com.edsom.EraPay.Entities.User;
import com.edsom.EraPay.GlobalUtils.ResponseUtil;
import com.edsom.EraPay.Repos.EasebuzzPayinRepo;
import com.edsom.EraPay.Repos.UserRepo;
import com.edsom.EraPay.Service.EaseBuzzPayInService;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class EasBuzzPayinImpl implements EaseBuzzPayInService {

    @Value("${easebuzz.api.key}")
    private String apiKey;

    @Value("${easebuzz.api.salt}")
    private String apiSalt;

    @Value("${easebuzz.api.url}")
    private String apiUrl;

    @Autowired
    UserRepo userRepo;

    @Autowired
    EasebuzzPayinRepo easerepo;

    @Override
    public ResponseEntity<?> initiatePayment(PaymentRequest paymentRequest) {
        Optional<User> checkUser = userRepo.findById( paymentRequest.getUserid());
        if (checkUser.isPresent()) {
            User user = checkUser.get();
            try {
                String hash = EasbuzzUtil.generateHash(apiKey, paymentRequest.getTxnid(),
                        paymentRequest.getAmount(), paymentRequest.getProductinfo(), user.getName(),
                        user.getEmail(), apiSalt);

                String params = String.format(
                        "key=%s&txnid=%s&amount=%s&productinfo=%s&firstname=%s&phone=%s&email=%s&surl=%s&furl=%s&hash=%s&show_payment_mode=%s",
                        apiKey, paymentRequest.getTxnid(), paymentRequest.getAmount(),
                        paymentRequest.getProductinfo(), user.getName(), user.getMobile(),
                        user.getEmail(), paymentRequest.getSurl(), paymentRequest.getFurl(), hash, PaymentRequest.getPaymentMode());

                try (CloseableHttpClient client = HttpClients.createDefault()) {
                    HttpPost httpPost = new HttpPost(apiUrl);
                    httpPost.setEntity(new StringEntity(params));
                    httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    httpPost.setHeader("Accept", "application/json");

                    String responseString = EntityUtils.toString(client.execute(httpPost).getEntity());
                    boolean resp = GenrateOrder(user, paymentRequest);
                    Map<String,Object> respSend = new HashMap<>();
                    if (!resp) {
                        respSend.put("success", false);
                        respSend.put("message","Can't create order..!! Try again later..");
                        return ResponseUtil.buildResponse("Something Went Worng",HttpStatus.BAD_REQUEST,Map.of("error",respSend));
                    }
                    respSend.put("success", true);
                    respSend.put("data",responseString);
                    return ResponseUtil.buildResponse("Success",HttpStatus.OK,respSend);
                }
            } catch (Exception e) {
                Map<String,Object> respSend = new HashMap<>();
                respSend.put("success", false);
                respSend.put("message","Can't create order..!! Try again later..");
                respSend.put("error",e.getLocalizedMessage());
                return ResponseUtil.buildResponse("Something Went Worng",HttpStatus.INTERNAL_SERVER_ERROR,respSend);
            }
        }
        Map<String,Object> respSend = new HashMap<>();
        respSend.put("success", false);
        respSend.put("message","User Not Found..!!");
        return ResponseUtil.buildResponse("Something Went Worng",HttpStatus.INTERNAL_SERVER_ERROR,respSend);
    }

    public boolean GenrateOrder(User user, PaymentRequest paymentRequest) {
        EasebuzzPayin easebuzz = new EasebuzzPayin();
        easebuzz.setUser(user);
        easebuzz.setPhone(user.getMobile());
        easebuzz.setTxnid(paymentRequest.getTxnid());
        easebuzz.setAddedon(LocalDateTime.now());
        easebuzz.setStatus("ORDER_CREATED");
        easebuzz.setSettlement("unsettled");
        double payinAmountDouble = Double.valueOf(paymentRequest.getAmount());
//        double famountnew = 0.0;
//        double AmountforMasterTxn=0.0;


        this.easerepo.save(easebuzz);
        return true;
    }

}
