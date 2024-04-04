package com.viettelpost.core.controller.request;

import lombok.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LsgdRequest {
    public String toDate;
    public String accountNo;
    public String historyNumber;
    public String historyType;
    public String sessionId;
    public String fromDate;
    public String refNo;
    public String type;
    public String deviceIdCommon;

    public String toJsonString() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
