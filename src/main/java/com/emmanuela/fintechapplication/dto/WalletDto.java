package com.emmanuela.fintechapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class WalletDto {

    private String accountNumber;
    private BigDecimal balance;
    private String bankName;
}
