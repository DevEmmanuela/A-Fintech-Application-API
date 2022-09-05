package com.emmanuela.fintechapplication.controller;

import com.emmanuela.fintechapplication.dto.LoginRequestPayload;
import com.emmanuela.fintechapplication.dto.LoginResponseDto;
import com.emmanuela.fintechapplication.dto.UsersResponse;
import com.emmanuela.fintechapplication.pagination_criteria.TransactionHistoryPages;
import com.emmanuela.fintechapplication.request.EmailVerifyRequest;
import com.emmanuela.fintechapplication.request.PasswordRequest;
import com.emmanuela.fintechapplication.request.ResetPasswordRequest;
import com.emmanuela.fintechapplication.response.BaseResponse;
import com.emmanuela.fintechapplication.response.TransactionHistoryResponse;
import com.emmanuela.fintechapplication.service.serviceImpl.LoginServiceImpl;
import com.emmanuela.fintechapplication.service.serviceImpl.TransactionHistoryImpl;
import com.emmanuela.fintechapplication.service.serviceImpl.UsersServiceImpl;
import com.emmanuela.fintechapplication.service.serviceImpl.WalletServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping
@CrossOrigin
public class UsersController {

    private final UsersServiceImpl usersService;

    private final LoginServiceImpl loginService;

    private final WalletServiceImpl walletService;
    private final TransactionHistoryImpl transactionHistory;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestPayload loginRequestPayload) throws JSONException {
            log.info("successful");
            String token = loginService.login(loginRequestPayload);
            return new ResponseEntity<>(new LoginResponseDto(token),HttpStatus.OK);
    }


    @GetMapping("/viewUser")
    public ResponseEntity<UsersResponse> getUsers(){
       UsersResponse usersResponse = usersService.getUser();
        return new ResponseEntity<>(usersResponse, HttpStatus.OK);
    }

    @GetMapping("/transactionHistory")
    public BaseResponse<Page<TransactionHistoryResponse>> getTransactionHistory(TransactionHistoryPages transactionHistoryPages) {
        return transactionHistory.getTransactionHistory(transactionHistoryPages);
    }

    @PostMapping("/changePassword")
    public BaseResponse<String> changePassword(@RequestBody PasswordRequest passwordRequest){
        return usersService.changePassword(passwordRequest);
    }

    @PostMapping("/forgot-Password")
    public BaseResponse<String> forgotPassword(@RequestBody EmailVerifyRequest emailVerifyRequest) throws MessagingException{
        return usersService.generateResetToken(emailVerifyRequest);
    }

    @PostMapping("/reset-Password")
    public BaseResponse<String> resetPassword(@RequestBody ResetPasswordRequest passwordRequest, @RequestParam ("token") String token){
        return usersService.resetPassword(passwordRequest, token);
    }

    @GetMapping("/getUserName")
    public BaseResponse<String> getName(){
        return usersService.getUserName();
    }

}
