package jira6.fate.global.security.dto;

import jakarta.servlet.http.HttpServletResponse;
import jira6.fate.global.dto.MessageResponse;

import java.io.IOException;

public class SecurityMessageResponse extends SecurityResponse<MessageResponse> {

    @Override
    public void sendResponse(HttpServletResponse response, MessageResponse messageResponse) throws IOException {

        String json = objectToJson(messageResponse);
        initResponse(response, messageResponse.getStatusCode(), json);

    }

}
