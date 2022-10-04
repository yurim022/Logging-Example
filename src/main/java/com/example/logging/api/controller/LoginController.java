package com.example.logging.api.controller;

import com.example.logging.annotation.Audit;
import com.example.logging.api.domain.Login;
import com.example.logging.api.service.LoginService;
import com.example.logging.domain.AuditEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    @Audit(type = AuditEnum.Type.AUTH, event = AuditEnum.Event.PHONE,personalInfoList = "mobile")
    public String requestLogin(@RequestBody Login login, HttpServletRequest request){

        request.setAttribute(Audit.AUDIT_REQUEST_TARGET, login.getMobile());
        request.setAttribute(Audit.AUDIT_LOGIN_USER_ID,login.getId());
        request.setAttribute(Audit.AUDIT_REQUEST_DETAIL,"로그인 요청");

        return loginService.requestLogin(login);

    }
}
