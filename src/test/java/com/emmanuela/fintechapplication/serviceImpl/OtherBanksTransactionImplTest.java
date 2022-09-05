package com.emmanuela.fintechapplication.serviceImpl;

import com.emmanuela.fintechapplication.request.FlwAccountRequest;
import com.emmanuela.fintechapplication.response.FlwAccountResponse;
import com.emmanuela.fintechapplication.response.FlwBankResponse;
import com.emmanuela.fintechapplication.util.Constant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class OtherBanksTransactionImplTest {

    @Test
    void testGetBanks() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + Constant.AUTHORIZATION);

        HttpEntity<FlwBankResponse> request = new HttpEntity<>(null, headers);

        FlwBankResponse flwBankResponse = restTemplate.exchange(
                Constant.GET_BANKS_API + "/NG",
                HttpMethod.GET,
                request,
                FlwBankResponse.class
        ).getBody();

        assertEquals("success", flwBankResponse.getStatus());
        assertNotNull(flwBankResponse.getData());
    }


    @Test
    public void resolveAccount() {
        FlwAccountRequest flwAccountRequest = new FlwAccountRequest("0690000032", "044");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + Constant.AUTHORIZATION);

        HttpEntity<FlwAccountRequest> request = new HttpEntity<>(flwAccountRequest, headers);

        FlwAccountResponse flwAccountResponse = restTemplate.exchange(
                Constant.RESOLVE_ACCOUNT_API,
                HttpMethod.POST,
                request,
                FlwAccountResponse.class).getBody();

        assertEquals("success", flwAccountResponse.getStatus());
        assertNotNull(flwAccountResponse.getData());
    }

}

