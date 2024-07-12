package jira6.fate.global.security.dto;

import jakarta.servlet.http.HttpServletResponse;
import jira6.fate.global.exception.ErrorCode;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SecurityErrorResponse extends SecurityResponse<ErrorCode> {

    @Override
    public void sendResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {

        Map<String, Object> data = setData(errorCode);
        String json = objectToJson(data);
        initResponse(response, errorCode.getStatus(), json);

    }

    private Map<String, Object> setData(ErrorCode errorCode) {

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("statusCode", errorCode.getStatus());
        data.put("message", errorCode.getMessage());

        return data;
    }

}
