package com.emmanuela.fintechapplication.repository;

import com.emmanuela.fintechapplication.entities.Users;
import com.emmanuela.fintechapplication.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findWalletByUsers(Users user);

    Wallet findWalletByAccountNumber(String accountNumber);
    Wallet findByTxRef(String txRef);
}
