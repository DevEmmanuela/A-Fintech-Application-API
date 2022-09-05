package com.emmanuela.fintechapplication.response;

import com.emmanuela.fintechapplication.enums.TransactionType;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryResponse {
    private Long id;
    private String name;
    private String bank;
    private String transactionTime;
    private TransactionType type;
    private String amount;
}
