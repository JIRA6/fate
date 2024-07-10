package jira6.fate.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	FAIL(500, "실패했습니다."),
	USER_NOT_FOUND(400, "해당하는 유저를 찾을 수 없습니다."),
	SOCIAL_USER_NOT_FOUND(400, "해당하는 소셜 유저 데이터를 가져오는데 실패했습니다."),
	DUPLICATE_PASSWORD(400, "이전 비밀번호와 동일한 비밀번호입니다."),
	USER_NOT_UNIQUE(400,"중복된 사용자가 존재합니다."),
	EMAIL_NOT_UNIQUE(400,"중복된 이메일이 존재합니다."),
	INCORRECT_PASSWORD(400, "입력하신 비밀번호가 일치하지 않습니다."),
	POST_NOT_FOUND(404, "게시물을 찾을 수 없습니다."),
	POST_NOT_USER(400, "해당 게시물의 작성자가 아닙니다."),
	POST_SAME_USER(400, "해당 게시물의 작성자입니다."),
	POST_LIKE_EXIST(400, "이미 게시물에 좋아요가 등록되어있습니다."),
	POST_LIKE_NOT_EXIST(400, "이미 게시물에 좋아요가 존재하지 않습니다."),
	COMMENT_NOT_FOUND(404,"댓글을 찾을 수 없습니다."),
	COMMENT_NOT_USER(400, "해당 댓글의 작성자가 아닙니다."),
	COMMENT_SAME_USER(400, "해당 댓글의 작성자입니다."),
	COMMENT_LIKE_EXIST(400, "이미 댓글에 좋아요가 등록되어있습니다."),
	COMMENT_LIKE_NOT_EXIST(400, "이미 댓글에 좋아요가 존재하지 않습니다."),
	TOKEN_EXPIRED(400, "토큰이 만료되었습니다."),
	TOKEN_NOT_FOUND(400, "토큰을 찾을 수 없습니다."),
	INVALID_SIGNATURE(400, "유효하지 않은 JWT 서명입니다."),
	UNSUPPORTED_TOKEN(400, "지원되지 않는 JWT 토큰입니다."),
	ILLEGAL_TOKEN(400, "잘못된 JWT 토큰입니다."),
	RELOGIN_REQUIRED(401, "재로그인 해주세요"),
	INVALID_REQUEST(400, "잘못된 요청입니다."),
	INCORRECT_ADMIN_KEY(400, "입력하신 ADMIN키가 일치하지 않습니다."),
	UNAUTHORIZED_ADMIN(403, "권한이 없습니다.");

	private final int status;
	private final String msg;
}