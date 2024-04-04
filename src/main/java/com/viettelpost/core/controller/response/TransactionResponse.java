package com.viettelpost.core.controller.response;

import com.viettelpost.core.controller.request.Transaction;
import lombok.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionResponse {
    public int code;
    public String message;
    public List<Transaction> data;
    public String refNo;
    public Object result;
    public Object transactionHistoryList;
    public static TransactionResponse fromJson(InputStream jsonStream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonStream, TransactionResponse.class);
    }
}
