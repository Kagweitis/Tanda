package com.tanda.payments.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanda.payments.Configs.KafkaListeners;
import com.tanda.payments.DTO.*;
import com.tanda.payments.Services.DarajaService;
import com.tanda.payments.Services.PaymentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/payments")
public class PaymentsController {

    private final KafkaTemplate<String, GwRequest> kafkaTemplate;
    private final KafkaListeners kafkaListeners;
    private final PaymentsService paymentsService;
    private final DarajaService darajaService;
    private final AcknowledgeResponse acknowledgeResponse;

    @PostMapping("gw/request")
    public ResponseEntity<?> gwRequest(@RequestBody GwRequest gwRequest){
        Map<String, String> errors = paymentsService.validateGwRequest(gwRequest);
        if (!errors.isEmpty()) {
            return new ResponseEntity<>(errors.values(), HttpStatus.BAD_REQUEST);
        }
        kafkaTemplate.send("payment-topic", gwRequest);
        return new ResponseEntity<>("Request sent to Kafka", HttpStatus.OK);
    }
    @GetMapping(path = "/token", produces = "application/json")
    public ResponseEntity<AccessTokenResponse> getAccessToken() {
        return ResponseEntity.ok(darajaService.getAccessToken());
    }


    @PostMapping(path = "/b2c-transaction-result", produces = "application/json")
    public ResponseEntity<?> b2cTransactionAsyncResults(@RequestBody B2CSuccessfullResponse b2CSuccessfullResponse)
            {
        log.info("============ B2C Transaction Response =============");
        log.info(String.valueOf(b2CSuccessfullResponse));
        // Tell Safaricom We've Recieved the Response
        return ResponseEntity.ok(acknowledgeResponse);
    }

    @PostMapping(path = "/b2c-queue-timeout", produces = "application/json")
    public ResponseEntity<AcknowledgeResponse> queueTimeout(@RequestBody Object object) {
        return ResponseEntity.ok(acknowledgeResponse);
    }

//    @PostMapping(path = "/b2c-transaction", produces = "application/json")
//    public ResponseEntity<B2CResponse> performB2CTransaction(@RequestBody InternalB2CTransactionRequest internalB2CTransactionRequest) throws IOException {
//        return ResponseEntity.ok(darajaService.performB2CTransaction(internalB2CTransactionRequest));
//    }
}
