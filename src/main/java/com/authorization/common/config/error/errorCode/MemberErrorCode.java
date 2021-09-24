package com.authorization.common.config.error.errorCode;

public enum MemberErrorCode implements ErrorCode {

    NOT_VALID_PASSWORD_LENGTH("MEC0001"),
    ALREADY_JOIN_ID("MFC0002"),
    NOT_FOUND_MEMBER("MFC0003");

    private final String code;

    MemberErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return "error.member." + code;
    }
}
