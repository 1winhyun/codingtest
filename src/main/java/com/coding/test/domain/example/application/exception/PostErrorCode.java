package com.coding.test.domain.example.application.exception;

import com.coding.test.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements ErrorCodeInterface {

    POST_TITLE_BLANK(40201, HttpStatus.BAD_REQUEST, "게시글 제목은 비어 있을 수 없습니다.");

    private final int code;
    private final HttpStatus status;
    private final String message;
}
