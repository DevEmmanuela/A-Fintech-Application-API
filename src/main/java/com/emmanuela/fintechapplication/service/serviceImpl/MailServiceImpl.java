package com.emmanuela.fintechapplication.service.serviceImpl;

import com.emmanuela.fintechapplication.customExceptions.FailedMailException;
import com.emmanuela.fintechapplication.dto.SendMailDto;
import com.emmanuela.fintechapplication.service.MailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;
    private final String FOOTER_TEMPLATE = "\n\n Regards\n Transact Team!";

    @Override
    public String sendMail(SendMailDto sendMailDto) throws MailException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(sendMailDto.getTo());
        simpleMailMessage.setSubject(sendMailDto.getSubject());
        simpleMailMessage.setText("Hi, " + sendMailDto.getName() + "\n\n" + sendMailDto.getBody() +
                FOOTER_TEMPLATE);
        try{

            javaMailSender.send(simpleMailMessage);
            return "Mail Sent Successfully ";
        } catch (MailException ex)

        {
            throw new FailedMailException("Mail could not send because : "+ex.getMessage());
        }

    }
}

