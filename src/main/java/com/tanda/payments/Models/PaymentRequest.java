package com.tanda.payments.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collation = "payment_requests")
public class PaymentRequest {

    @Id
    private UUID transactionId;

    private Double amount;
    private String mobileNumber;
    private Status status;

    @CreatedDate
    private Date createdAt;
}
