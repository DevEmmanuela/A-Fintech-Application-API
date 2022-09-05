package com.emmanuela.fintechapplication.service.serviceImpl;

import com.emmanuela.fintechapplication.entities.Transaction;
import com.emmanuela.fintechapplication.entities.Users;
import com.emmanuela.fintechapplication.entities.Wallet;
import com.emmanuela.fintechapplication.enums.TransactionType;
import com.emmanuela.fintechapplication.pagination_criteria.TransactionHistoryPages;
import com.emmanuela.fintechapplication.repository.TransactionRepository;
import com.emmanuela.fintechapplication.repository.UsersRepository;
import com.emmanuela.fintechapplication.repository.WalletRepository;
import com.emmanuela.fintechapplication.response.BaseResponse;
import com.emmanuela.fintechapplication.response.TransactionHistoryResponse;
import com.emmanuela.fintechapplication.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TransactionHistoryImpl implements TransactionHistoryService {
    private final WalletRepository walletRepository;
    private final UsersRepository usersRepository;
    private final TransactionRepository transactionRepository;

    public BaseResponse<Page<TransactionHistoryResponse>> getTransactionHistory(TransactionHistoryPages transactionHistoryPages) throws NullPointerException{
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepository.findUsersByEmail(userEmail);

        Sort sort = Sort.by(transactionHistoryPages.getSortDirection(), transactionHistoryPages.getSortBy());
        Pageable pageable = PageRequest.of(transactionHistoryPages.getPageNumber(), transactionHistoryPages.getPageSize(), sort);

        Wallet wallet = walletRepository.findWalletByUsers(user);

        String userAccountNumber = wallet.getAccountNumber();

        Page<Transaction> transactions = transactionRepository
                .findAllBySenderAccountNumberOrDestinationAccountNumber(userAccountNumber, userAccountNumber, pageable);

        List<TransactionHistoryResponse> userHistory = new ArrayList<>();

        for (Transaction transaction : transactions) {
            TransactionHistoryResponse response = mapTransferToTransactionHistoryResponse(userAccountNumber, transaction);
            userHistory.add(response);
        }

        PageImpl<TransactionHistoryResponse> transactionHistoryPage = new PageImpl<>(userHistory, pageable, transactions.getTotalElements());

        return new BaseResponse<>(HttpStatus.OK, "Transaction History retrieved", transactionHistoryPage);
    }

    private TransactionHistoryResponse mapTransferToTransactionHistoryResponse(String userAccountNumber, Transaction transaction) {

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("E, dd-MMMM-yyyy HH:mm");
        boolean isSender = userAccountNumber.equals(transaction.getSenderAccountNumber());
        String amount = String.format("\u20a6%,.2f",transaction.getAmount());
        return TransactionHistoryResponse.builder()
                .id(transaction.getId())
                .name(isSender ? transaction.getDestinationFullName() : transaction.getSenderFullName())
                .bank(isSender ? transaction.getDestinationBank() : transaction.getSenderBankName())
                .type(isSender ? TransactionType.DEBIT : TransactionType.CREDIT)
                .transactionTime(dateFormat.format(transaction.getCreatedAt()))
                .amount(isSender ? "- " + amount : "+ " + amount)
                .build();
    }
}
