package com.emmanuela.fintechapplication.service;

import com.emmanuela.fintechapplication.dto.UsersDTO;
import com.emmanuela.fintechapplication.entities.Users;
import org.springframework.boot.configurationprocessor.json.JSONException;

public interface RegistrationService {

    String register(UsersDTO usersDTO) throws Exception;
    void sendMailVerificationLink(String name, String email, String link);
    void resendVerificationEmail(Users user) throws Exception;
    String confirmToken(String token) throws Exception;
}
