package com.emmanuela.fintechapplication.serviceImpl;

import com.emmanuela.fintechapplication.dto.LoginRequestPayload;
import com.emmanuela.fintechapplication.entities.Users;
import com.emmanuela.fintechapplication.entities.Wallet;
import com.emmanuela.fintechapplication.enums.UsersStatus;
import com.emmanuela.fintechapplication.repository.UsersRepository;
import com.emmanuela.fintechapplication.security.filter.JwtUtils;
import com.emmanuela.fintechapplication.service.serviceImpl.LoginServiceImpl;
import com.emmanuela.fintechapplication.service.serviceImpl.UsersServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {LoginServiceImpl.class})
@ExtendWith(SpringExtension.class)
class LoginServiceImplTest {
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private LoginServiceImpl loginServiceImpl;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private UsersServiceImpl usersServiceImpl;


    @Test
    void testLogin() throws AuthenticationException, Exception {
        Wallet wallet = new Wallet();
        wallet.setAccountNumber("00912347464");
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
        users.setBVN("12345678901");
        users.setCreatedAt(LocalDateTime.of(2022, 07, 2, 8, 43));
        users.setEmail("jane.doe@example.org");
        users.setFirstName("Jane");
        users.setId(123L);
        users.setLastName("Doe");
        users.setPassword("12345");
        users.setPhoneNumber("0704563536");
        users.setPin("1234");
        users.setRole("USER");
        users.setUpdatedAt(LocalDateTime.of(2022, 07, 2, 8, 43));
        users.setUsersStatus(UsersStatus.ACTIVE);
        users.setWallet(wallet);

        Wallet wallet1 = new Wallet();
        wallet1.setAccountNumber("4264784674");
        wallet1.setBalance(BigDecimal.valueOf(42L));
        wallet1.setBankName("Wema Name");
        wallet1.setCreateAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet1.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet1.setId(123L);
        wallet1.setModifyAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet1.setTransactions(new ArrayList<>());
        wallet1.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet1.setUsers(users);

        Users users1 = new Users();
        users1.setBVN("1664664657");
        users1.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users1.setEmail("jane.doe@example.org");
        users1.setFirstName("Jane");
        users1.setId(123L);
        users1.setLastName("Doe");
        users1.setPassword("iloveyou");
        users1.setPhoneNumber("0707464t66");
        users1.setPin("1234");
        users1.setRole("USEr");
        users1.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users1.setUsersStatus(UsersStatus.ACTIVE);
        users1.setWallet(wallet1);
        Optional<Users> ofResult = Optional.of(users1);
        when(usersRepository.findByEmail((String) any())).thenReturn(ofResult);
        when(usersServiceImpl.loadUserByUsername((String) any()))
                .thenReturn(new User("janedoe", "iloveyou", new ArrayList<>()));
        when(jwtUtils.generateToken((UserDetails) any())).thenReturn("ABC123");
        when(authenticationManager.authenticate((Authentication) any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        assertEquals("ABC123", loginServiceImpl.login(new LoginRequestPayload("jane.doe@example.org", "iloveyou")));
        verify(usersRepository).findByEmail((String) any());
        verify(usersServiceImpl).loadUserByUsername((String) any());
        verify(jwtUtils).generateToken((UserDetails) any());
        verify(authenticationManager).authenticate((Authentication) any());
    }


}

