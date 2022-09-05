package com.emmanuela.fintechapplication.service;

import com.emmanuela.fintechapplication.dto.ResolveLocalDTO;
import com.emmanuela.fintechapplication.entities.Transaction;
import com.emmanuela.fintechapplication.request.TransferRequest;
import com.emmanuela.fintechapplication.response.BaseResponse;

public interface LocalTransferService {

//    String localTransfer(TransferRequest transferRequest);

    BaseResponse<Transaction> makeLocalTransfer(TransferRequest transferRequest);

    BaseResponse<?> resolveLocalAccount(ResolveLocalDTO accountNumber);
}
