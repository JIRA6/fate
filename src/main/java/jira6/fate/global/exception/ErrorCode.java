package jira6.fate.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	FAIL(500, "실패했습니다."),
	NOT_UNAUTHORIZED(401, "권한이 없습니다."),
	UNAUTHORIZED_MANAGER(403, "매니저가 아닙니다."),
	INVALID_REQUEST(400, "입력값을 확인해주세요."),
	INCORRECT_PASSWORD(400, "입력하신 비밀번호가 일치하지 않습니다."),
	INCORRECT_MANAGER_KEY(400, "입력하신 MANAGER키가 일치하지 않습니다."),
	USER_NOT_FOUND(400, "해당하는 유저를 찾을 수 없습니다."),
	USER_NOT_UNIQUE(409,"사용 중인 아이디입니다."),
	USER_NOT_TEAM(404, "해당 유저가 팀에 없습니다."),
	BOARD_NOT_FOUND(404, "해당 보드를 찾을 수 없습니다."),
	COLUMN_NOT_FOUND(404, "해당 컬럼을 찾을 수 없습니다."),
	CARD_NOT_FOUND(404, "카드를 찾을 수 없습니다."),
	TOKEN_EXPIRED(400, "토큰이 만료되었습니다."),
	TOKEN_NOT_FOUND(400, "토큰을 찾을 수 없습니다."),
	INVALID_SIGNATURE(400, "유효하지 않은 JWT 서명입니다."),
	UNSUPPORTED_TOKEN(400, "지원되지 않는 JWT 토큰입니다."),
	ILLEGAL_TOKEN(400, "잘못된 JWT 토큰입니다."),
	RE_LOGIN_REQUIRED(401, "재로그인 해주세요");

	private final Integer status;
	private final String message;
}