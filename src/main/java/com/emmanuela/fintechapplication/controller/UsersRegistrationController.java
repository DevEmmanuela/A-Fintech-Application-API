package com.emmanuela.fintechapplication.controller;

import com.emmanuela.fintechapplication.dto.UsersDTO;
import com.emmanuela.fintechapplication.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UsersRegistrationController {
        public final RegistrationService registrationService;

        @PostMapping("/register")
        public String register(@Valid @RequestBody UsersDTO usersDTO) throws JSONException {
            return registrationService.register(usersDTO);
        }

        @GetMapping("/confirmToken")
        public String confirmToken(@RequestParam("token") String token) throws JSONException {
            return registrationService.confirmToken(token);
        }
}
