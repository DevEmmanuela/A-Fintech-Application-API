package com.emmanuela.fintechapplication.entities;

import com.emmanuela.fintechapplication.enums.TransactionType;
import com.emmanuela.fintechapplication.enums.UsersStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

@Entity
public class Transaction extends BaseClass{
    @NotNull
    private BigDecimal amount;


    @NotNull
    private String narration;
    private String destinationAccountNumber;
    private String destinationBank;
    private String destinationFullName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UsersStatus userStatus;

    @NotNull
    private String clientRef;

//    @NotNull
    private Long flwRef;

    private String senderFullName;
    private String senderBankName;
    private String senderAccountNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

//    @NotNull
    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private Wallet wallet;

    private String transferStatus;

}

