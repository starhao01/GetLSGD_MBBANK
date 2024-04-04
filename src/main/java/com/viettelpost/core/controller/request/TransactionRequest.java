package com.viettelpost.core.controller.request;

import lombok.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionRequest {
    String user;
    String accountNo;
    String sessionId;
    String deviceId;
    public String toJsonString() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
