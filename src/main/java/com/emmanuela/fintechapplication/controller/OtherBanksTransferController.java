package com.emmanuela.fintechapplication.controller;

import com.emmanuela.fintechapplication.entities.FlwBank;
import com.emmanuela.fintechapplication.request.FlwAccountRequest;
import com.emmanuela.fintechapplication.request.TransferRequest;
import com.emmanuela.fintechapplication.request.VerifyTransferRequest;
import com.emmanuela.fintechapplication.response.FlwAccountResponse;
import com.emmanuela.fintechapplication.service.TransactionService;
import com.emmanuela.fintechapplication.service.serviceImpl.OtherBanksTransactionImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path="/transfer")
public class OtherBanksTransferController {
    private final TransactionService transactionService;

    private final OtherBanksTransactionImpl otherBanksTransfer;

    @GetMapping("/banks")
    public List<FlwBank> getBanks() {
        return transactionService.getBanks();
    }

    @PostMapping("/otherbank-account-query")
    public FlwAccountResponse resolveAccount(@RequestBody FlwAccountRequest flwAccountRequest) {
        return transactionService.resolveAccount(flwAccountRequest);
    }

    @PostMapping("/other-bank")
    public String makeTransfer(@RequestBody TransferRequest transferRequest) {
        transactionService.initiateOtherBankTransfer(transferRequest);
        return "Transfer successful";
    }

    @PostMapping("/verify-transfer")
    public ResponseEntity<String> verify(@RequestBody VerifyTransferRequest verifyTransferRequest){
        otherBanksTransfer.verifyTransfer(verifyTransferRequest);
    return ResponseEntity.ok("status updated");
    }
}
