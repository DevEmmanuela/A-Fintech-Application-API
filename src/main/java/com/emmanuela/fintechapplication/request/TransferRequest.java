package com.emmanuela.fintechapplication.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest{

    @NotNull(message = "Account number is required")
    private String accountNumber;

    @NotNull(message = "Account name is required")
    private String accountName;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @NotNull(message = "Narration is required")
    private String narration;

    @NotNull(message = "Pin is required")
    private String pin;

    private String bankCode;
}