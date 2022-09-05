package com.emmanuela.fintechapplication.repository;

import com.emmanuela.fintechapplication.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transaction, Long> {

    Transaction findTransfersByFlwRef(Long flwRef);
}
