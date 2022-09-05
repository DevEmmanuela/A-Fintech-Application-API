package com.emmanuela.fintechapplication.service.serviceImpl;

import com.emmanuela.fintechapplication.customExceptions.InsufficientAmountException;
import com.emmanuela.fintechapplication.customExceptions.InvalidAmountException;
import com.emmanuela.fintechapplication.customExceptions.InvalidPinException;
import com.emmanuela.fintechapplication.entities.FlwBank;
import com.emmanuela.fintechapplication.entities.Transaction;
import com.emmanuela.fintechapplication.entities.Users;
import com.emmanuela.fintechapplication.entities.Wallet;
import com.emmanuela.fintechapplication.enums.TransactionType;
import com.emmanuela.fintechapplication.enums.UsersStatus;
import com.emmanuela.fintechapplication.repository.TransferRepository;
import com.emmanuela.fintechapplication.repository.UsersRepository;
import com.emmanuela.fintechapplication.repository.WalletRepository;
import com.emmanuela.fintechapplication.request.FlwAccountRequest;
import com.emmanuela.fintechapplication.request.OtherBankTransferRequest;
import com.emmanuela.fintechapplication.request.TransferRequest;
import com.emmanuela.fintechapplication.request.VerifyTransferRequest;
import com.emmanuela.fintechapplication.response.FlwAccountResponse;
import com.emmanuela.fintechapplication.response.FlwBankResponse;
import com.emmanuela.fintechapplication.response.OtherBankTransferResponse;
import com.emmanuela.fintechapplication.service.TransactionService;
import com.emmanuela.fintechapplication.util.Constant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class OtherBanksTransactionImpl implements TransactionService {

    private final UsersRepository usersRepository;
    private final WalletRepository walletRepository;
    private final TransferRepository transferRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    @Override
    public List<FlwBank> getBanks() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + Constant.AUTHORIZATION);

        HttpEntity<FlwBankResponse> request = new HttpEntity<>(null, headers);

        FlwBankResponse flwBankResponse = restTemplate.exchange(
                Constant.GET_BANKS_API + "/NG",
                HttpMethod.GET,
                request,
                FlwBankResponse.class
        ).getBody();

        return flwBankResponse.getData();
    }

    @Override
    public FlwAccountResponse resolveAccount(FlwAccountRequest flwAccountRequest) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + Constant.AUTHORIZATION);

        HttpEntity<FlwAccountRequest> request = new HttpEntity<>(flwAccountRequest, headers);
        try {
            return restTemplate.exchange(
                    Constant.RESOLVE_ACCOUNT_API,
                    HttpMethod.POST,
                    request,
                    FlwAccountResponse.class).getBody();
        } catch (Exception ex) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);

        }
    }

    @Override
    public void initiateOtherBankTransfer(TransferRequest transferRequest) {
        User user1 = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Users user = retrieveUserDetails(user1.getUsername());

        if (!validatePin(transferRequest.getPin(), user)) {
            throw new InvalidPinException("Pin error");
        }
        if (!validateRequestBalance(transferRequest.getAmount())) {
            throw new InvalidAmountException("Amount error");
        }
        if (!validateWalletBalance(transferRequest.getAmount(), user)) {
           throw new InsufficientAmountException("Balance error");
        }

        Transaction transaction = saveTransaction(user, transferRequest);
        OtherBankTransferResponse response = otherBankTransfer(transferRequest, transaction.getClientRef());
        transaction.setFlwRef(response.getData().getId());
        transferRepository.save(transaction);
    }

    private OtherBankTransferResponse otherBankTransfer(TransferRequest transferRequest, String clientRef) {
        OtherBankTransferRequest otherBankTransferRequest = OtherBankTransferRequest.builder()
                .accountBank(transferRequest.getBankCode())
                .accountNumber(transferRequest.getAccountNumber())
                .amount(transferRequest.getAmount())
                .currency("NGN")
                .narration(transferRequest.getNarration())
                .reference(clientRef)
                .debitCurrency("NGN")
                .callbackUrl(Constant.VERIFY_TRANSFER)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + Constant.AUTHORIZATION);

        HttpEntity<OtherBankTransferRequest> request = new HttpEntity<>(otherBankTransferRequest, headers);

        return restTemplate.exchange(
                Constant.OTHER_BANK_TRANSFER,
                HttpMethod.POST,
                request,
                OtherBankTransferResponse.class).getBody();
    }

    private Transaction saveTransaction(Users user, TransferRequest transferRequest) {
        String clientReference = UUID.randomUUID().toString();
        Wallet wallet = walletRepository.findWalletByUsers(user);

        BigDecimal amount = transferRequest.getAmount();
        BigDecimal balance = wallet.getBalance().subtract(amount);
        wallet.setBalance(balance);

        Transaction transaction = new Transaction();
                transaction.setDestinationFullName(transferRequest.getAccountName());
                transaction.setSenderAccountNumber(wallet.getAccountNumber());
                transaction.setAmount(transferRequest.getAmount());
                transaction.setClientRef(clientReference);
                transaction.setNarration(transferRequest.getNarration());
                transaction.setTransferStatus(Constant.STATUS);
                transaction.setDestinationAccountNumber(transferRequest.getAccountNumber());
                transaction.setDestinationBank(transferRequest.getBankCode());
                transaction.setTransactionType(TransactionType.DEBIT);
                transaction.setUserStatus(UsersStatus.ACTIVE);
                transaction.setCreatedAt(LocalDateTime.now());
                transaction.setWallet(wallet);
                transaction.setSenderFullName(wallet.getUsers().getFirstName() + " " + wallet.getUsers().getLastName());
                transaction.setSenderBankName(wallet.getBankName());

        walletRepository.save(wallet);
        return transferRepository.save(transaction);
    }

    private Users retrieveUserDetails(String username) {
        return usersRepository.findUsersByEmail(username);
    }

    private boolean validatePin(String pin, Users user) {
        return bCryptPasswordEncoder.matches(pin, user.getPin());
    }

    private boolean validateRequestBalance(BigDecimal requestAmount) {
        int result = requestAmount.compareTo(BigDecimal.valueOf(0));
        return result > 0;
    }

    private boolean validateWalletBalance(BigDecimal requestAmount, Users user) {
        Wallet wallet = walletRepository.findWalletByUsers(user);
        int result = wallet.getBalance().compareTo(requestAmount);
        if (result == 0 || result == 1) return true;
        return false;
    }

    @Override
    public void verifyTransfer(VerifyTransferRequest verifyTransferRequest){
        Long flwRef = Long.valueOf(verifyTransferRequest.getData().getId());
        String status = verifyTransferRequest.getData().getStatus();

        Transaction transaction = transferRepository.findTransfersByFlwRef(flwRef);
        transaction.setTransferStatus(status);
        transferRepository.save(transaction);
    }


}
