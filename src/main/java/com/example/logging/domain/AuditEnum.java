package com.example.logging.domain;

public class AuditEnum {

    public enum Type {
        EMPTY,
        ACCESS, //사용자 접속 로그
        AUTH //인증로그
    }

    public enum Event {
        EMPTY,
        LOGIN,
        LOGOUT,
        READ,
        UPDATE,
        EXPORT,
        CREATE,
        DELETE,
        EMAIL, //email 인증
        PHONE //휴대폰 인증
    }

}
