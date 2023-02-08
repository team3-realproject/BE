package com.example.alba_pocket.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonStatusCode implements StatusCode{

    //문자열 체크
    NOT_VALIDCONTENT("유효하지 않은 내용입니다.",HttpStatus.BAD_REQUEST.value()),
    NOT_VALIDURL("유효하지 않은 URL입니다.",HttpStatus.BAD_REQUEST.value()),
    //sse
    NOT_EXIST_NOTIFICATION("존재하지 않는 알림입니다.",HttpStatus.NOT_FOUND.value()),

    OK("정상", HttpStatus.OK.value()),
    FILE_SAVE_FAIL("파일 저장에 실패하였습니다.", HttpStatus.BAD_REQUEST.value()),
    WRONG_IMAGE_FORMAT("지원하지 않는 파일 형식입니다.", HttpStatus.BAD_REQUEST.value()),
    POST_LIKE("게시글 좋아요", HttpStatus.OK.value()),
    POST_LIKE_CANCEL("게시글 좋아요취소", HttpStatus.OK.value()),
    COMMENT_LIKE("댓글 좋아요", HttpStatus.OK.value()),
    COMMENT_LIKE_CANCEL("댓글 좋아요취소", HttpStatus.OK.value()),
    DELETE_COMMENT("댓글 삭제 성공", HttpStatus.OK.value()),
    CREATE_POST("게시글 작성 성공", HttpStatus.OK.value()),
    UPDATE_POST("게시글 수정 성공", HttpStatus.OK.value()),
    DELETE_POST("게시글 삭제 성공", HttpStatus.OK.value()),
    INVALID_PARAMETER("Invalid parameter included", HttpStatus.BAD_REQUEST.value()),
    INTERNAL_SERVER_ERROR("Internal server error", HttpStatus.BAD_REQUEST.value()),
    NO_ARTICLE("게시글이 존재하지 않습니다", HttpStatus.BAD_REQUEST.value()),
    NO_COMMENT("댓글이 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value()),
    NO_WORKPLACE("근무지가 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_USER_UPDATE("작성자만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_USER_DELETE("작성자만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST.value()),
    OVER_CONTENT("게시물은 500자 이내로 작성해주세요.", HttpStatus.BAD_REQUEST.value()),
    OVER_TITLE("제목은 50자 이내로 작성해주세요.", HttpStatus.BAD_REQUEST.value()),
    OVER_COMMENT("댓글은 100자 이내로 작성해주세요", HttpStatus.BAD_REQUEST.value());


    private final String Msg;
    private final int statusCode;

}

