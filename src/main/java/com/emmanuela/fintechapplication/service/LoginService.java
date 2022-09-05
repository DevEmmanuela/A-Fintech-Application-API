package com.emmanuela.fintechapplication.service;

import com.emmanuela.fintechapplication.dto.LoginRequestPayload;

public interface LoginService {

    String login(LoginRequestPayload loginRequestPayload);
}
