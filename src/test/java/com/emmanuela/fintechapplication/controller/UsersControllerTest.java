package com.emmanuela.fintechapplication.controller;

import com.emmanuela.fintechapplication.dto.UsersResponse;
import com.emmanuela.fintechapplication.pagination_criteria.TransactionHistoryPages;
import com.emmanuela.fintechapplication.service.serviceImpl.LoginServiceImpl;
import com.emmanuela.fintechapplication.service.serviceImpl.UsersServiceImpl;
import com.emmanuela.fintechapplication.service.serviceImpl.WalletServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UsersController.class})
@ExtendWith(SpringExtension.class)
class UsersControllerTest {
    @MockBean
    private LoginServiceImpl loginServiceImpl;

    @Autowired
    private UsersController usersController;

    @MockBean
    private UsersServiceImpl usersServiceImpl;

    @MockBean
    private WalletServiceImpl walletServiceImpl;


    @Test
    void testGetTransactionHistory() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/transactionHistory");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(usersController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetUsers() throws Exception {
        when(usersServiceImpl.getUser())
                .thenReturn(new UsersResponse("Jane", "Doe", "jane.doe@example.org", "4105551212", "BVN"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/viewUser");
        MockMvcBuilders.standaloneSetup(usersController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"phoneNumber\":\"4105551212\",\"bvn"
                                        + "\":\"BVN\"}"));
    }
}

