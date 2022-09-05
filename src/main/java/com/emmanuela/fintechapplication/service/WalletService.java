package com.emmanuela.fintechapplication.service;

import com.emmanuela.fintechapplication.dto.AccountFundDTO;
import com.emmanuela.fintechapplication.dto.WalletDto;
import com.emmanuela.fintechapplication.entities.Users;
import com.emmanuela.fintechapplication.entities.Wallet;
import com.emmanuela.fintechapplication.request.FundAccountRequest;
import com.emmanuela.fintechapplication.response.BaseResponse;
import org.springframework.boot.configurationprocessor.json.JSONException;


public interface WalletService {

    Wallet createWallet(Users walletRequestDetails) throws JSONException;
    WalletDto viewWalletDetails();
    void fundWallet(FundAccountRequest fundAccountRequest);

    BaseResponse<String> fundWallet(AccountFundDTO amount);
}
