package com.tanda.payments.Models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "pending_request_log")
public class PendingRequestLog {

    @Id
    private String id;

    private String paymentRequestId;
    private int amount;
    private String retryCount;

    @CreatedDate
    private Date createdAt;
}
