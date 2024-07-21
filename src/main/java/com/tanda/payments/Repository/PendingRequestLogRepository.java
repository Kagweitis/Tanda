package com.tanda.payments.Repository;

import com.tanda.payments.Models.PendingRequestLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PendingRequestLogRepository extends MongoRepository<PendingRequestLog, String> {

    PendingRequestLog findByPaymentRequestId(String paymentRequestId);

}
