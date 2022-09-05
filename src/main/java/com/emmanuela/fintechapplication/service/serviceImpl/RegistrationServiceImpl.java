package com.emmanuela.fintechapplication.service.serviceImpl;

import com.emmanuela.fintechapplication.customExceptions.EmailAlreadyConfirmedException;
import com.emmanuela.fintechapplication.customExceptions.TokenNotFoundException;
import com.emmanuela.fintechapplication.customExceptions.UserNotFoundException;
import com.emmanuela.fintechapplication.dto.SendMailDto;
import com.emmanuela.fintechapplication.dto.UsersDTO;
import com.emmanuela.fintechapplication.entities.Users;
import com.emmanuela.fintechapplication.repository.UsersRepository;
import com.emmanuela.fintechapplication.service.ConfirmationTokenService;
import com.emmanuela.fintechapplication.service.MailService;
import com.emmanuela.fintechapplication.service.RegistrationService;
import com.emmanuela.fintechapplication.service.UsersService;
import com.emmanuela.fintechapplication.util.Constant;
import com.emmanuela.fintechapplication.validations.token.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UsersService usersService;
    private final MailService mailService;
    private final ConfirmationTokenService confirmationTokenService;
    private final UsersRepository usersRepository;

    @Override
    public String register(UsersDTO usersDTO) throws Exception {
        String token = usersService.registerUser(usersDTO);

        String link = Constant.EMAIL_VERIFICATION_LINK + token;
        sendMailVerificationLink(usersDTO.getFirstName(), usersDTO.getEmail(), link);

        return "Please check your email for account activation link.";
    }

    @Override
    public void sendMailVerificationLink(String name, String email, String link) {
        String subject = "Email Verification";
        String body = "Click the link below to verify your email \n" + link;
        SendMailDto sendMailDto = new SendMailDto(email, name, subject, body);
        mailService.sendMail(sendMailDto);
    }

    @Override
    public void resendVerificationEmail(Users user) throws Exception {
        String token = UUID.randomUUID().toString();
        String link = Constant.EMAIL_VERIFICATION_LINK + token;
        sendMailVerificationLink(user.getFirstName(), user.getEmail(), link);

        usersService.saveToken(token, user);
    }

    @Override
    public String confirmToken(String token) throws Exception {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Token not found."));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new EmailAlreadyConfirmedException("Email already confirmed.");
        }
        
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            Users user = usersRepository.findByEmail(confirmationToken.getUser().getEmail()).orElseThrow(
                    ()-> new UserNotFoundException("Users not found"));

            usersService.deleteUnverifiedToken(confirmationToken);

            resendVerificationEmail(user);
            return "Previous verification token expired. Check email for new token.";
        }

        confirmationTokenService.setConfirmedAt(token);
        usersService.enableUser(confirmationToken.getUser().getEmail());

        return "Email confirmed!";
    }
}
