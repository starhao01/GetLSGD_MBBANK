package com.viettelpost.core.controller.request;

import lombok.Data;

@Data
public class MomoRequest {
    private String signature;
    private String phone;
    private String tranId;
    private String ackTime;
    private String partnerId;
    private String partnerName;
    private String amount;
    private String comment;
}
