package com.authorization.common.config.error.exception;

import com.authorization.common.config.error.model.CommonErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = CommonException.class) // 클래스 단위로도 사용 가능(필요한 컨트롤러 내에 선언)
    @ResponseBody
    public CommonErrorResponse handleBaseException(CommonException e) {
        System.out.println(e.getMessage());
        System.out.println(e.getErrorCode());
        /*
            여기서 원하는 형태로 response 하면 된다.
         */
        return CommonErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(value = Exception.class)
    public String handleException() {
        return "error";
    }

}
