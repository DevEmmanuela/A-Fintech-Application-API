package com.emmanuela.fintechapplication.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UsersResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String BVN;

}
