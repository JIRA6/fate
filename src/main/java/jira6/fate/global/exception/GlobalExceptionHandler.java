package jira6.fate.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ExceptionResponse> defaultException(HttpServletRequest request, Exception e){
		e.printStackTrace();
		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.msg(ErrorCode.FAIL.getMsg())
			.path(request.getRequestURI())
			.build();
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ExceptionResponse> handleInvalidPasswordException(HttpServletRequest request, CustomException e) {
		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.msg(e.getErrorCode().getMsg())
			.path(request.getRequestURI())
			.build();
		return new ResponseEntity<>(exceptionResponse, HttpStatusCode.valueOf(e.getErrorCode().getStatus()));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ExceptionResponse> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.msg(ErrorCode.UNAUTHORIZED_ADMIN.getMsg())
			.path(request.getRequestURI())
			.build();
		return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> processValidationError(HttpServletRequest request, MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		StringBuilder builder = new StringBuilder();
		String msg = ErrorCode.FAIL.getMsg();

		FieldError fieldError = bindingResult.getFieldErrors().get(0);
		String fieldName = fieldError.getField();

		builder.append("[");
		builder.append(fieldName);
		builder.append("](은)는 ");
		builder.append(fieldError.getDefaultMessage());
		builder.append(" / 입력된 값: [");
		builder.append(fieldError.getRejectedValue());
		builder.append("]");

		return new ResponseEntity<>(
			ExceptionResponse.builder()
				.msg(builder.toString())
				.path(request.getRequestURI())
				.build(),
			HttpStatus.BAD_REQUEST
		);
	}
}