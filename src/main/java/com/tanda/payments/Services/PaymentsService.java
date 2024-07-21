package com.tanda.payments.Services;

import com.tanda.payments.DTO.B2CResponse;
import com.tanda.payments.DTO.B2CSuccessfullResponse;
import com.tanda.payments.DTO.GwRequest;
import com.tanda.payments.DTO.InternalB2CTransactionRequest;
import com.tanda.payments.Models.PaymentRequest;
import com.tanda.payments.Models.PendingRequestLog;
import com.tanda.payments.Models.Status;
import com.tanda.payments.Repository.PaymentRequestRepository;
import com.tanda.payments.Repository.PendingRequestLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentsService {


    private final PaymentRequestRepository paymentRequestRepository;
    private final PendingRequestLogRepository pendingRequestLogRepository;
    private final DarajaService darajaService;

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

    public ResponseEntity<?> processPayment(GwRequest gwRequest) {


        // Validate GwRequest
        // Log GwRequest to MongoDB
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(gwRequest.getAmount());
        paymentRequest.setStatus(Status.PENDING);
        paymentRequest.setMobileNumber(gwRequest.getMobileNumber());
        paymentRequest.setTransactionId(gwRequest.getId());
        paymentRequestRepository.save(paymentRequest);

        // Send to 3rd Party PG
        InternalB2CTransactionRequest internalB2CTransactionRequest = new InternalB2CTransactionRequest();
        internalB2CTransactionRequest.setAmount(paymentRequest.getAmount().toString());
        internalB2CTransactionRequest.setCommandID("BusinessPayment");
        internalB2CTransactionRequest.setOccasion("Disbursement");
        internalB2CTransactionRequest.setPartyB(paymentRequest.getMobileNumber());
        internalB2CTransactionRequest.setRemarks("Tanda pay");

        try {
            darajaService.performB2CTransaction(internalB2CTransactionRequest);
            return ResponseEntity.ok().body("Success");
        } catch (Exception e){
            log.error("Failed to make payment "+e.getMessage());
            paymentRequest.setStatus(Status.FAILED);
            paymentRequestRepository.save(paymentRequest);
            return ResponseEntity.internalServerError().body("could not make b2c payment please try again "+e);
        }
    }

    public void handleB2CTransactionCallback(B2CSuccessfullResponse b2CSuccessfullResponse) {
        // Validate and update the payment request log
        PaymentRequest paymentRequest = paymentRequestRepository.findByTransactionId(UUID.fromString(b2CSuccessfullResponse.getResult().getOriginatorConversationID()));
        if (paymentRequest != null) {
            if ("0".equals(b2CSuccessfullResponse.getResult())) {
                paymentRequest.setStatus(Status.SUCCESSFUL);
            } else {
                paymentRequest.setStatus(Status.FAILED);
            }
            paymentRequestRepository.save(paymentRequest);

            // Remove from pending log if exists
            PendingRequestLog pendingRequest = pendingRequestLogRepository.findByPaymentRequestId(b2CSuccessfullResponse.getResult().getOriginatorConversationID());
            if (pendingRequest != null) {
                pendingRequestLogRepository.delete(pendingRequest);
            }
        }
    }

    private void logPendingRequest(PaymentRequest paymentRequest) {
        PendingRequestLog pendingRequest = new PendingRequestLog();
        pendingRequest.setPaymentRequestId(String.valueOf(paymentRequest.getTransactionId()));
        pendingRequest.setRetryCount(String.valueOf(0));
        pendingRequestLogRepository.save(pendingRequest);
    }


}
