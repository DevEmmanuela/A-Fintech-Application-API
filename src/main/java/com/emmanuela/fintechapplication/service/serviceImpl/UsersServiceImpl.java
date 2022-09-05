package com.emmanuela.fintechapplication.service.serviceImpl;

import com.emmanuela.fintechapplication.customExceptions.EmailTakenException;
import com.emmanuela.fintechapplication.customExceptions.PasswordNotMatchingException;
import com.emmanuela.fintechapplication.customExceptions.UserNotFoundException;
import com.emmanuela.fintechapplication.dto.SendMailDto;
import com.emmanuela.fintechapplication.dto.UsersDTO;
import com.emmanuela.fintechapplication.dto.UsersResponse;
import com.emmanuela.fintechapplication.entities.Users;
import com.emmanuela.fintechapplication.entities.Wallet;
import com.emmanuela.fintechapplication.enums.UsersStatus;
import com.emmanuela.fintechapplication.repository.ConfirmationTokenRepository;
import com.emmanuela.fintechapplication.repository.TransactionRepository;
import com.emmanuela.fintechapplication.repository.UsersRepository;
import com.emmanuela.fintechapplication.repository.WalletRepository;
import com.emmanuela.fintechapplication.request.EmailVerifyRequest;
import com.emmanuela.fintechapplication.request.PasswordRequest;
import com.emmanuela.fintechapplication.request.ResetPasswordRequest;
import com.emmanuela.fintechapplication.response.BaseResponse;
import com.emmanuela.fintechapplication.security.filter.JwtUtils;
import com.emmanuela.fintechapplication.service.UsersService;
import com.emmanuela.fintechapplication.service.WalletService;
import com.emmanuela.fintechapplication.validations.token.ConfirmationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;


@Service @Transactional
@RequiredArgsConstructor @Slf4j
public class UsersServiceImpl implements UsersService, UserDetailsService {

    private String USER_EMAIL_ALREADY_EXISTS_MSG = "Users with email %s already exists!";
    private final ConfirmationTokenServiceImpl confirmTokenService;
    private final WalletService walletService;

    private final JwtUtils jwtUtils;
    private final WalletServiceImpl walletServices;
    private final WalletRepository walletRepository;
    private final MailServiceImpl mailService;
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final TransactionRepository transactionRepository;


    @Override
    public String registerUser(UsersDTO usersDTO) throws JSONException {

        if (!usersDTO.getPassword().equals(usersDTO.getConfirmPassword())) {
            throw new PasswordNotMatchingException("Passwords do not match!");
        }

        boolean userExists = usersRepository.findByEmail(usersDTO.getEmail()).isPresent();

        if (userExists) {
            throw new EmailTakenException(
                    String.format(USER_EMAIL_ALREADY_EXISTS_MSG, usersDTO.getEmail()));
        }

        Users user = new Users();
        user.setFirstName(usersDTO.getFirstName());
        user.setLastName(usersDTO.getLastName());
        user.setEmail(usersDTO.getEmail());
        user.setPhoneNumber(usersDTO.getPhoneNumber());
        user.setBVN(usersDTO.getBVN());
        user.setPassword(bCryptPasswordEncoder.encode(usersDTO.getPassword()));
        user.setPin(bCryptPasswordEncoder.encode(usersDTO.getPin())); // limit pin to 4 digits
        user.setCreatedAt(LocalDateTime.now());
        user.setUsersStatus(UsersStatus.INACTIVE);
        user.setRole("USER");
        Users user1 = usersRepository.save(user);

        Wallet wallet = generateWallet(user1);
        wallet.setUsers(user1);
        walletRepository.save(wallet);

        String token = UUID.randomUUID().toString();
        saveToken(token, user);

        return token;
    }

    @Override
    public void saveToken(String token, Users user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(24),
                user
        );
        confirmTokenService.saveConfirmationToken(confirmationToken);
    }

    @Override
    public Wallet generateWallet(Users user) throws JSONException {
        return walletService.createWallet(user);
    }

    @Override
    public UsersResponse getUser() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user1 = usersRepository.findUsersByEmail(user.getUsername());
        UsersResponse usersResponse = UsersResponse.builder()
                .firstName(user1.getFirstName())
                .lastName(user1.getLastName())
                .email(user1.getEmail())
                .phoneNumber(user1.getPhoneNumber())
                .BVN(user1.getBVN())
                .build();
        return usersResponse;

    }

    @Override
    public void enableUser(String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() ->  new UserNotFoundException("Users not found."));
        user.setUsersStatus(UsersStatus.ACTIVE);
        usersRepository.save(user);
    }

    @Override
    public void deleteUnverifiedToken(ConfirmationToken token) {
        confirmationTokenRepository.delete(token);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = usersRepository.findUsersByEmail(email);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("USER");
        if(user == null){
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }else {
            log.info("User Found");

            return new User(user.getEmail(), user.getPassword(), Collections.singleton(authority));
        }
    }

//    public BaseResponse<Page<TransactionHistoryResponse>> getTransactionHistory(TransactionHistoryPages transactionHistoryPages) throws NullPointerException{
//        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        Users user = usersRepository.findUsersByEmail(userEmail);
//
//        Sort sort = Sort.by(transactionHistoryPages.getSortDirection(), transactionHistoryPages.getSortBy());
//        Pageable pageable = PageRequest.of(transactionHistoryPages.getPageNumber(), transactionHistoryPages.getPageSize(), sort);
//
//        Wallet wallet = walletRepository.findWalletByUsers(user);
//
//        String userAccountNumber = wallet.getAccountNumber();
//
//        Page<Transaction> transactions = transactionRepository
//                .findAllBySenderAccountNumberOrDestinationAccountNumber(userAccountNumber, userAccountNumber, pageable);
//
//        List<TransactionHistoryResponse> userHistory = new ArrayList<>();
//
//        for (Transaction transaction : transactions) {
//            TransactionHistoryResponse response = mapTransferToTransactionHistoryResponse(userAccountNumber, transaction);
//            userHistory.add(response);
//        }
//
//        PageImpl<TransactionHistoryResponse> transactionHistoryPage = new PageImpl<>(userHistory, pageable, transactions.getTotalElements());
//
//        return new BaseResponse<>(HttpStatus.OK, "Transaction History retrieved", transactionHistoryPage);
//    }
//
//    private TransactionHistoryResponse mapTransferToTransactionHistoryResponse(String userAccountNumber, Transaction transaction) {
//
//        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("E, dd-MMMM-yyyy HH:mm");
//        boolean isSender = userAccountNumber.equals(transaction.getSenderAccountNumber());
//        String amount = String.format("\u20a6%,.2f",transaction.getAmount());
//        return TransactionHistoryResponse.builder()
//                .id(transaction.getId())
//                .name(isSender ? transaction.getDestinationFullName() : transaction.getSenderFullName())
//                .bank(isSender ? transaction.getDestinationBank() : transaction.getSenderBankName())
//                .type(isSender ? TransactionType.DEBIT : TransactionType.CREDIT)
//                .transactionTime(dateFormat.format(transaction.getCreatedAt()))
//                .amount(isSender ? "- " + amount : "+ " + amount)
//                .build();
//    }

    @Override
    public BaseResponse<String> generateResetToken(EmailVerifyRequest emailVerifyRequest) throws MessagingException {
        String email = emailVerifyRequest.getEmail();
        Users user = usersRepository.findUsersByEmail(email);
        if (user == null) {
            return new BaseResponse<>(HttpStatus.NOT_FOUND, "User with email not found", null);
        }


        String token = jwtUtils.generatePasswordResetToken(user.getEmail());
//        String url = "//http://localhost:8085/api/v1/resetPassword?token=" + token;

        sendPasswordResetEmail(user, token);
        return new BaseResponse<>(HttpStatus.OK,"Check Your Email to Reset Your Password", null);
    }
    private void sendPasswordResetEmail(Users user, String url) {
        String subject = "Reset your password";
        String senderName = "Fintech App";
        String mailContent = user.getLastName() + "\n"+ " Please click on the link below to reset your password \n";
        mailContent += "http://localhost:3000/reset-Password?token="+url;
        SendMailDto sendMailDto = new SendMailDto(user.getEmail(), senderName, subject, mailContent);
        mailService.sendMail(sendMailDto);
    }

    @Override
    public BaseResponse<String> resetPassword(ResetPasswordRequest resetPasswordRequest, String token) {
        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST, "Passwords don't match.", null);
        }
        String email = jwtUtils.extractUsername(token);

        Users user = usersRepository.findUsersByEmail(email);

        if (user == null) {
            return new BaseResponse<>(HttpStatus.NOT_FOUND, "User with email " + email + " not found", null);
        }

        user.setPassword(bCryptPasswordEncoder.encode(resetPasswordRequest.getNewPassword()));
        usersRepository.save(user);
        return new BaseResponse<>(HttpStatus.OK,"Password Reset Successfully",null);

    }

    @Override
    public BaseResponse<String> changePassword(PasswordRequest passwordRequest) {
        if(!passwordRequest.getNewPassword().equals(passwordRequest.getConfirmPassword())){
            return new BaseResponse<>(HttpStatus.BAD_REQUEST, "new password must match with confirm password", null);
        }
        String loggedInUsername =  SecurityContextHolder.getContext().getAuthentication().getName();

        Users user = usersRepository.findUsersByEmail(loggedInUsername);

        if (user == null) {
            return new BaseResponse<>(HttpStatus.UNAUTHORIZED, "User not logged In", null);
        }

        boolean matchPasswordWithOldPassword = bCryptPasswordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword());

        if(!matchPasswordWithOldPassword){
            return new BaseResponse<>(HttpStatus.BAD_REQUEST, "old password is not correct", null);
        }


        user.setPassword(bCryptPasswordEncoder.encode(passwordRequest.getNewPassword()));

        usersRepository.save(user);
        return new BaseResponse<>(HttpStatus.OK, "password changed successfully", null);
    }

    @Override
    public BaseResponse<String> getUserName() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepository.findUsersByEmail(userName);
        String fullName = "Hi, " + user.getFirstName();
        return new BaseResponse<>(HttpStatus.OK, "fetch successful",fullName);
    }


}
