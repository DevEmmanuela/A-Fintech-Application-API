package com.emmanuela.fintechapplication.controller;

import com.emmanuela.fintechapplication.entities.FlwBank;
import com.emmanuela.fintechapplication.entities.Transaction;
import com.emmanuela.fintechapplication.entities.Users;
import com.emmanuela.fintechapplication.entities.Wallet;
import com.emmanuela.fintechapplication.enums.TransactionType;
import com.emmanuela.fintechapplication.enums.UsersStatus;
import com.emmanuela.fintechapplication.repository.TransferRepository;
import com.emmanuela.fintechapplication.repository.UsersRepository;
import com.emmanuela.fintechapplication.repository.WalletRepository;
import com.emmanuela.fintechapplication.request.FlwAccountRequest;
import com.emmanuela.fintechapplication.request.TransferRequest;
import com.emmanuela.fintechapplication.request.VerifyTransferRequest;
import com.emmanuela.fintechapplication.response.FlwAccountResponse;
import com.emmanuela.fintechapplication.service.serviceImpl.OtherBanksTransactionImpl;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OtherBanksTransferControllerTest {

    @Test
    void testGetBanks() {
        OtherBanksTransactionImpl otherBanksTransactionImpl = mock(OtherBanksTransactionImpl.class);
        ArrayList<FlwBank> flwBankList = new ArrayList<>();
        when(otherBanksTransactionImpl.getBanks()).thenReturn(flwBankList);
        UsersRepository usersRepository = mock(UsersRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        TransferRepository transferRepository = mock(TransferRepository.class);
        List<FlwBank> actualBanks = (new OtherBanksTransferController(otherBanksTransactionImpl,
                new OtherBanksTransactionImpl(usersRepository, walletRepository, transferRepository,
                        new BCryptPasswordEncoder()))).getBanks();
        assertSame(flwBankList, actualBanks);
        assertTrue(actualBanks.isEmpty());
        verify(otherBanksTransactionImpl).getBanks();
    }

    @Test
    void testResolveAccount() {
        OtherBanksTransactionImpl otherBanksTransactionImpl = mock(OtherBanksTransactionImpl.class);
        FlwAccountResponse flwAccountResponse = new FlwAccountResponse();
        when(otherBanksTransactionImpl.resolveAccount((FlwAccountRequest) any())).thenReturn(flwAccountResponse);
        UsersRepository usersRepository = mock(UsersRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        TransferRepository transferRepository = mock(TransferRepository.class);
        OtherBanksTransferController otherBanksTransferController = new OtherBanksTransferController(
                otherBanksTransactionImpl, new OtherBanksTransactionImpl(usersRepository, walletRepository,
                transferRepository, new BCryptPasswordEncoder()));
        assertSame(flwAccountResponse, otherBanksTransferController.resolveAccount(new FlwAccountRequest("42", "3")));
        verify(otherBanksTransactionImpl).resolveAccount((FlwAccountRequest) any());
    }


    @Test
    void testMakeTransfer() {
        OtherBanksTransactionImpl otherBanksTransactionImpl = mock(OtherBanksTransactionImpl.class);
        doNothing().when(otherBanksTransactionImpl).initiateOtherBankTransfer((TransferRequest) any());
        UsersRepository usersRepository = mock(UsersRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        TransferRepository transferRepository = mock(TransferRepository.class);
        OtherBanksTransferController otherBanksTransferController = new OtherBanksTransferController(
                otherBanksTransactionImpl, new OtherBanksTransactionImpl(usersRepository, walletRepository,
                transferRepository, new BCryptPasswordEncoder()));
        otherBanksTransferController.makeTransfer(new TransferRequest());
        verify(otherBanksTransactionImpl).initiateOtherBankTransfer((TransferRequest) any());
    }


    @Test
    void testVerify() {
        Users users = new Users();
        users.setBVN("BVN");
        users.setCreatedAt(null);
        users.setEmail("jane.doe@example.org");
        users.setFirstName("Jane");
        users.setId(123L);
        users.setLastName("Doe");
        users.setPassword("iloveyou");
        users.setPhoneNumber("4105551212");
        users.setPin("Pin");
        users.setRole("Role");
        users.setUpdatedAt(null);
        users.setUsersStatus(UsersStatus.ACTIVE);
        users.setWallet(new Wallet());

        Wallet wallet = new Wallet();
        wallet.setAccountNumber("42");
        wallet.setBalance(BigDecimal.valueOf(42L));
        wallet.setBankName("Bank Name");
        wallet.setCreateAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet.setId(123L);
        wallet.setModifyAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet.setTransactions(new ArrayList<>());
        wallet.setUpdatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet.setUsers(users);

        Users users1 = new Users();
        users1.setBVN("BVN");
        users1.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        users1.setEmail("jane.doe@example.org");
        users1.setFirstName("Jane");
        users1.setId(123L);
        users1.setLastName("Doe");
        users1.setPassword("iloveyou");
        users1.setPhoneNumber("4105551212");
        users1.setPin("Pin");
        users1.setRole("Role");
        users1.setUpdatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        users1.setUsersStatus(UsersStatus.ACTIVE);
        users1.setWallet(wallet);

        Wallet wallet1 = new Wallet();
        wallet1.setAccountNumber("42");
        wallet1.setBalance(BigDecimal.valueOf(42L));
        wallet1.setBankName("Bank Name");
        wallet1.setCreateAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet1.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet1.setId(123L);
        wallet1.setModifyAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet1.setTransactions(new ArrayList<>());
        wallet1.setUpdatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet1.setUsers(users1);

        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(42L));
        transaction.setClientRef("Client Ref");
        transaction.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        transaction.setDestinationAccountNumber("42");
        transaction.setDestinationBank("Destination Bank");
        transaction.setDestinationFullName("Dr Jane Doe");
        transaction.setFlwRef(1L);
        transaction.setId(123L);
        transaction.setNarration("Narration");
        transaction.setSenderAccountNumber("42");
        transaction.setSenderBankName("Sender Bank Name");
        transaction.setSenderFullName("Dr Jane Doe");
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction.setTransferStatus("Transfer Status");
        transaction.setUpdatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        transaction.setUserStatus(UsersStatus.ACTIVE);
        transaction.setWallet(wallet1);

        Users users2 = new Users();
        users2.setBVN("BVN");
        users2.setCreatedAt(null);
        users2.setEmail("jane.doe@example.org");
        users2.setFirstName("Jane");
        users2.setId(123L);
        users2.setLastName("Doe");
        users2.setPassword("iloveyou");
        users2.setPhoneNumber("4105551212");
        users2.setPin("Pin");
        users2.setRole("Role");
        users2.setUpdatedAt(null);
        users2.setUsersStatus(UsersStatus.ACTIVE);
        users2.setWallet(new Wallet());

        Wallet wallet2 = new Wallet();
        wallet2.setAccountNumber("42");
        wallet2.setBalance(BigDecimal.valueOf(42L));
        wallet2.setBankName("Bank Name");
        wallet2.setCreateAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet2.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet2.setId(123L);
        wallet2.setModifyAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet2.setTransactions(new ArrayList<>());
        wallet2.setUpdatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet2.setUsers(users2);

        Users users3 = new Users();
        users3.setBVN("BVN");
        users3.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        users3.setEmail("jane.doe@example.org");
        users3.setFirstName("Jane");
        users3.setId(123L);
        users3.setLastName("Doe");
        users3.setPassword("iloveyou");
        users3.setPhoneNumber("4105551212");
        users3.setPin("Pin");
        users3.setRole("Role");
        users3.setUpdatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        users3.setUsersStatus(UsersStatus.ACTIVE);
        users3.setWallet(wallet2);

        Wallet wallet3 = new Wallet();
        wallet3.setAccountNumber("42");
        wallet3.setBalance(BigDecimal.valueOf(42L));
        wallet3.setBankName("Bank Name");
        wallet3.setCreateAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet3.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet3.setId(123L);
        wallet3.setModifyAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet3.setTransactions(new ArrayList<>());
        wallet3.setUpdatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        wallet3.setUsers(users3);

        Transaction transaction1 = new Transaction();
        transaction1.setAmount(BigDecimal.valueOf(42L));
        transaction1.setClientRef("Client Ref");
        transaction1.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        transaction1.setDestinationAccountNumber("42");
        transaction1.setDestinationBank("Destination Bank");
        transaction1.setDestinationFullName("Dr Jane Doe");
        transaction1.setFlwRef(1L);
        transaction1.setId(123L);
        transaction1.setNarration("Narration");
        transaction1.setSenderAccountNumber("42");
        transaction1.setSenderBankName("Sender Bank Name");
        transaction1.setSenderFullName("Dr Jane Doe");
        transaction1.setTransactionType(TransactionType.CREDIT);
        transaction1.setTransferStatus("Transfer Status");
        transaction1.setUpdatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        transaction1.setUserStatus(UsersStatus.ACTIVE);
        transaction1.setWallet(wallet3);
        TransferRepository transferRepository = mock(TransferRepository.class);
        when(transferRepository.save((Transaction) any())).thenReturn(transaction1);
        when(transferRepository.findTransfersByFlwRef((Long) any())).thenReturn(transaction);
        UsersRepository usersRepository = mock(UsersRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        OtherBanksTransactionImpl otherBanksTransfer = new OtherBanksTransactionImpl(usersRepository, walletRepository,
                transferRepository, new BCryptPasswordEncoder());

        UsersRepository usersRepository1 = mock(UsersRepository.class);
        WalletRepository walletRepository1 = mock(WalletRepository.class);
        TransferRepository transferRepository1 = mock(TransferRepository.class);
        OtherBanksTransferController otherBanksTransferController = new OtherBanksTransferController(
                new OtherBanksTransactionImpl(usersRepository1, walletRepository1, transferRepository1,
                        new BCryptPasswordEncoder()),
                otherBanksTransfer);

        VerifyTransferRequest.Card card = new VerifyTransferRequest.Card();
        card.setAdditionalProperties(new HashMap<>());
        card.setCountry("GB");
        card.setExpiry("Expiry");
        card.setFirst6digits("First6digits");
        card.setIssuer("Issuer");
        card.setLast4digits("Last4digits");
        card.setType("Type");

        VerifyTransferRequest.Customer customer = new VerifyTransferRequest.Customer();
        customer.setAdditionalProperties(new HashMap<>());
        customer.setCreatedAt("Jan 1, 2020 8:00am GMT+0100");
        customer.setEmail("jane.doe@example.org");
        customer.setId(1);
        customer.setName("Name");
        customer.setPhoneNumber("Phone Number");

        VerifyTransferRequest.Data data = new VerifyTransferRequest.Data();
        data.setAccountId(3);
        data.setAdditionalProperties(new HashMap<>());
        data.setAmount(10);
        data.setAppFee(10.0d);
        data.setAuthModel("Auth Model");
        data.setCard(card);
        data.setChargedAmount(1);
        data.setCreatedAt("Jan 1, 2020 8:00am GMT+0100");
        data.setCurrency("GBP");
        data.setCustomer(customer);
        data.setDeviceFingerprint("b6:03:0e:39:97:9e:d0:e7:24:ce:a3:77:3e:01:42:09");
        data.setFlwRef("Flw Ref");
        data.setId(1);
        data.setIp("127.0.0.1");
        data.setMerchantFee(1);
        data.setNarration("Narration");
        data.setPaymentType("Payment Type");
        data.setProcessorResponse("Processor Response");
        data.setStatus("Status");
        data.setTxRef("Tx Ref");

        VerifyTransferRequest verifyTransferRequest = new VerifyTransferRequest();
        verifyTransferRequest.setAdditionalProperties(new HashMap<>());
        verifyTransferRequest.setData(data);
        verifyTransferRequest.setEvent("Event");
        ResponseEntity<String> actualVerifyResult = otherBanksTransferController.verify(verifyTransferRequest);
        assertEquals("status updated", actualVerifyResult.getBody());
        assertEquals(HttpStatus.OK, actualVerifyResult.getStatusCode());
        assertTrue(actualVerifyResult.getHeaders().isEmpty());
        verify(transferRepository).findTransfersByFlwRef((Long) any());
        verify(transferRepository).save((Transaction) any());
    }
}

