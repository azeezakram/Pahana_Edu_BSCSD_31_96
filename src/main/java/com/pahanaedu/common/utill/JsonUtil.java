package com.pahanaedu.common.utill;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    private static <T> T fromJson(String json, Class<T> clazz) throws IOException {

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

    public static <T> T extract (HttpServletRequest req, Class<T> clazz) throws IOException {
        var jsonBuffer = new StringBuilder();
        String line;

        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
                System.out.println(line);
            }
        }

        String requestBody = jsonBuffer.toString();

        return fromJson(requestBody, clazz);

    }
}

