package jira6.fate.global.security.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public abstract class SecurityResponse<T> {

    public abstract void sendResponse(HttpServletResponse response, T t) throws IOException;

    protected String objectToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(object);
    }

    protected void initResponse(HttpServletResponse response, int statusCode, String json) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(statusCode);
        response.getWriter().write(json);
        response.flushBuffer();

    }

}
