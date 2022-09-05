package com.emmanuela.fintechapplication.service.serviceImpl;

import com.emmanuela.fintechapplication.dto.LoginRequestPayload;
import com.emmanuela.fintechapplication.entities.Users;
import com.emmanuela.fintechapplication.enums.UsersStatus;
import com.emmanuela.fintechapplication.repository.UsersRepository;
import com.emmanuela.fintechapplication.security.filter.JwtUtils;
import com.emmanuela.fintechapplication.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;




@Service @RequiredArgsConstructor @Slf4j
public class LoginServiceImpl implements LoginService {

    private final UsersRepository usersRepository;

    private final UsersServiceImpl usersService;

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public String login(LoginRequestPayload loginRequestPayload) {
        Users user = usersRepository.findByEmail(loginRequestPayload.getEmail()).
                orElseThrow(()-> new UsernameNotFoundException("User not found!"));

        if (user.getUsersStatus() != UsersStatus.ACTIVE){
            return "Please verify your account from your email";
        }

        try{
            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken
                    (loginRequestPayload.getEmail(), loginRequestPayload.getPassword())
            );
        }catch (BadCredentialsException ex){
            throw new UsernameNotFoundException("Invalid Credential");
        }
        final UserDetails userDetails = usersService.loadUserByUsername(loginRequestPayload.getEmail());
        return jwtUtils.generateToken(userDetails);

    }
}
