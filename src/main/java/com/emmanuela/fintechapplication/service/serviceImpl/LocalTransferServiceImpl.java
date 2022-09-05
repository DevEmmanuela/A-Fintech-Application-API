package com.emmanuela.fintechapplication.service.serviceImpl;
import com.emmanuela.fintechapplication.dto.ResolveLocalDTO;
import com.emmanuela.fintechapplication.entities.Transaction;
import com.emmanuela.fintechapplication.entities.Users;
import com.emmanuela.fintechapplication.entities.Wallet;
import com.emmanuela.fintechapplication.enums.TransactionType;
import com.emmanuela.fintechapplication.enums.UsersStatus;
import com.emmanuela.fintechapplication.repository.TransactionRepository;
import com.emmanuela.fintechapplication.repository.UsersRepository;
import com.emmanuela.fintechapplication.repository.WalletRepository;
import com.emmanuela.fintechapplication.request.TransferRequest;
import com.emmanuela.fintechapplication.response.BaseResponse;
import com.emmanuela.fintechapplication.service.LocalTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalTransferServiceImpl implements LocalTransferService {

    private final WalletRepository walletRepository;

    private final UsersRepository usersRepository;

    private final TransactionRepository transactionRepository;

    private final BCryptPasswordEncoder encoder;

    @Transactional
  @Override
    public BaseResponse<Transaction> makeLocalTransfer(TransferRequest transferRequest) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepository.findUsersByEmail(userEmail);
        if (user == null) {
            return new BaseResponse<>(HttpStatus.UNAUTHORIZED, "User not logged in", null);
        }
        Wallet recipientWallet = walletRepository.findWalletByAccountNumber(transferRequest.getAccountNumber());
        if (recipientWallet == null) {
            return new BaseResponse<>(HttpStatus.NOT_FOUND, "Recipient Wallet not found", null);
        }
        Users recipient = recipientWallet.getUsers();
        if (recipient == null) {
            return new BaseResponse<>(HttpStatus.NOT_FOUND, "Recipient not found", null);
        } else  if (user == recipient) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST, "Can't transfer to self", null);
        }
        Wallet userWallet = walletRepository.findWalletByUsers(user);
        if (userWallet == null) {
            return new BaseResponse<>(HttpStatus.NOT_FOUND, "Sender Wallet not found", null);
        }
        if (transferRequest.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST, "Invalid transfer amount", null);
        }
        if (userWallet.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST, "Insufficient funds", null);
        }
        if (!encoder.matches(transferRequest.getPin(), user.getPin())) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST, "Incorrect transfer pin", null);
        }
        String recipientName = recipient.getFirstName() + " " + recipient.getLastName();
        Transaction newTransfer = Transaction.builder()
                .senderFullName(user.getFirstName() + " " + user.getLastName())
                .senderAccountNumber(userWallet.getAccountNumber())
                .senderBankName(userWallet.getBankName())
                .destinationFullName(recipientName)
                .destinationAccountNumber(transferRequest.getAccountNumber())
                .destinationBank(recipientWallet.getBankName())
                .amount(transferRequest.getAmount())
                .clientRef(UUID.randomUUID().toString())
                .narration(transferRequest.getNarration())
                .transactionType(TransactionType.CREDIT)
                .userStatus(UsersStatus.ACTIVE)
                .wallet(recipientWallet)
                .flwRef(0L)
                .build();
        transactionRepository.save(newTransfer);
        userWallet.setBalance(userWallet.getBalance().subtract(transferRequest.getAmount()));
        recipientWallet.setBalance(recipientWallet.getBalance().add(transferRequest.getAmount()));
        walletRepository.save(userWallet);
        walletRepository.save(recipientWallet);
        transactionRepository.save(newTransfer);
        return new BaseResponse<>(HttpStatus.OK, "Transfer to " + recipientName + " successful", newTransfer);
    }

    @Override
    public BaseResponse<?> resolveLocalAccount(ResolveLocalDTO accountNumber) {
        Wallet wallet = walletRepository.findWalletByAccountNumber(accountNumber.getAccountNumber());

        String accountName = wallet.getUsers().getFirstName() + " " + wallet.getUsers().getLastName();
        if(wallet == null){
            return new BaseResponse<>(HttpStatus.OK, "Account Not Found", null);
        }
        return new BaseResponse<>(HttpStatus.OK,"Account Found", accountName);
    }


}
