package com.gender_healthcare_system.services;

import com.mservice.allinone.models.PaymentResponse;
import com.mservice.shared.sharedmodels.Environment;
import org.springframework.stereotype.Service;

@Service
public class MomoPaymentService {
    String requestId = String.valueOf(System.currentTimeMillis());
    String orderId = String.valueOf(System.currentTimeMillis());
    Long transId = 2L;
    long amount = 50000;

    String partnerClientId = "partnerClientId";
    String orderInfo = "Pay With MoMo";
    String returnURL = "https://google.com.vn"; //đổi cái returnUrl thành url của api khi mà ng dùng thanh toán momo
    String notifyURL = "https://google.com.vn";
    String callbackToken = "callbackToken";
    String token = "";

    Environment environment = Environment.selectEnv("dev");


//      Remember to change the IDs at enviroment.properties file

    /***
     * create payment with capture momo wallet
     */
    PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(amount), orderInfo, returnURL, notifyURL, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);

/***

}
