package com.emmanuela.fintechapplication.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlwWalletRequest {

    private String email;
    private String firstname;
    private String lastname;
    private String bvn;
    private String tx_ref;
    private String phonenumber;
    private String narration;
    @JsonProperty(value = "is_permanent")
    private boolean is_permanent;
}
