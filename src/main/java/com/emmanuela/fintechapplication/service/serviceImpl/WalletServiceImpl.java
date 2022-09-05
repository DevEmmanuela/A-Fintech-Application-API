package com.emmanuela.fintechapplication.service.serviceImpl;

import com.emmanuela.fintechapplication.dto.AccountFundDTO;
import com.emmanuela.fintechapplication.dto.WalletDto;
import com.emmanuela.fintechapplication.entities.Transaction;
import com.emmanuela.fintechapplication.entities.Users;
import com.emmanuela.fintechapplication.entities.Wallet;
import com.emmanuela.fintechapplication.enums.TransactionType;
import com.emmanuela.fintechapplication.repository.TransactionRepository;
import com.emmanuela.fintechapplication.repository.UsersRepository;
import com.emmanuela.fintechapplication.repository.WalletRepository;
import com.emmanuela.fintechapplication.request.FlwWalletRequest;
import com.emmanuela.fintechapplication.request.FundAccountRequest;
import com.emmanuela.fintechapplication.request.TransactionVerificationRequest;
import com.emmanuela.fintechapplication.response.BaseResponse;
import com.emmanuela.fintechapplication.response.FlwVirtualAccountResponse;
import com.emmanuela.fintechapplication.security.filter.JwtUtils;
import com.emmanuela.fintechapplication.service.WalletService;
import com.emmanuela.fintechapplication.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor @Slf4j

public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UsersRepository usersRepository;
    private final TransactionRepository transactionRepository;

   private final JwtUtils jwtUtils;
    private  String userToken = "";

    @Override
    public Wallet createWallet(Users user) throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + Constant.AUTHORIZATION);

        FlwWalletRequest payload = generatePayload(user);

        HttpEntity<FlwWalletRequest> request = new HttpEntity<>(payload, headers);

        FlwVirtualAccountResponse body = restTemplate.exchange(
                Constant.CREATE_VIRTUAL_ACCOUNT_API,
                HttpMethod.POST,
                request,
                FlwVirtualAccountResponse.class
        ).getBody();

        return Wallet.builder()
                .accountNumber(body.getData().getAccountNumber())
                .balance(BigDecimal.valueOf(0.00))
                .bankName(body.getData().getBankName())
                .createAt(LocalDateTime.now())
                .txRef(payload.getTx_ref())
                .modifyAt(LocalDateTime.now())
                .build();
    }

    public void getToken(String token){
        this.userToken = token;
    }




    @Override
    public WalletDto viewWalletDetails() {

    WalletDto walletDto = new WalletDto();

    User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Users users = usersRepository.findUsersByEmail(user.getUsername());
    if(users == null){
        throw new UsernameNotFoundException("User Not found");
    }
    Wallet wallet = walletRepository.findWalletByUsers(users.getWallet().getUsers());
        BeanUtils.copyProperties(wallet,walletDto);
        return walletDto;
    }

    @Override
    public BaseResponse<String> fundWallet(
            AccountFundDTO amount) {

        if(amount.getAmount().compareTo(BigDecimal.ONE) < 0){
            return new BaseResponse<>(HttpStatus.BAD_REQUEST, "Invalid Amount", null);
        }
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = usersRepository.findUsersByEmail(user.getUsername());
        Wallet wallet = walletRepository.findWalletByUsers(users);
        BigDecimal bigDecimal = wallet.getBalance().add(amount.getAmount());
        wallet.setBalance(bigDecimal);
        walletRepository.save(wallet);
        return new BaseResponse<>(HttpStatus.OK, "Account has been funded successfully", null);


    }

    private FlwWalletRequest generatePayload(Users user) {
        FlwWalletRequest jsono = new FlwWalletRequest();
        jsono.setEmail(user.getEmail());
        jsono.set_permanent(true);
        jsono.setBvn(user.getBVN());
        jsono.setPhonenumber(user.getPhoneNumber());
        jsono.setFirstname(user.getFirstName());
        jsono.setLastname(user.getLastName());
        jsono.setTx_ref(UUID.randomUUID().toString());
        jsono.setNarration(user.getFirstName() + " " + user.getLastName());

        return jsono;
    }



    @Override
    public void fundWallet(FundAccountRequest fundAccountRequest) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + Constant.AUTHORIZATION);
        HttpEntity<TransactionVerificationRequest> request = new HttpEntity<>(headers);

        Integer dataId = fundAccountRequest.getData().getId();
        BigDecimal txAmount = fundAccountRequest.getData().getAmount();
        String txTransRef = fundAccountRequest.getData().getTx_ref();
        String txStatus = fundAccountRequest.getData().getStatus();
        String txTransCurrency = fundAccountRequest.getData().getCurrency();



        TransactionVerificationRequest flwRequestResponse = getTransactionVerificationRequest(restTemplate, request, dataId);

        BigDecimal rxAmount = flwRequestResponse.getData().getAmount();
        String  rxTransRef = flwRequestResponse.getData().getTxRef();
        String rxStatus = flwRequestResponse.getData().getStatus();
        String rxTransCurrency = flwRequestResponse.getData().getCurrency();

        if(!txAmount.equals(rxAmount)){
            throw new CommandAcceptanceException("Amount mismatch");
        }
        if(!txTransRef.equals(rxTransRef)){
            throw new CommandAcceptanceException("Transaction Reference mismatch");
        }
        if(!txStatus.equals("successful") || !rxStatus.equals("successful")){
            throw new CommandAcceptanceException("failed transaction");
        }
        if(!txTransCurrency.equals(rxTransCurrency)){
            throw new CommandAcceptanceException("Transaction currency mismatch");
        }

        Wallet wallet = getWallet(flwRequestResponse);

        transactionHistory(flwRequestResponse, wallet);
    }

    private TransactionVerificationRequest getTransactionVerificationRequest(RestTemplate restTemplate, HttpEntity<TransactionVerificationRequest> request, Integer dataId) {
        TransactionVerificationRequest flwRequestResponse = restTemplate.exchange(
                Constant.VERIFY_TRANSACTION+ dataId +"/verify",
                HttpMethod.GET,
                request,
                TransactionVerificationRequest.class
        ).getBody();
        return flwRequestResponse;
    }

    private void transactionHistory(TransactionVerificationRequest flwRequestResponse, Wallet wallet) {
        Transaction transaction = Transaction.builder()
               .amount(flwRequestResponse.getData().getAmount())
               .narration("Wallet Fund")
               .userStatus(wallet.getUsers().getUsersStatus())
               .clientRef(UUID.randomUUID().toString())
               .transactionType(TransactionType.FUND)
               .build();
        transactionRepository.save(transaction);
    }

    private Wallet getWallet(TransactionVerificationRequest flwRequestResponse) {
        Wallet wallet = walletRepository.findByTxRef(flwRequestResponse.getData().getTxRef());
        wallet.setBalance(wallet.getBalance().add(flwRequestResponse.getData().getAmount()));
        walletRepository.save(wallet);
        return wallet;
    }


}
