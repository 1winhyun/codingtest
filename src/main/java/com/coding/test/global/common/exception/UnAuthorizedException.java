package com.coding.test.global.common.exception;

public class UnAuthorizedException extends ApplicationException {
    public UnAuthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
