package com.edsom.EraPay.Service;

import com.edsom.EraPay.EasbuzzUtil.PaymentRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface EaseBuzzPayInService {

    ResponseEntity<?> initiatePayment(PaymentRequest paymentRequest);
    ResponseEntity<?> getResponse(Map<String, String> requestBody, String mobileno);
}
