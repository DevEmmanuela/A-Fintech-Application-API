package com.emmanuela.fintechapplication.service;

import com.emmanuela.fintechapplication.pagination_criteria.TransactionHistoryPages;
import com.emmanuela.fintechapplication.response.BaseResponse;
import com.emmanuela.fintechapplication.response.TransactionHistoryResponse;
import org.springframework.data.domain.Page;

public interface TransactionHistoryService {
    BaseResponse<Page<TransactionHistoryResponse>>
    getTransactionHistory(TransactionHistoryPages transactionHistoryPages);
}
