package com.viettelpost.core.controller.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result {
    public String message;
    public String responseCode;
    public Boolean ok;
}
