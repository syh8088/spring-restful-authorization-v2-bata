package com.authorization.common.config.error.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonErrorResponse {

    private String errorCode;
    private String message;

    @Builder
    public CommonErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
