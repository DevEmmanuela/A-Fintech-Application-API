package com.emmanuela.fintechapplication.serviceImpl;

import com.emmanuela.fintechapplication.customExceptions.EmailTakenException;
import com.emmanuela.fintechapplication.dto.UsersDTO;
import com.emmanuela.fintechapplication.entities.Users;
import com.emmanuela.fintechapplication.entities.Wallet;
import com.emmanuela.fintechapplication.enums.UsersStatus;
import com.emmanuela.fintechapplication.repository.UsersRepository;
import com.emmanuela.fintechapplication.repository.WalletRepository;
import com.emmanuela.fintechapplication.security.filter.JwtUtils;
import com.emmanuela.fintechapplication.service.WalletService;
import com.emmanuela.fintechapplication.service.serviceImpl.ConfirmationTokenServiceImpl;
import com.emmanuela.fintechapplication.service.serviceImpl.UsersServiceImpl;
import com.emmanuela.fintechapplication.service.serviceImpl.WalletServiceImpl;
import com.emmanuela.fintechapplication.validations.token.ConfirmationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UsersServiceImpl.class, BCryptPasswordEncoder.class, WalletService.class})
@ExtendWith(SpringExtension.class)
class UsersServiceImplTest {
    @MockBean
    private ConfirmationTokenServiceImpl confirmationTokenServiceImpl;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UsersRepository usersRepository;

    @Autowired
    private UsersServiceImpl usersServiceImpl;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private WalletService walletService;

    @MockBean
    private WalletServiceImpl walletServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() throws JSONException {
        doNothing().when(confirmationTokenServiceImpl).saveConfirmationToken((ConfirmationToken) any());

        Wallet wallet = new Wallet();
        wallet.setAccountNumber("422345566777");
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
        users.setBVN("20229087253");
        users.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users.setEmail("jane.doe@example.org");
        users.setFirstName("Jane");
        users.setId(123L);
        users.setLastName("Doe");
        users.setPassword("12345");
        users.setPhoneNumber("08162210489");
        users.setPin("1234");
        users.setRole("USER");
        users.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users.setUsersStatus(UsersStatus.ACTIVE);
        users.setWallet(wallet);

        Wallet wallet1 = new Wallet();
        wallet1.setAccountNumber("42");
        wallet1.setBalance(BigDecimal.valueOf(42L));
        wallet1.setBankName("Wema Name");
        wallet1.setCreateAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet1.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet1.setId(123L);
        wallet1.setModifyAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet1.setTransactions(new ArrayList<>());
        wallet1.setUpdatedAt(LocalDateTime.of(2022, 7, 12, 1, 2));
        wallet1.setUsers(users);

        Users users1 = new Users();
        users1.setBVN("208977759784");
        users1.setCreatedAt(LocalDateTime.of(2022, 7, 9, 1, 27));
        users1.setEmail("jane.doe@example.org");
        users1.setFirstName("Jane");
        users1.setId(123L);
        users1.setLastName("Doe");
        users1.setPassword("12345");
        users1.setPhoneNumber("08162210489");
        users1.setPin("1234");
        users1.setRole("USER");
        users1.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users1.setUsersStatus(UsersStatus.ACTIVE);
        users1.setWallet(wallet1);

        Wallet wallet2 = new Wallet();
        wallet2.setAccountNumber("42");
        wallet2.setBalance(BigDecimal.valueOf(42L));
        wallet2.setBankName("Wema Name");
        wallet2.setCreateAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet2.setCreatedAt(LocalDateTime.of(12022, 7, 8, 1, 2));
        wallet2.setId(123L);
        wallet2.setModifyAt(LocalDateTime.of(2022, 9, 8, 1, 2));
        wallet2.setTransactions(new ArrayList<>());
        wallet2.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet2.setUsers(users1);
        when(walletService.createWallet((Users) any())).thenReturn(wallet2);

        Wallet wallet3 = new Wallet();
        wallet3.setAccountNumber("42");
        wallet3.setBalance(null);
        wallet3.setBankName("Bank Name");
        wallet3.setCreateAt(null);
        wallet3.setCreatedAt(null);
        wallet3.setId(123L);
        wallet3.setModifyAt(null);
        wallet3.setTransactions(new ArrayList<>());
        wallet3.setUpdatedAt(null);
        wallet3.setUsers(new Users());

        Users users2 = new Users();
        users2.setBVN("45673764764");
        users2.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users2.setEmail("jane.doe@example.org");
        users2.setFirstName("Jane");
        users2.setId(123L);
        users2.setLastName("Doe");
        users2.setPassword("12345");
        users2.setPhoneNumber("0908475657");
        users2.setPin("1234");
        users2.setRole("USER");
        users2.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users2.setUsersStatus(UsersStatus.ACTIVE);
        users2.setWallet(wallet3);

        Wallet wallet4 = new Wallet();
        wallet4.setAccountNumber("4247955678");
        wallet4.setBalance(BigDecimal.valueOf(42L));
        wallet4.setBankName("Wema Name");
        wallet4.setCreateAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet4.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet4.setId(123L);
        wallet4.setModifyAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet4.setTransactions(new ArrayList<>());
        wallet4.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet4.setUsers(users2);

        Users users3 = new Users();
        users3.setBVN("273779974894");
        users3.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users3.setEmail("jane.doe@example.org");
        users3.setFirstName("Jane");
        users3.setId(123L);
        users3.setLastName("Doe");
        users3.setPassword("iloveyou");
        users3.setPhoneNumber("09084784774");
        users3.setPin("1234");
        users3.setRole("USER");
        users3.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users3.setUsersStatus(UsersStatus.ACTIVE);
        users3.setWallet(wallet4);

        Wallet wallet5 = new Wallet();
        wallet5.setAccountNumber("202212322");
        wallet5.setBalance(BigDecimal.valueOf(42L));
        wallet5.setBankName("Wema Name");
        wallet5.setCreateAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet5.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet5.setId(123L);
        wallet5.setModifyAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet5.setTransactions(new ArrayList<>());
        wallet5.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet5.setUsers(users3);
        when(walletRepository.save((Wallet) any())).thenReturn(wallet5);

        Users users4 = new Users();
        users4.setBVN("202287346");
        users4.setCreatedAt(null);
        users4.setEmail("jane.doe@example.org");
        users4.setFirstName("Jane");
        users4.setId(123L);
        users4.setLastName("Doe");
        users4.setPassword("iloveyou");
        users4.setPhoneNumber("0907353653");
        users4.setPin("1234");
        users4.setRole("USER");
        users4.setUpdatedAt(null);
        users4.setUsersStatus(UsersStatus.ACTIVE);
        users4.setWallet(new Wallet());

        Wallet wallet6 = new Wallet();
        wallet6.setAccountNumber("43633864448");
        wallet6.setBalance(BigDecimal.valueOf(42L));
        wallet6.setBankName("Wema Name");
        wallet6.setCreateAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet6.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet6.setId(123L);
        wallet6.setModifyAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet6.setTransactions(new ArrayList<>());
        wallet6.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet6.setUsers(users4);

        Users users5 = new Users();
        users5.setBVN("0918273736");
        users5.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users5.setEmail("jane.doe@example.org");
        users5.setFirstName("Jane");
        users5.setId(123L);
        users5.setLastName("Doe");
        users5.setPassword("12345");
        users5.setPhoneNumber("0908474663");
        users5.setPin("1234");
        users5.setRole("USER");
        users5.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users5.setUsersStatus(UsersStatus.ACTIVE);
        users5.setWallet(wallet6);

        Wallet wallet7 = new Wallet();
        wallet7.setAccountNumber("4256456465");
        wallet7.setBalance(BigDecimal.valueOf(42L));
        wallet7.setBankName("Wema Name");
        wallet7.setCreateAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet7.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet7.setId(123L);
        wallet7.setModifyAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet7.setTransactions(new ArrayList<>());
        wallet7.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet7.setUsers(users5);

        Users users6 = new Users();
        users6.setBVN("BVN");
        users6.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users6.setEmail("jane.doe@example.org");
        users6.setFirstName("Jane");
        users6.setId(123L);
        users6.setLastName("Doe");
        users6.setPassword("iloveyou");
        users6.setPhoneNumber("4105551212");
        users6.setPin("Pin");
        users6.setRole("Role");
        users6.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users6.setUsersStatus(UsersStatus.ACTIVE);
        users6.setWallet(wallet7);

        Wallet wallet8 = new Wallet();
        wallet8.setAccountNumber("42");
        wallet8.setBalance(null);
        wallet8.setBankName("Bank Name");
        wallet8.setCreateAt(null);
        wallet8.setCreatedAt(null);
        wallet8.setId(123L);
        wallet8.setModifyAt(null);
        wallet8.setTransactions(new ArrayList<>());
        wallet8.setUpdatedAt(null);
        wallet8.setUsers(new Users());

        Users users7 = new Users();
        users7.setBVN("BVN");
        users7.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users7.setEmail("jane.doe@example.org");
        users7.setFirstName("Jane");
        users7.setId(123L);
        users7.setLastName("Doe");
        users7.setPassword("iloveyou");
        users7.setPhoneNumber("4105551212");
        users7.setPin("Pin");
        users7.setRole("Role");
        users7.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users7.setUsersStatus(UsersStatus.ACTIVE);
        users7.setWallet(wallet8);

        Wallet wallet9 = new Wallet();
        wallet9.setAccountNumber("42");
        wallet9.setBalance(BigDecimal.valueOf(42L));
        wallet9.setBankName("Bank Name");
        wallet9.setCreateAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet9.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet9.setId(123L);
        wallet9.setModifyAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet9.setTransactions(new ArrayList<>());
        wallet9.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet9.setUsers(users7);

        Users users8 = new Users();
        users8.setBVN("BVN");
        users8.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users8.setEmail("jane.doe@example.org");
        users8.setFirstName("Jane");
        users8.setId(123L);
        users8.setLastName("Doe");
        users8.setPassword("iloveyou");
        users8.setPhoneNumber("4105551212");
        users8.setPin("Pin");
        users8.setRole("Role");
        users8.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users8.setUsersStatus(UsersStatus.ACTIVE);
        users8.setWallet(wallet9);
        Optional<Users> ofResult = Optional.of(users8);
        when(usersRepository.save((Users) any())).thenReturn(users6);
        when(usersRepository.findByEmail((String) any())).thenReturn(ofResult);
        assertThrows(EmailTakenException.class, () -> usersServiceImpl.registerUser(
                new UsersDTO("Jane", "Doe", "BVN", "jane.doe@example.org", "4105551212", "iloveyou", "iloveyou", "Pin")));
        verify(usersRepository).findByEmail((String) any());
    }


    @Test
    void testGenerateWallet() throws JSONException {
        Wallet wallet = new Wallet();
        wallet.setAccountNumber("42");
        wallet.setBalance(null);
        wallet.setBankName("Bank Name");
        wallet.setCreateAt(null);
        wallet.setCreatedAt(null);
        wallet.setId(123L);
        wallet.setModifyAt(null);
        wallet.setTransactions(new ArrayList<>());
        wallet.setUpdatedAt(null);
        wallet.setUsers(new Users());

        Users users = new Users();
        users.setBVN("BVN");
        users.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users.setEmail("jane.doe@example.org");
        users.setFirstName("Jane");
        users.setId(123L);
        users.setLastName("Doe");
        users.setPassword("iloveyou");
        users.setPhoneNumber("4105551212");
        users.setPin("Pin");
        users.setRole("Role");
        users.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users.setUsersStatus(UsersStatus.ACTIVE);
        users.setWallet(wallet);

        Wallet wallet1 = new Wallet();
        wallet1.setAccountNumber("42");
        wallet1.setBalance(BigDecimal.valueOf(42L));
        wallet1.setBankName("Bank Name");
        wallet1.setCreateAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet1.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet1.setId(123L);
        wallet1.setModifyAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet1.setTransactions(new ArrayList<>());
        wallet1.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet1.setUsers(users);

        Users users1 = new Users();
        users1.setBVN("BVN");
        users1.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users1.setEmail("jane.doe@example.org");
        users1.setFirstName("Jane");
        users1.setId(123L);
        users1.setLastName("Doe");
        users1.setPassword("iloveyou");
        users1.setPhoneNumber("4105551212");
        users1.setPin("Pin");
        users1.setRole("Role");
        users1.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users1.setUsersStatus(UsersStatus.ACTIVE);
        users1.setWallet(wallet1);

        Wallet wallet2 = new Wallet();
        wallet2.setAccountNumber("42");
        wallet2.setBalance(BigDecimal.valueOf(42L));
        wallet2.setBankName("Bank Name");
        wallet2.setCreateAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet2.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet2.setId(123L);
        wallet2.setModifyAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet2.setTransactions(new ArrayList<>());
        wallet2.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet2.setUsers(users1);
        when(walletService.createWallet((Users) any())).thenReturn(wallet2);

        Wallet wallet3 = new Wallet();
        wallet3.setAccountNumber("42");
        wallet3.setBalance(null);
        wallet3.setBankName("Bank Name");
        wallet3.setCreateAt(null);
        wallet3.setCreatedAt(null);
        wallet3.setId(123L);
        wallet3.setModifyAt(null);
        wallet3.setTransactions(new ArrayList<>());
        wallet3.setUpdatedAt(null);
        wallet3.setUsers(new Users());

        Users users2 = new Users();
        users2.setBVN("BVN");
        users2.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users2.setEmail("jane.doe@example.org");
        users2.setFirstName("Jane");
        users2.setId(123L);
        users2.setLastName("Doe");
        users2.setPassword("iloveyou");
        users2.setPhoneNumber("4105551212");
        users2.setPin("Pin");
        users2.setRole("Role");
        users2.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users2.setUsersStatus(UsersStatus.ACTIVE);
        users2.setWallet(wallet3);

        Wallet wallet4 = new Wallet();
        wallet4.setAccountNumber("42");
        wallet4.setBalance(BigDecimal.valueOf(42L));
        wallet4.setBankName("Bank Name");
        wallet4.setCreateAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet4.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet4.setId(123L);
        wallet4.setModifyAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet4.setTransactions(new ArrayList<>());
        wallet4.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet4.setUsers(users2);

        Users users3 = new Users();
        users3.setBVN("BVN");
        users3.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users3.setEmail("jane.doe@example.org");
        users3.setFirstName("Jane");
        users3.setId(123L);
        users3.setLastName("Doe");
        users3.setPassword("iloveyou");
        users3.setPhoneNumber("4105551212");
        users3.setPin("Pin");
        users3.setRole("Role");
        users3.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users3.setUsersStatus(UsersStatus.ACTIVE);
        users3.setWallet(wallet4);
        Wallet actualGenerateWalletResult = usersServiceImpl.generateWallet(users3);
        assertSame(wallet2, actualGenerateWalletResult);
        assertEquals("42", actualGenerateWalletResult.getBalance().toString());
        assertEquals("42", actualGenerateWalletResult.getUsers().getWallet().getBalance().toString());
        verify(walletService).createWallet((Users) any());
    }


    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        Users users = new Users();
        users.setBVN("2209098475");
        users.setCreatedAt(null);
        users.setEmail("jane.doe@example.org");
        users.setFirstName("Jane");
        users.setId(123L);
        users.setLastName("Doe");
        users.setPassword("12345");
        users.setPhoneNumber("4105551212");
        users.setPin("1234");
        users.setRole("USER");
        users.setUpdatedAt(null);
        users.setUsersStatus(UsersStatus.ACTIVE);
        users.setWallet(new Wallet());

        Wallet wallet = new Wallet();
        wallet.setAccountNumber("42");
        wallet.setBalance(BigDecimal.valueOf(42L));
        wallet.setBankName("Wema Name");
        wallet.setCreateAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet.setId(123L);
        wallet.setModifyAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet.setTransactions(new ArrayList<>());
        wallet.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet.setUsers(users);

        Users users1 = new Users();
        users1.setBVN("22028976454");
        users1.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users1.setEmail("jane.doe@example.org");
        users1.setFirstName("Jane");
        users1.setId(123L);
        users1.setLastName("Doe");
        users1.setPassword("12345");
        users1.setPhoneNumber("08162210489");
        users1.setPin("1234");
        users1.setRole("USER");
        users1.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        users1.setUsersStatus(UsersStatus.ACTIVE);
        users1.setWallet(wallet);

        Wallet wallet1 = new Wallet();
        wallet1.setAccountNumber("42");
        wallet1.setBalance(BigDecimal.valueOf(42L));
        wallet1.setBankName("Wema Name");
        wallet1.setCreateAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet1.setCreatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet1.setId(123L);
        wallet1.setModifyAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet1.setTransactions(new ArrayList<>());
        wallet1.setUpdatedAt(LocalDateTime.of(2022, 7, 8, 1, 2));
        wallet1.setUsers(users1);

        Users users2 = new Users();
        users2.setBVN("12345678901");
        users2.setCreatedAt(LocalDateTime.of(2022, 1, 2, 1, 6));
        users2.setEmail("jane.doe@example.org");
        users2.setFirstName("Jane");
        users2.setId(123L);
        users2.setLastName("Doe");
        users2.setPassword("12345");
        users2.setPhoneNumber("08162210489");
        users2.setPin("1234");
        users2.setRole("USER");
        users2.setUpdatedAt(LocalDateTime.of(2022, 1, 3, 1, 7));
        users2.setUsersStatus(UsersStatus.ACTIVE);
        users2.setWallet(wallet1);
        when(usersRepository.findUsersByEmail((String) any())).thenReturn(users2);
        UserDetails actualLoadUserByUsernameResult = usersServiceImpl.loadUserByUsername("jane.doe@example.org");
        assertEquals(1, actualLoadUserByUsernameResult.getAuthorities().size());
        assertTrue(actualLoadUserByUsernameResult.isEnabled());
        assertTrue(actualLoadUserByUsernameResult.isCredentialsNonExpired());
        assertTrue(actualLoadUserByUsernameResult.isAccountNonLocked());
        assertTrue(actualLoadUserByUsernameResult.isAccountNonExpired());
        assertEquals("jane.doe@example.org", actualLoadUserByUsernameResult.getUsername());
        assertEquals("12345", actualLoadUserByUsernameResult.getPassword());
        verify(usersRepository).findUsersByEmail((String) any());
    }

}


