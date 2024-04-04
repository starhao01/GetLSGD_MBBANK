package com.viettelpost.core.controller.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class UserRequest implements Serializable {
    String username;
    String password;
    String appCode;
}
