package com.emmanuela.fintechapplication.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class FundAccountRequest {

    @JsonProperty("event")
    private String event;
    @JsonProperty("data")
    private Data data;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @lombok.Data
    public static class Card{
        @JsonProperty("first_6digits")
        private String first_6digits;
        @JsonProperty("last_4digits")
        private String last_4digits;
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
    public static class Customer{
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("phone_number")
        private Object phone_number;
        @JsonProperty("email")
        private String email;
        @JsonProperty("created_at")
        private String created_at;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    }

    @lombok.Data
    public static class Data{
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("tx_ref")
        private String tx_ref;
        @JsonProperty("flw_ref")
        private String flw_ref;
        @JsonProperty("device_fingerprint")
        private String device_fingerprint;
        @JsonProperty("amount")
        private BigDecimal amount;
        @JsonProperty("currency")
        private String currency;
        @JsonProperty("charged_amount")
        private int charged_amount;
        @JsonProperty("app_fee")
        private Double app_fee;
        @JsonProperty("merchant_fee")
        private int merchant_fee;
        @JsonProperty("processor_response")
        private String processor_response;
        @JsonProperty("auth_model")
        private String auth_model;
        @JsonProperty("ip")
        private String ip;
        @JsonProperty("narration")
        private String narration;
        @JsonProperty("status")
        private String status;
        @JsonProperty("payment_type")
        private String payment_type;
        @JsonProperty("created_at")
        private String created_at;
        @JsonProperty("account_id")
        private int account_id;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    }


}
