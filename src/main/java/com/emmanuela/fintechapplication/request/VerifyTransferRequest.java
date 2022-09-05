package com.emmanuela.fintechapplication.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@lombok.Data
public class VerifyTransferRequest {

    @JsonProperty("event")
    private String event;
    @JsonProperty("data")
    private Data data;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @lombok.Data
    public static class Card {

        @JsonProperty("first_6digits")
        private String first6digits;
        @JsonProperty("last_4digits")
        private String last4digits;
        @JsonProperty("issuer")
        private String issuer;
        @JsonProperty("country")
        private String country;
        @JsonProperty("type")
        private String type;
        @JsonProperty("expiry")
        private String expiry;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    }

    @lombok.Data
    public static class Data {

        @JsonProperty("id")
        private Integer id;
        @JsonProperty("tx_ref")
        private String txRef;
        @JsonProperty("flw_ref")
        private String flwRef;
        @JsonProperty("device_fingerprint")
        private String deviceFingerprint;
        @JsonProperty("amount")
        private Integer amount;
        @JsonProperty("currency")
        private String currency;
        @JsonProperty("charged_amount")
        private Integer chargedAmount;
        @JsonProperty("app_fee")
        private Double appFee;
        @JsonProperty("merchant_fee")
        private Integer merchantFee;
        @JsonProperty("processor_response")
        private String processorResponse;
        @JsonProperty("auth_model")
        private String authModel;
        @JsonProperty("ip")
        private String ip;
        @JsonProperty("narration")
        private String narration;
        @JsonProperty("status")
        private String status;
        @JsonProperty("payment_type")
        private String paymentType;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("account_id")
        private Integer accountId;
        @JsonProperty("customer")
        private Customer customer;
        @JsonProperty("card")
        private Card card;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();



    }

    @lombok.Data
    public static class Customer {

        @JsonProperty("id")
        private Integer id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("phone_number")
        private Object phoneNumber;
        @JsonProperty("email")
        private String email;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    }
}

