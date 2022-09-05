package com.emmanuela.fintechapplication.service;

import com.emmanuela.fintechapplication.dto.SendMailDto;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    String sendMail(SendMailDto sendMailDto) throws MailException;
}

