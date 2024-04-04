package com.viettelpost.core.controller.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginAuthRequest {
    public String password;
    public String refNo;
    public  String sessionId;
    public String userId;
    public String captcha;
    public String deviceIdCommon;
}
