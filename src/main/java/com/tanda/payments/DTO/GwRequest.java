package com.tanda.payments.DTO;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;


import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GwRequest {

    @NonNull
    private UUID id;

    @NonNull
    @Positive
    private Double amount;

    @NonNull
    @Pattern(regexp = "^\\+2547[0-9]{8}$", message = "Invalid Safaricom mobile number")
    private String mobileNumber;
    private Date createdAt;

}
