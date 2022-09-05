package com.emmanuela.fintechapplication.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMailDto {
    private String to;
    private String name;
    private String subject;
    private String body;
}

