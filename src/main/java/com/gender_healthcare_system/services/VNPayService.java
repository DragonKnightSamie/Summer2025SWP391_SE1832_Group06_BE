package com.gender_healthcare_system.services;

import com.gender_healthcare_system.configs.VNPayConfig;
import com.gender_healthcare_system.payloads.todo.VNPayRefundPayload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServlet;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService extends HttpServlet{

    //private static String responseString = "";

    public String createPaymentUrl(Long amount, String redirectUrl,
                                   HttpServletRequest request) {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long realAmount = amount *100;
        String bankCode = "VNBANK";
        String locale = "vn";

        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);

        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(realAmount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", locale);
        vnp_Params.put("vnp_ReturnUrl", redirectUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {

            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);

            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        //String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        return VNPayConfig.vnp_PayUrl + "?" + query;
    }

    /*public String createTransactionRefundRequest
            (VNPayRefundPayload payload, HttpServletRequest request,
             HttpServletResponse response) throws IOException {
        request.setAttribute("vnp_TxnRef", payload.getTransactionReference());
        request.setAttribute("vnp_TransactionNo", payload.getTransactionId());
        request.setAttribute("vnp_TransactionDate", payload.getTransactionDate());
        request.setAttribute("vnp_Amount", payload.getAmount());

        doPost(request, response);
        return responseString;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        System.out.println("Param vnp_TxnRef: " + request.getAttribute("vnp_TxnRef"));
        System.out.println("Param vnp_TransactionNo: " + request.getAttribute("vnp_TransactionNo"));
        System.out.println("Param vnp_TransactionDate: " + request.getAttribute("vnp_TransactionDate"));
        System.out.println("Param vnp_Amount: " + request.getAttribute("vnp_Amount"));

        String vnp_RequestId = VNPayConfig.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "refund";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String vnp_TransactionType = "02";
        //String vnp_TxnRef = (String) request.getAttribute("vnp_TxnRef");
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);

        long amount = (long)request.getAttribute("vnp_Amount")*100;
        String vnp_Amount = String.valueOf(amount);

        String vnp_OrderInfo = "Hoan tien GD OrderId:" + vnp_TxnRef;
        String vnp_TransactionNo = (String) request.getAttribute("vnp_TransactionNo"); //Assuming value of the parameter "vnp_TransactionNo" does not exist on your system.
        String vnp_TransactionDate = (String) request.getAttribute("vnp_TransactionDate");
        String vnp_CreateBy = "User";

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        String vnp_IpAddr = VNPayConfig.getIpAddress(request);

        Map<String, String> vnp_Params = new HashMap<>();

        vnp_Params.put("vnp_RequestId", vnp_RequestId);
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_TransactionType", vnp_TransactionType);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_Amount", vnp_Amount);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_TransactionNo", vnp_TransactionNo);
        vnp_Params.put("vnp_TransactionDate", vnp_TransactionDate);
        vnp_Params.put("vnp_CreateBy", vnp_CreateBy);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        String hash_Data= String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode,
                vnp_TransactionType, vnp_TxnRef, vnp_Amount, vnp_TransactionNo, vnp_TransactionDate,
                vnp_CreateBy, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);

        String vnp_SecureHash = VNPayConfig
                .hmacSHA512(VNPayConfig.secretKey, hash_Data);

        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);

        URL url = new URL (VNPayConfig.vnp_ApiUrl);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(vnp_Params.toString());
        wr.flush();
        wr.close();
        String message = con.getResponseMessage();
        int responseCode = con.getResponseCode();
        System.out.println("Sending 'POST' request to URL : " + url);
        System.out.println("Post Data : " + vnp_Params);
        System.out.println("Response Code : " + responseCode);
        System.out.println("Response Message : " + message);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;

        StringBuilder responseBuffer = new StringBuilder();
        while ((output = in.readLine()) != null) {
            responseBuffer.append(output);
        }
        in.close();
        System.out.println(responseBuffer);

        responseString = "Success";
    }*/

    public String checkPaymentError(String transactionNo, String responseCode,
                                    String transactionStatus){

        if(!responseCode.equals("00") || !transactionStatus.equals("00")){
            return "Transaction with ID " + transactionNo + "was not successful";
        }

        return "Transaction with ID " + transactionNo + "was successful";

        /*Map<String, String> fields = new HashMap<>();
        for(Enumeration<String> params = request.getParameterNames();
            params.hasMoreElements();){

            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);

            if(fieldValue != null){
                fields.put(fieldName, fieldValue);
            }
        };

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");

        if(fields.containsKey("vnp_SecureHashType")){
            fields.remove("vnp_SecureHashType");
        }

        if(fields.containsKey("vnp_SecureHash")){
            fields.remove("vnp_SecureHash");
        }

        String signValue = VNPayConfig.hashAllFields(fields);

        if(signValue.equals(vnp_SecureHash)){

            String responseCode = request.getParameter("vnp_ResponseCode");

            if(responseCode.equals("00")){
                return "Transaction was successful";
            }
            else {
                return "Transaction was not successful";
            }
        }
        else {
            return "Failed to validate Signature";
        }*/
    }
}
