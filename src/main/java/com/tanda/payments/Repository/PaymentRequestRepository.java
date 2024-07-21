package com.tanda.payments.Repository;

import com.tanda.payments.Models.PaymentRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRequestRepository extends MongoRepository<PaymentRequest, UUID> {
}
