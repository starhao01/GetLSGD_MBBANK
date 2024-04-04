package com.viettelpost.core.controller;

import com.viettelpost.core.base.BaseController;
import com.viettelpost.core.base.Utils;
import com.viettelpost.core.services.AppService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@CrossOrigin("*")
@RestController
@RequestMapping("/app")
public class AppController extends BaseController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String chucNang = "APP CONTROLLER";

    @Autowired
    AppService appService;

    @ApiOperation(value = "API Get All App By UserID")
    @GetMapping("/getAllOwnerAppByUserID")
    public ResponseEntity getAllOwnerAppByUserID(@RequestParam Long userid) {
        //List<AppInfo> result = appService.getAllAppInfoByUserID(userid);
        return successApi(null, "Thành công");
    }

}
