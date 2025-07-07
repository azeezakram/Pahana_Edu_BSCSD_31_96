package com.pahanaedu.module.user.module.staff.config;

import com.pahanaedu.common.utill.JsonUtil;
import com.pahanaedu.module.user.module.staff.dto.StaffAuthorizationDTO;
import com.pahanaedu.module.user.module.staff.dto.StaffRequestAuthorizationDTO;
import com.pahanaedu.module.user.module.staff.dto.StaffWithoutPasswordDTO;
import com.pahanaedu.module.user.module.staff.service.StaffService;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

public class StaffAutherization {

    private static final StaffService staffService =  new StaffService();

    public static boolean isAuthorize (HttpServletRequest req) throws IOException {
        StringBuilder jsonBuffer = new StringBuilder();
        String line;

        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
                System.out.println(line);
            }
        }

        String requestBody = jsonBuffer.toString();

        StaffRequestAuthorizationDTO requestStaff = JsonUtil.fromJson(requestBody, StaffRequestAuthorizationDTO.class);

        if (requestStaff != null) {
            StaffWithoutPasswordDTO authorizedStaff = staffService.findByUsername(requestStaff.username());

            if (Objects.nonNull(authorizedStaff)) {
                return requestStaff.username().equals(authorizedStaff.username());
            }
        }

        return false;

    }

}
