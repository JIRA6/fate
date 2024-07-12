package jira6.fate.global.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jira6.fate.global.exception.ErrorCode;
import jira6.fate.global.security.dto.SecurityErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        SecurityErrorResponse securityErrorResponse = new SecurityErrorResponse();
        securityErrorResponse.sendResponse(response, ErrorCode.UNAUTHENTICATED);

    }

}
