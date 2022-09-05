package com.emmanuela.fintechapplication.service;

import com.emmanuela.fintechapplication.dto.UsersDTO;
import com.emmanuela.fintechapplication.dto.UsersResponse;
import com.emmanuela.fintechapplication.entities.Users;
import com.emmanuela.fintechapplication.entities.Wallet;
import com.emmanuela.fintechapplication.request.EmailVerifyRequest;
import com.emmanuela.fintechapplication.request.PasswordRequest;
import com.emmanuela.fintechapplication.request.ResetPasswordRequest;
import com.emmanuela.fintechapplication.response.BaseResponse;
import com.emmanuela.fintechapplication.validations.token.ConfirmationToken;
import org.springframework.boot.configurationprocessor.json.JSONException;

import javax.mail.MessagingException;

public interface UsersService {
    String registerUser(UsersDTO usersDTO) throws Exception;
    void saveToken(String token, Users user);
    void enableUser(String email) throws Exception;
    Wallet generateWallet(Users user) throws Exception;
    UsersResponse getUser();
    void deleteUnverifiedToken(ConfirmationToken token);

    BaseResponse<String> generateResetToken(EmailVerifyRequest emailVerifyRequest) throws MessagingException;

    BaseResponse<String> resetPassword(ResetPasswordRequest resetPasswordRequest, String token);

    BaseResponse<String> changePassword(PasswordRequest passwordRequest);

    BaseResponse<String>  getUserName();


}
