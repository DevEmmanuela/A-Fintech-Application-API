package com.emmanuela.fintechapplication.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtherBankTransferResponse {
    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Date data;


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Date {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("account_number")
        private String accountNumber;

        @JsonProperty("bank_code")
        private String bankCode;

        @JsonProperty("full_name")
        private String fullName;

        @JsonProperty("created_at")
        private String createAt;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("debit_currency")
        private String debitCurrency;

        @JsonProperty("amount")
        private BigDecimal amount;

        @JsonProperty("fee")
        private BigDecimal fee;

        @JsonProperty("status")
        private String status;

        @JsonProperty("reference")
        private String reference;

        @JsonProperty("meta")
        private String meta;

        @JsonProperty("narration")
        private String narration;

        @JsonProperty("complete_message")
        private String completeMessage;

        @JsonProperty("requires_approval")
        private Integer requiresApproval;

        @JsonProperty("is_approved")
        private Integer isApproved;

        @JsonProperty("bank_name")
        private String bankName;
    }
}
