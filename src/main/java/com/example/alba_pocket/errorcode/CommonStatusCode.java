package com.example.alba_pocket.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonStatusCode implements StatusCode{
    OK("정상", HttpStatus.OK.value()),
    FILE_SAVE_FAIL("파일 저장에 실패하였습니다.", HttpStatus.BAD_REQUEST.value()),
    WRONG_IMAGE_FORMAT("지원하지 않는 파일 형식입니다.", HttpStatus.BAD_REQUEST.value()),
    POST_LIKE("좋아요", HttpStatus.OK.value()),
    POST_LIKE_CANCEL("좋아요취소", HttpStatus.OK.value()),
    DELETE_COMMENT("댓글 삭제 성공", HttpStatus.OK.value()),
    CREATE_POST("게시글 작성 성공", HttpStatus.OK.value()),
    UPDATE_POST("게시글 수정 성공", HttpStatus.OK.value()),
    DELETE_POST("게시글 삭제 성공", HttpStatus.OK.value()),
    INVALID_PARAMETER("Invalid parameter included", HttpStatus.BAD_REQUEST.value()),
    INTERNAL_SERVER_ERROR("Internal server error", HttpStatus.BAD_REQUEST.value()),
    NO_ARTICLE("게시글이 존재하지 않습니다", HttpStatus.BAD_REQUEST.value()),
    NO_COMMENT("댓글이 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_USER_UPDATE("작성자만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_USER_DELETE("작성자만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST.value());



    private final String Msg;
    private final int statusCode;

}

