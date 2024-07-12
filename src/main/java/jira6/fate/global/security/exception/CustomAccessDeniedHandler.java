package jira6.fate.global.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jira6.fate.global.exception.ErrorCode;
import jira6.fate.global.security.dto.SecurityErrorResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        SecurityErrorResponse securityErrorResponse = new SecurityErrorResponse();
        securityErrorResponse.sendResponse(response, ErrorCode.UNAUTHORIZED_MANAGER);

    }

}
