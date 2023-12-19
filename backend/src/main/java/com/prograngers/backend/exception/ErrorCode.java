package com.prograngers.backend.exception;

public enum ErrorCode {
    INVALID_REQUEST_BODY,
    SOLUTION_NOT_FOUND,
    PROBLEM_NOT_FOUND,
    PROBLEM_LINK_NOT_FOUND,
    COMMENT_NOT_FOUND,
    REVIEW_NOT_FOUND,
    ALGORITHM_NOT_EXISTS,
    DATASTRUCTURE_NOT_EXISTS,
    LEVEL_NOT_EXISTS,
    SORT_TYPE_NOT_EXISTS,
    MEMBER_NOT_FOUND,
    INVALID_PASSWORD,
    EXPIRED_TOKEN,
    FAIL_TO_ENCODED,
    FAIL_TO_DECODED,
    NOT_FOUND_REFRESH_TOKEN,
    NOT_EXIST_REFRESH_TOKEN,
    LANGUAGE_NOT_EXISTS,
    INCORRECT_PASSWORD,
    NOT_EXIST_ACCESS_TOKEN,
    ALREADY_EXIST_MEMBER,
    ALREADY_EXIST_NICKNAME,
    INCORRECT_NAVER_CODE,
    INVALID_ACCESS_TOKEN,
    MISSING_ISSUER_TOKEN,
    NOT_PROGRANGERS_TOKEN,
    UNSUPPORTED_TOKEN,
    INCORRECTLY_CONSTRUCTED_TOKEN,
    FAILED_SIGNATURE_TOKEN,
    UNAUTHORIZED_MEMBER,
    TYPE_MISMATCH,
    BLANK_NICKNAME,
    COMMENT_ALREADY_DELETED,
    REVIEW_ALREADY_DELETED,
    ALREADY_EXISTS_LIKE,
    NOT_FOUND_LIKE,
    INVALID_CLAIM_TYPE,
    INVALID_NOTIFICATION_TYPE,
    SSE_DISCONNECTED,
    PRIVATE_SOLUTION,
    INVALID_PAGE_NUMBER,
    ALREADY_FOLLOWING,
    NOT_FOUND_FOLLOW,
    FAIL_TO_CONVERT,
    NOT_EXIST_OLD_PASSWORD,
    INVALID_PARENT,
    DIFFERENT_SOLUTION,
    INVALID_CODE_LINE_NUMBER,
    DIFFERENT_CODE_LINE_NUMBER,
    ALREADY_DELETED_MEMBER,
    PROHIBITION_NICKNAME,
    QUIT_MEMBER,
    PAGE_SIZE_UNDER_THAN_ONE;
}
