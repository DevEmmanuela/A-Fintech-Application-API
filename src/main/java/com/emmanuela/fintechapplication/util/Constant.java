package com.emmanuela.fintechapplication.util;

import org.springframework.beans.factory.annotation.Value;

public class Constant {

    public static final String CREATE_VIRTUAL_ACCOUNT_API = "https://api.flutterwave.com/v3/virtual-account-numbers";
    @Value("${FLW_SECRET_KEY}")
    public static String AUTHORIZATION;
    public static final String EMAIL_VERIFICATION_LINK = "https://fintech-application.herokuapp.com/confirmToken?token=";
    public static final String GET_BANKS_API = "https://api.flutterwave.com/v3/banks/";
    public static final String RESOLVE_ACCOUNT_API = "https://api.flutterwave.com/v3/accounts/resolve";
    public static final String OTHER_BANK_TRANSFER = "https://api.flutterwave.com/v3/transfers";
    public static final String STATUS = "PENDING";
    public static final String KEYS="secret";

    public static final String VERIFY_TRANSFER = "https://fintech-application.herokuapp.com/transfer/verify-transfer";
    public static final String VERIFY_TRANSACTION = "https://api.flutterwave.com/v3/transactions/:";

}
