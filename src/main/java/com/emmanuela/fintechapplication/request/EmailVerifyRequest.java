package com.emmanuela.fintechapplication.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class EmailVerifyRequest {
    @Email
    private String email;
}
