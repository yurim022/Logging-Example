# Logging-Example

* Custom 어노테이션 Audit 생성
* 감사로그를 작성할 AuditLogInterceptor 구현 및 WebMVCConfig 등록
* CommonsRequestLoggingFilter 빈 등록 및 로깅
* logback-spring.xml 에 asyncappender로 비동기 로깅처리

### 결과

```
2022-10-04 17:37:40.762 DEBUG [] [http-nio-8080-exec-2] o.s.w.f.CommonsRequestLoggingFilter : Before request [POST /auth/login, headers=[user-agent:"PostmanRuntime/7.29.2", accept:"*/*", postman-token:"69c5ea6e-0219-4e33-96cd-caee09268b5d", host:"localhost:8080", accept-encoding:"gzip, deflate, br", connection:"keep-alive", content-length:"81", Content-Type:"application/json;charset=UTF-8"]]
2022-10-04 17:37:40.768 INFO  [] [http-nio-8080-exec-2] c.e.l.interceptor.AuditLogInterceptor : [Request INFO] URL : POST /auth/login
2022-10-04 17:37:40.836 INFO  [] [http-nio-8080-exec-2] audit : {"timestamp":"2022-10-04 17:37:40.821","operation":"requestLogin","transactionId":"yurim-logging-example-6e3f5025-b637-4a3b-8899-6dde87432657","logType":"IN_MSG","url":"/auth/login","response":{"type":"SUCCESS","code":"200 OK","duration":"54"},"user":{"id":"yurimming","type":"user","ip":"0:0:0:0:0:0:0:1","agent":"PostmanRuntime/7.29.2"},"security":{"type":"AUTH","event":"PHONE","target":"01000000000","detail":"로그인 요청","personalInfoList":"mobile"}}
2022-10-04 17:37:40.837 DEBUG [] [http-nio-8080-exec-2] o.s.w.f.CommonsRequestLoggingFilter : REQUEST DATA : POST /auth/login, headers=[user-agent:"PostmanRuntime/7.29.2", accept:"*/*", postman-token:"69c5ea6e-0219-4e33-96cd-caee09268b5d", host:"localhost:8080", accept-encoding:"gzip, deflate, br", connection:"keep-alive", content-length:"81", Content-Type:"application/json;charset=UTF-8"], payload={
    "id":"yurimming",
    "password":"secret",
    "mobile":"01000000000"
}]

```
