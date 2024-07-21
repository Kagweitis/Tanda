package com.tanda.payments.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanda.payments.Configs.KafkaListeners;
import com.tanda.payments.DTO.AcknowledgeResponse;
import com.tanda.payments.DTO.B2CSuccessfullResponse;
import com.tanda.payments.DTO.GwRequest;
import com.tanda.payments.Services.PaymentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("gw/request")
    public ResponseEntity<?> gwRequest(@RequestBody GwRequest gwRequest){
        Map<String, String> errors = paymentsService.validateGwRequest(gwRequest);
        if (!errors.isEmpty()) {
            return new ResponseEntity<>(errors.values(), HttpStatus.BAD_REQUEST);
        }
        kafkaTemplate.send("payment-topic", gwRequest);
        return new ResponseEntity<>("Request sent to Kafka", HttpStatus.OK);
    }


    @PostMapping(path = "/b2c-transaction-result", produces = "application/json")
    public ResponseEntity<?> b2cTransactionAsyncResults(@RequestBody B2CSuccessfullResponse b2CSuccessfullResponse)
            {
        log.info("============ B2C Transaction Response =============");
        log.info(String.valueOf(b2CSuccessfullResponse));
        return ResponseEntity.ok("success");
    }
}
