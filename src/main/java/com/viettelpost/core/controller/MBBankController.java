package com.viettelpost.core.controller;

import com.viettelpost.core.base.BaseController;
import com.viettelpost.core.base.VtException;
import com.viettelpost.core.controller.request.LoginRequest;
import com.viettelpost.core.controller.request.MomoRequest;
import com.viettelpost.core.services.MBBankService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mbbank") // Define a base URL path for the controller
public class MBBankController {

    @Autowired
    private MBBankService mbbankService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String captcha = loginRequest.getCaptcha(); // Assuming the captcha is part of the LoginRequest object
        String response = mbbankService.login(loginRequest.getUser(), loginRequest.getPass(), captcha);
        return ResponseEntity.ok(response);
    }
}
