package com.pahanaedu.common.utill;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T fromJson(String json, Class<T> clazz) throws IOException {

        if (json.isEmpty()) {
            return null;
        }

        return mapper.readValue(json, clazz);
    }

    public static void sendJson(HttpServletResponse resp, Object data, int status) throws IOException {
        String json = mapper.writeValueAsString(data);
        resp.setContentType("application/json");
        resp.setStatus(status);
        resp.getWriter().write(json);
    }
}

