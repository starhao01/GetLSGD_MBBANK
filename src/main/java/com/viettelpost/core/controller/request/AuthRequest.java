package com.viettelpost.core.controller.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthRequest {
    String userId;
    String password;
    String deviceId;
    String captcha;
}
