package com.gender_healthcare_system.services;

import com.gender_healthcare_system.payloads.todo.MomoPaymentPayload;

import com.gender_healthcare_system.payloads.todo.MomoPaymentRefundPayload;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Enumeration;

@Service
public class MomoPaymentService {

    private static final String PARTNER_CODE = "MOMO";
    private static final String ACCESS_KEY = "F8BBA842ECF85";
    private static final String SECRET_KEY = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
//    private static final String REDIRECT_URL =
//            "http://localhost:8080/customer/payment-transaction/check-error";
    private static final String IPN_URL = "https://callback.url/notify";

    public void getMomoPaymentTransactionInfo(HttpServletRequest request){

       Enumeration<String> params = request.getParameterNames();

       while (params.hasMoreElements()){
           String paramName = params.nextElement();
           System.out.println("Param name: "+ paramName +
                   ", value: "+ request.getParameter(paramName));
       }

    }

    public String createMomoPaymentRequest(MomoPaymentPayload payload) throws Exception{

        try {
            // Generate requestId and orderId
            String requestId = PARTNER_CODE + new Date().getTime();
            String orderId = requestId;

            String REQUEST_TYPE = "payWithMethod";

            String orderInfo = "Customer:" + payload.getCustomerFullName()
                    + ". Description: " + payload.getDescription();
            String redirectUrl = payload.getRedirectUrl();
            String extraData = "";
            long amount = payload.getAmount();

            // Generate raw signature
            String rawSignature = String.format(
                    "accessKey=%s&amount=%s&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s" +
                            "&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                    ACCESS_KEY, amount, extraData, IPN_URL, orderId,
                    orderInfo, PARTNER_CODE, redirectUrl,
                    requestId, REQUEST_TYPE);

            // Sign with HMAC SHA256
            String signature = signHmacSHA256(rawSignature);
            System.out.println("Generated Signature: " + signature);

            JSONObject requestBody = new JSONObject();
            requestBody.put("partnerCode", PARTNER_CODE);
            requestBody.put("accessKey", ACCESS_KEY);
            requestBody.put("requestId", requestId);
            requestBody.put("amount", amount);
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", orderInfo);
            requestBody.put("redirectUrl", redirectUrl);
            requestBody.put("ipnUrl", IPN_URL);
            requestBody.put("extraData", extraData);
            requestBody.put("requestType", REQUEST_TYPE);
            requestBody.put("signature", signature);
            requestBody.put("lang", "en");

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(
                    "https://test-payment.momo.vn/v2/gateway/api/create");
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                //CloseableHttpResponse response = httpClient.execute(httpPost);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(),
                                StandardCharsets.UTF_8));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {

                    result.append(line);
                }

                System.out.println("Response from MoMo: " + result.toString());
                return result.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to create payment request: " + e.getMessage() + "\"}";
        }
    }

   /* public String createMomoRefundPaymentRequest
            (MomoPaymentRefundPayload payload) throws Exception{

        try {
            // Generate requestId and orderId
            String orderId = String.valueOf(System.currentTimeMillis());
            String requestId = String.valueOf(System.currentTimeMillis()+1000);

            //String REQUEST_TYPE = "payWithMethod";

            String description = "Customer:" + payload.getCustomerFullName()
                    + ". Description: " + payload.getDescription();
            String lang = "en";

            //String transId = payload.getTransactionId();
            long transId = payload.getTransactionId();

            long amount = payload.getAmount();

            // Generate raw signature
            String rawSignature = String.format(
                    "accessKey=%s&amount=%s&description=%s&orderId=%s&partnerCode=%s" +
                            "&requestId=%s&transId=%s",
                    ACCESS_KEY, amount, description, orderId,
                    PARTNER_CODE, requestId, transId);

            // Sign with HMAC SHA256
            String signature = signHmacSHA256(rawSignature);
            System.out.println("Generated Signature: " + signature);

            JSONObject requestBody = new JSONObject();
            requestBody.put("partnerCode", PARTNER_CODE);
            //requestBody.put("accessKey", ACCESS_KEY);
            requestBody.put("orderId", orderId);
            requestBody.put("requestId", requestId);
            requestBody.put("amount", amount);
            requestBody.put("transId", transId);
            requestBody.put("lang", "vi");
            requestBody.put("description", description);
            //requestBody.put("redirectUrl", redirectUrl);
            //requestBody.put("ipnUrl", IPN_URL);
            //requestBody.put("extraData", extraData);
            //requestBody.put("requestType", REQUEST_TYPE);
            requestBody.put("signature", signature);


            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(
                    "https://test-payment.momo.vn/v2/gateway/api/refund");
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                //CloseableHttpResponse response = httpClient.execute(httpPost);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(),
                                StandardCharsets.UTF_8));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {

                    result.append(line);
                }

                System.out.println("Response from MoMo: " + result.toString());
                return result.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to create payment request: " + e.getMessage() + "\"}";
        }
    }*/

    // HMAC SHA256 signing method
    private static String signHmacSHA256(String data) throws Exception {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKey = new SecretKeySpec
                (MomoPaymentService.SECRET_KEY.getBytes(StandardCharsets.UTF_8),
                        "HmacSHA256");

        hmacSHA256.init(secretKey);
        byte[] hash = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}