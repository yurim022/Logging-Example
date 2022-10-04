package com.example.logging.api.service;

import com.example.logging.api.domain.Login;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    public String requestLogin(Login login) {
        return "Success Login";
    }
}
