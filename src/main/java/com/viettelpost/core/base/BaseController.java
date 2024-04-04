package com.viettelpost.core.base;

import com.viettelpost.core.services.domains.UserInfo;
import com.viettelpost.core.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;

public class BaseController extends Utils {

    protected UserInfo getCurrentUser() throws Exception {
        UserInfo info = null;
        try {
            info = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
        }
        if (info == null || ((HashMap)info.getInfos().get("result")).get("userid") == null || !info.verify()) {
            throw new VtException(401, "Tài khoản đã được đăng nhập ở một nơi khác");
        }
        return info;
    }

    protected ResponseEntity<BaseResponse> successApi(Object data, String message) {
        return ResponseEntity.ok(new BaseResponse(false, message, data));
    }

    protected ResponseEntity<BaseResponse> customOutput(Boolean error, Object data, String message) {
        return ResponseEntity.ok(new BaseResponse(error, message, data));
    }

    protected ResponseEntity<BaseListResponse> customList(int total, int page, Object data) {
        return ResponseEntity.ok(new BaseListResponse(total,page, data));
    }
    protected ResponseEntity<BaseListResponse2> customList2(Boolean error, int total, int page, Object data) {
        return ResponseEntity.ok(new BaseListResponse2(error,total,page, data));
    }

    protected ResponseEntity<BaseResponse> errorApi(String message) {
        return ResponseEntity.ok(new BaseResponse(true, message, null));
    }

    protected ResponseEntity<BaseResponse> errorWithDataApi(Object data, String message) {
        return ResponseEntity.ok(new BaseResponse(true, message, data));
    }

    public ResponseEntity resultOk(String message, Object body) {
        return new ResponseEntity(new BaseResponse(false, Utils.isNullOrEmpty(message) ? "OK" : message, body), HttpStatus.OK);
    }

}
