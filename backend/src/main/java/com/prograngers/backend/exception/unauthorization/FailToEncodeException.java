package com.prograngers.backend.exception.unauthorization;

import static com.prograngers.backend.exception.ErrorCodeBefore.FAIL_TO_ENCODED;

public class FailToEncodeException extends UnAuthorizationException {
    public FailToEncodeException() {
        super(FAIL_TO_ENCODED, "비밀번호를 암호화하는데 실패했습니다.");
    }
}
