package com.emmanuela.fintechapplication.controller;

import com.emmanuela.fintechapplication.dto.ResolveLocalDTO;
import com.emmanuela.fintechapplication.request.TransferRequest;
import com.emmanuela.fintechapplication.response.BaseResponse;
import com.emmanuela.fintechapplication.service.LocalTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController

public class LocalTransferController {

    private final LocalTransferService localTransferService;

    @PostMapping("/transfer/local")
    public String localTransfer(@RequestBody TransferRequest transferRequest){
         localTransferService.makeLocalTransfer(transferRequest);
         return "Transfer successful";
    }

    @PostMapping("/transfer/resolve-local-account")
    public BaseResponse<?> resolveLocalAccount(@RequestBody ResolveLocalDTO accountNumber){
        return localTransferService.resolveLocalAccount(accountNumber);
    }
}
