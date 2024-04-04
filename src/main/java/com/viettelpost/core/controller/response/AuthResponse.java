package com.viettelpost.core.controller.response;

import com.viettelpost.core.controller.request.Result;
import lombok.*;

import java.util.LinkedHashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse {
    public String sessionId;
    public String refNo;
    public int code ;
    public String message ;
    public Result result;
    private CustObject cust;
    public Object menuManager;
    public Object webSecurityToken;

}
