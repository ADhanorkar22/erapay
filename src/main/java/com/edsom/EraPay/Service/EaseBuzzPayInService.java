package com.edsom.EraPay.Service;

import com.edsom.EraPay.EasbuzzUtil.PaymentRequest;
import org.springframework.http.ResponseEntity;

public interface EaseBuzzPayInService {

    ResponseEntity<?> initiatePayment(PaymentRequest paymentRequest);
}
