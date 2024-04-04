package com.viettelpost.core.controller;

import com.viettelpost.core.base.BaseController;
import com.viettelpost.core.base.VtException;
import com.viettelpost.core.controller.request.MomoRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class MomoController extends BaseController {

    private static final String SIGNATURE = "e37f64b10e0ed7de21c9a0fa1461ae373e8f2eed80a55f22f83d7bbcfc255a86";

    @ApiOperation(value = "API Get All App By UserID")
    @GetMapping("/getHistoryWebhook")
    public void handleMomoRequest(@RequestBody MomoRequest request) {
        if (SIGNATURE.equals(request.getSignature())) {
            String phone = request.getPhone();
            String tranId = request.getTranId();
            String ackTime = request.getAckTime();
            String partnerId = request.getPartnerId();
            String partnerName = request.getPartnerName();
            String amount = request.getAmount();
            String comment = request.getComment();

            // Update the order status or add money to the user's account based on the above information
            // Code to add money
            // End
        } else {

        }
    }
}
