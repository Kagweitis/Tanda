package com.tanda.payments.Services;

import com.tanda.payments.DTO.GwRequest;
import com.tanda.payments.Models.PaymentRequest;
import com.tanda.payments.Models.Status;
import com.tanda.payments.Repository.PaymentRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentsService {


    private final PaymentRequestRepository paymentRequestRepository;

    public Map<String, String> validateGwRequest(GwRequest gwRequest) {
        Map<String, String> errors = new HashMap<>();

        if (gwRequest.getId() == null) {
            errors.put("transactionId", "Transaction ID is required");
        } else {
            try {
                UUID.fromString(gwRequest.getId().toString());
            } catch (IllegalArgumentException e) {
                errors.put("transactionId", "Invalid Transaction ID");
            }
        }

        if (gwRequest.getAmount() == null) {
            errors.put("amount", "Amount is required");
        } else if (gwRequest.getAmount() < 10 || gwRequest.getAmount() > 150000) {
            errors.put("amount", "Amount must be between KSh 10 and KSh 150,000");
        }

        if (gwRequest.getMobileNumber() == null || gwRequest.getMobileNumber().isEmpty()) {
            errors.put("mobileNumber", "Mobile number is required");
        } else if (!gwRequest.getMobileNumber().matches("^\\+2547[0-9]{8}$")) {
            errors.put("mobileNumber", "Invalid Safaricom mobile number");
        }

        return errors;
    }

    public void processPayment(GwRequest gwRequest) {


        // Validate GwRequest
        // Log GwRequest to MongoDB
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(gwRequest.getAmount());
        paymentRequest.setStatus(Status.PENDING);
        paymentRequest.setMobileNumber(gwRequest.getMobileNumber());
        paymentRequest.setTransactionId(gwRequest.getId());
        paymentRequestRepository.save(paymentRequest);

        // Send to 3rd Party PG

//        Body
//        {
//            "OriginatorConversationID": "3379ed8f-2308-4fd8-8a68-08c249776b50",
//                "InitiatorName": "testapi",
//                "SecurityCredential": "P0DAwEhniNq+oujuQdzqWSXa/UhYZ6sujX9WP+YtO9QlKMfa3hJ3507ImCBifEfwaj1ei4tp330KhbowDYWDetnwfPTIAqdtcyGtTgDKHZtdOWNx2UcDQ9SwoDo/lRq1alLMoh/vTYGCLivTSEnqlbYdJa/bnHnY8RXPCUSK6p710NOYI0AbhgNOQGxzYjRfNXwP2XiIU0ntLJmvzUMD1NysjHluNlw90vBCnYRtV89o3B3NQ725c6CaSbnfOpgmibrnF5uksyaQ/qKI8FNskmN7T5I3yxTGRZUrrBXmunCDQpnEuMEBgNzmtLStOqiM25SnYQ8LriccxQ5mLhx4mg==",
//                "CommandID": "BusinessPayment",
//                "Amount": 10,
//                "PartyA": 600995,
//                "PartyB": 254799264960,
//                "Remarks": "Test remarks",
//                "QueueTimeOutURL": "https://mydomain.com/b2c/queue",
//                "ResultURL": "https://mydomain.com/b2c/result",
//                "occasion": "Disbursment"
//        }
//        try {
//            // Mock sending request to PG
//            // Assume success and send callback
//            Result result = new Result();
//            result.setId(gwRequest.getId());
//            result.setStatus("Success");
//            result.setRef("MPESA12345");
//            handleCallback(result);
//        } catch (Exception e) {
//            // Log to PendingRequest if failed
//            PendingRequest pendingRequest = new PendingRequest();
//            pendingRequest.setId(gwRequest.getId());
//            pendingRequest.setAmount(gwRequest.getAmount());
//            pendingRequest.setMobileNumber(gwRequest.getMobileNumber());
//            pendingRequestRepository.save(pendingRequest);
//        }
    }


}
