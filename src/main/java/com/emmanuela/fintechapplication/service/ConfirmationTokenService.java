package com.emmanuela.fintechapplication.service;

import com.emmanuela.fintechapplication.validations.token.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationToken token);
    Optional<ConfirmationToken> getToken(String token);
    void setConfirmedAt(String token);
}
