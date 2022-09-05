package com.emmanuela.fintechapplication.controller;

import com.emmanuela.fintechapplication.request.TransferRequest;
import com.emmanuela.fintechapplication.service.LocalTransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

@ContextConfiguration(classes = {LocalTransferController.class})
@ExtendWith(SpringExtension.class)
class LocalTransactionControllerTest {
    @Autowired
    private LocalTransferController localTransferController;

    @MockBean
    private LocalTransferService localTransferService;

    @Test
    void testLocalTransfer() throws Exception {
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setAccountName("Dr Jane Doe");
        transferRequest.setAccountNumber("42");
        transferRequest.setAmount(BigDecimal.valueOf(42L));
        transferRequest.setNarration("Narration");
        transferRequest.setPin("Pin");
        String content = (new ObjectMapper()).writeValueAsString(transferRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/transfer/local")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(localTransferController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

