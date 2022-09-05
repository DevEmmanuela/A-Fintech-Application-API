package com.emmanuela.fintechapplication.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class OtherBankTransferRequest {
    @JsonProperty("account_bank")
    private String accountBank;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("narration")
    private String narration;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("reference")
    private String reference;

    @JsonProperty("debit_currency")
    private String debitCurrency;

    @JsonProperty("callback_url")
    private String callbackUrl;

}
