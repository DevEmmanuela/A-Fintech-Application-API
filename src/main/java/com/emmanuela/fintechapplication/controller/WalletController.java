package com.emmanuela.fintechapplication.controller;

import com.emmanuela.fintechapplication.dto.AccountFundDTO;
import com.emmanuela.fintechapplication.dto.WalletDto;
import com.emmanuela.fintechapplication.request.FundAccountRequest;
import com.emmanuela.fintechapplication.response.BaseResponse;
import com.emmanuela.fintechapplication.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet")
public class WalletController {
   private final WalletService walletService;

    @PostMapping("/fund")
    public ResponseEntity<String> fundWallet(@RequestBody FundAccountRequest fundAccountRequest) {
        walletService.fundWallet(fundAccountRequest);
        return ResponseEntity.ok("WebHook received");
    }

    @GetMapping("/viewWalletDetails")
    public ResponseEntity<WalletDto> viewWalletDetails()  {
        return new ResponseEntity<>(walletService.viewWalletDetails(), HttpStatus.OK);
    }

    @PostMapping("/fundLocalWallet")
    public BaseResponse<String> accountFund(@RequestBody AccountFundDTO amount){
        return walletService.fundWallet(amount);
    }

}
