package com.edsom.EraPay.ServiceImpl;

import com.edsom.EraPay.Config.OkHttpClientSingleton;
import com.edsom.EraPay.EasbuzzUtil.EasbuzzUtil;
import com.edsom.EraPay.EasbuzzUtil.PaymentRequest;
import com.edsom.EraPay.Entities.EasebuzzPayin;
import com.edsom.EraPay.Entities.User;
import com.edsom.EraPay.GlobalUtils.ResponseUtil;
import com.edsom.EraPay.Repos.EasebuzzPayinRepo;
import com.edsom.EraPay.Repos.UserRepo;
import com.edsom.EraPay.Service.EaseBuzzPayInService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    private OkHttpClient client;

    public EasBuzzPayinImpl() {
        this.client = OkHttpClientSingleton.getInstance();
    }

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

    private String generateSHA512Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @Override
    public ResponseEntity<?> getResponse(Map<String, String> requestBody, String mobileno) {
        try {
            String txnid = null;
            txnid = requestBody.get("txnid");
            String inputString = apiKey + "|" + txnid + "|" + apiSalt;
            String hash = generateSHA512Hash(inputString);

            FormBody formBody = new FormBody.Builder().add("txnid", txnid).add("key", apiKey).add("hash", hash).build();

            Request request = new Request.Builder().url("https://dashboard.easebuzz.in/transaction/v2/retrieve")
                    .post(formBody).addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json").build();

            try (Response response = client.newCall(request).execute()) {

                Map<String, String> returnResp = new HashMap<>();
                if (!response.isSuccessful()) {

                    throw new RuntimeException("Unexpected code " + response);

                }
                String json = response.body().string();

                System.out.println("json=====>" + json);

                ObjectMapper objectMapper = new ObjectMapper();


                EasebuzzPayin easebuzztixn = easerepo.findByTxnid(txnid);
                System.out.println("easebuzztixn=======>" + easebuzztixn.toString());

                Map<String, Object> jsonMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
                });

                // Extract 'msg' object from JSON
                Map<String, Object> msgMap = (Map<String, Object>) jsonMap.get("msg");

                System.out.println("msgMap=======>" + msgMap);

                easebuzztixn.setName((String) msgMap.get("firstname"));
                easebuzztixn.setEmail((String) msgMap.get("email"));
                easebuzztixn.setPhone((String) msgMap.get("phone"));
                easebuzztixn.setKeyId((String) msgMap.get("key")); // Corrected to setKeyId
                easebuzztixn.setMode((String) msgMap.get("mode"));
                easebuzztixn.setUnmappedstatus((String) msgMap.get("unmappedstatus"));
                easebuzztixn.setCardCategory((String) msgMap.get("cardCategory"));


                easebuzztixn.setAddedon(LocalDateTime.now());


                easebuzztixn.setPgType((String) msgMap.get("PG_TYPE"));
                easebuzztixn.setBankRefNum((String) msgMap.get("bank_ref_num"));
                easebuzztixn.setBankcode((String) msgMap.get("bankcode"));
                easebuzztixn.setError((String) msgMap.get("error"));
                easebuzztixn.setErrorMessage((String) msgMap.get("error_Message"));
                easebuzztixn.setNameOnCard((String) msgMap.get("name_on_card"));
                easebuzztixn.setUpiVa((String) msgMap.get("upi_va"));
                easebuzztixn.setCardnum((String) msgMap.get("cardnum"));
                easebuzztixn.setIssuingBank((String) msgMap.get("issuing_bank"));
                easebuzztixn.setEasepayid((String) msgMap.get("easepayid"));




                Double payinAmountDouble = Double.valueOf((String) msgMap.get("amount"));
                System.out.println("Payin Amount -->>" + payinAmountDouble);
                String Status = (String) msgMap.get("status");
                easebuzztixn.setAmount(String.valueOf(payinAmountDouble));
                easebuzztixn.setNetAmountDebit((String) msgMap.get("net_amount_debit"));
                easebuzztixn.setCashBackPercentage((String) msgMap.get("cash_back_percentage"));
                easebuzztixn.setDeductionPercentage((String) msgMap.get("deduction_percentage"));
                easebuzztixn.setProductinfo((String) msgMap.get("productinfo"));
                easebuzztixn.setCardType((String) msgMap.get("card_type"));
                easebuzztixn.setStatus((String) msgMap.get("status"));
                easebuzztixn.setBankName((String) msgMap.get("bank_name"));
                easebuzztixn.setAuthCode((String) msgMap.get("auth_code"));
                easebuzztixn.setSettlement("Settled");


                easerepo.save(easebuzztixn);

                returnResp.put("status", (String) msgMap.get("status"));
                returnResp.put("upiVia", (String) msgMap.get("upi_va"));
                returnResp.put("message", (String) msgMap.get("error_Message"));
                returnResp.put("amount", (String) msgMap.get("net_amount_debit"));
                returnResp.put("type", (String) msgMap.get("card_type"));
                returnResp.put("txnid",txnid);
//           returnResp.put("netAmountDebit", (String) msgMap.get("net_amount_debit"));
                returnResp.put("timestamp",LocalDateTime.now().toString());
                returnResp.put("utr",(String) msgMap.get("bank_ref_num"));


                System.out.println("Retrun response ---->> " + returnResp);

                return ResponseEntity.ok(returnResp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("status", 0, "message", "Error initiating payment"));
        }
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
