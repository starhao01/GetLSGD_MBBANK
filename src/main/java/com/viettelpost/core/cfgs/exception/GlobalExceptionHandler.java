package com.viettelpost.core.cfgs.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.viettelpost.core.base.BaseResponse;
import com.viettelpost.core.base.VtException;
import com.viettelpost.core.utils.LanguageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.profiles.active}")
    String active;

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<BaseResponse> handleException(Exception ex) {
        return handleMessageException(ex);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<BaseResponse> handleException(VtException ex) {
        return handleMessageException(ex);
    }

    private ResponseEntity<BaseResponse> handleMessageException(Exception ex) {
        if (active.equals("dev")) {
            logger.error(ex.getLocalizedMessage(), ex);
        }
        if (ex instanceof VtException) {
            return processPopulateException((VtException) ex);
        } else {
            HttpStatus status = HttpStatus.OK;
            BaseResponse errorDetails = new BaseResponse();
            errorDetails.setError(true);
            if (ex instanceof HttpRequestMethodNotSupportedException || ex instanceof MissingServletRequestParameterException) {
                status = HttpStatus.BAD_REQUEST;
                if (ex.getLocalizedMessage() != null && ex.getLocalizedMessage().contains("Required")) {
                    errorDetails.setMessage("Thiếu tham số");
                } else {
                    errorDetails.setMessage("Sai phương thức");
                }
            } else if (ex instanceof HttpMediaTypeNotSupportedException || ex instanceof HttpMessageNotReadableException || ex instanceof InvalidFormatException) {
                status = HttpStatus.BAD_REQUEST;
                errorDetails.setMessage("Sai định dạng dữ liệu");
            } else if (ex instanceof MethodArgumentNotValidException) {
                StringBuilder message = new StringBuilder();
                BindingResult bindingresult = ((MethodArgumentNotValidException) ex).getBindingResult();
                List<FieldError> fieldErrors = bindingresult.getFieldErrors();
                for (FieldError error : fieldErrors) {
                    message.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(";");
                }
                errorDetails.setMessage(message.toString());
            } else {
                logger.error(ex.getLocalizedMessage(), ex);
                errorDetails.setMessage("Hệ thống đang bận vui lòng thử lại sau");
            }
            return new ResponseEntity<>(errorDetails, status);
        }
    }

    private ResponseEntity<BaseResponse> processPopulateException(VtException ex) {
        if (ex.getCode() == 401) {
            BaseResponse errorDetails = new BaseResponse();
            errorDetails.setError(true);
            errorDetails.setMessage("Tài khoản đã được đăng nhập ở một nơi khác");
            return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
        } else {
            BaseResponse errorDetails = new BaseResponse();
            errorDetails.setError(true);
            errorDetails.setMessage(ex.getLocalizedMessage());
            return new ResponseEntity<>(errorDetails, HttpStatus.OK);
        }
    }
}
