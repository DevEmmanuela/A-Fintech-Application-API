package com.emmanuela.fintechapplication.service;

import com.emmanuela.fintechapplication.entities.FlwBank;
import com.emmanuela.fintechapplication.request.FlwAccountRequest;
import com.emmanuela.fintechapplication.request.TransferRequest;
import com.emmanuela.fintechapplication.request.VerifyTransferRequest;
import com.emmanuela.fintechapplication.response.FlwAccountResponse;

import java.util.List;

public interface TransactionService {
    void initiateOtherBankTransfer(TransferRequest transferRequest);
    List<FlwBank> getBanks();
    FlwAccountResponse resolveAccount(FlwAccountRequest flwAccountRequest);
    void verifyTransfer(VerifyTransferRequest verifyTransferRequest);
}
