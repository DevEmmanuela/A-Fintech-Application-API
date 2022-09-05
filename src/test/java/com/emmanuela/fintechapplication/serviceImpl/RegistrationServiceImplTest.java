package com.emmanuela.fintechapplication.serviceImpl;

import com.emmanuela.fintechapplication.customExceptions.EmailAlreadyConfirmedException;
import com.emmanuela.fintechapplication.dto.SendMailDto;
import com.emmanuela.fintechapplication.dto.UsersDTO;
import com.emmanuela.fintechapplication.entities.Users;
import com.emmanuela.fintechapplication.entities.Wallet;
import com.emmanuela.fintechapplication.enums.UsersStatus;
import com.emmanuela.fintechapplication.repository.UsersRepository;
import com.emmanuela.fintechapplication.service.ConfirmationTokenService;
import com.emmanuela.fintechapplication.service.MailService;
import com.emmanuela.fintechapplication.service.UsersService;
import com.emmanuela.fintechapplication.service.serviceImpl.RegistrationServiceImpl;
import com.emmanuela.fintechapplication.validations.token.ConfirmationToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {RegistrationServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RegistrationServiceImplTest {
    @MockBean
    private ConfirmationTokenService confirmationTokenService;

    @MockBean
    private MailService mailService;

    @Autowired
    private RegistrationServiceImpl registrationServiceImpl;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private UsersService usersService;


    @Test
    void testRegister() throws JSONException, MailException {
        when(usersService.registerUser((UsersDTO) any())).thenReturn("Register User");
        when(mailService.sendMail((SendMailDto) any())).thenReturn("Send Mail");
        assertEquals("Please check your email for account activation link.",
                registrationServiceImpl.register(new UsersDTO(
                        "sulaiman", "lawal", "12345678901","lawalmonsaw@gmail.com", "08162210489",
                        "12345", "12345", "1234")));
        verify(usersService).registerUser((UsersDTO) any());
        verify(mailService).sendMail((SendMailDto) any());
    }


    @Test
    void testSendMailVerificationLink() throws MailException {
        when(mailService.sendMail((SendMailDto) any())).thenReturn("Send Mail");
        registrationServiceImpl.sendMailVerificationLink("Name", "jane.doe@example.org", "Link");
        verify(mailService).sendMail((SendMailDto) any());
    }


    @Test
    void testResendVerificationEmail() throws JSONException, MailException {
        doNothing().when(usersService).enableUser((String) any());
        doNothing().when(usersService).saveToken((String) any(), (Users) any());
        when(mailService.sendMail((SendMailDto) any())).thenReturn("Send Mail");

        Wallet wallet = new Wallet();
        wallet.setAccountNumber("42");
        wallet.setBalance(null);
        wallet.setBankName("Wema Name");
        wallet.setCreateAt(null);
        wallet.setCreatedAt(null);
        wallet.setId(123L);
        wallet.setModifyAt(null);
        wallet.setTransactions(new ArrayList<>());
        wallet.setUpdatedAt(null);
        wallet.setUsers(new Users());

        Users users = new Users();
        users.setBVN("1253635698");
        users.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users.setEmail("jane.doe@example.org");
        users.setFirstName("Jane");
        users.setId(123L);
        users.setLastName("Doe");
        users.setPassword("iloveyou");
        users.setPhoneNumber("0998467657");
        users.setPin("1234");
        users.setRole("USER");
        users.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users.setUsersStatus(UsersStatus.ACTIVE);
        users.setWallet(wallet);

        registrationServiceImpl.resendVerificationEmail(users);
        verify(usersService).saveToken((String) any(), (Users) any());
        verify(mailService).sendMail((SendMailDto) any());
    }


    @Test
    void testConfirmToken() throws JSONException {
        Users users = new Users();
        users.setBVN("3425425363");
        users.setCreatedAt(null);
        users.setEmail("jane.doe@example.org");
        users.setFirstName("Jane");
        users.setId(123L);
        users.setLastName("Doe");
        users.setPassword("iloveyou");
        users.setPhoneNumber("0803564254");
        users.setPin("1234");
        users.setRole("USER");
        users.setUpdatedAt(null);
        users.setUsersStatus(UsersStatus.ACTIVE);
        users.setWallet(new Wallet());

        Wallet wallet = new Wallet();
        wallet.setAccountNumber("4245363766");
        wallet.setBalance(BigDecimal.valueOf(42L));
        wallet.setBankName("Wema Name");
        wallet.setCreateAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet.setId(123L);
        wallet.setModifyAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet.setTransactions(new ArrayList<>());
        wallet.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet.setUsers(users);

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setConfirmedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        confirmationToken.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        confirmationToken.setExpiresAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        confirmationToken.setId(123L);
        confirmationToken.setToken("1234");
        confirmationToken.setUser(users);
        Optional<ConfirmationToken> ofResult = Optional.of(confirmationToken);
        when(confirmationTokenService.getToken((String) any())).thenReturn(ofResult);
        assertThrows(EmailAlreadyConfirmedException.class, () -> registrationServiceImpl.confirmToken("ABC123"));
        verify(confirmationTokenService).getToken((String) any());
    }
}

