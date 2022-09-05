package com.emmanuela.fintechapplication.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FlwAccountResponse {
     private String status;
     private String message;
     private  Data data;

     @AllArgsConstructor
     @NoArgsConstructor
     @Getter
     @Setter
    static class Data {
        @JsonProperty("account_number")
        public String accountNumber;

        @JsonProperty("account_name")
        public String accountName;

    }
}
