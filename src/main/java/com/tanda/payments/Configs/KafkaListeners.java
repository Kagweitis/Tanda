package com.tanda.payments.Configs;

import com.tanda.payments.DTO.GwRequest;
import com.tanda.payments.Services.PaymentsService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaListeners {


    private final PaymentsService paymentService;

    @KafkaListener(topics = "payment-topic", groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
    public void listenPaymentRequests(GwRequest gwRequest) {
            log.info("++++++++++++++ request ++++++++++++++++ "+gwRequest);
        paymentService.processPayment(gwRequest);
    }
}
