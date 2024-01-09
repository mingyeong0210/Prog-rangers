package com.prograngers.backend.exception.badrequest;

import static com.prograngers.backend.exception.ErrorCodeBefore.ALREADY_EXISTS_LIKE;

public class LikesAlreadyExistsException extends AlreadyExistsException {

    public LikesAlreadyExistsException() {
        super(ALREADY_EXISTS_LIKE, "이미 좋아요를 누른 풀이입니다");
    }
}
