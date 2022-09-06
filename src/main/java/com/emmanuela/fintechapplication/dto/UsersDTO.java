package com.emmanuela.fintechapplication.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {
    private String firstName;
    private String lastName;
    private String BVN;
    private String email;
    private String phoneNumber;
    private String password;
    private String confirmPassword;
    private String pin;
}
