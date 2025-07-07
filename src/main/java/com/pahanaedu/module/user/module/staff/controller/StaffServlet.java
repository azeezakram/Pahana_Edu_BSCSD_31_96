package com.pahanaedu.module.user.module.staff.controller;

import com.pahanaedu.common.utill.JsonUtil;
import com.pahanaedu.module.user.module.staff.config.StaffAutherization;
import com.pahanaedu.module.user.module.staff.dto.StaffAuthorizationDTO;
import com.pahanaedu.module.user.module.staff.dto.StaffWithoutPasswordDTO;
import com.pahanaedu.module.user.module.staff.service.StaffService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/staff/*")
public class StaffServlet extends HttpServlet {
    private StaffService staffService;

    public void init() {
        this.staffService = new StaffService();
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (!StaffAutherization.isAuthorize(req)) {
            JsonUtil.sendJson(res, "{\"error\" : \"You're unauthorized\"}", HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (pathInfo == null || pathInfo.equals("/")) {
            List<StaffWithoutPasswordDTO> staffs = staffService.findAll();

            if (staffs != null) {
                JsonUtil.sendJson(res, staffs, HttpServletResponse.SC_FOUND);
                return;
            }

            JsonUtil.sendJson(res, "{\"error\" : \"Staffs not found\"}", HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            StaffWithoutPasswordDTO staff = staffService.findById(id);

            if (staff != null) {
                JsonUtil.sendJson(res, staff, HttpServletResponse.SC_FOUND);
                return;
            }

            JsonUtil.sendJson(res, "{\"error\" : \"Staff not found\"}", HttpServletResponse.SC_NOT_FOUND);

        } catch (NumberFormatException e) {
            JsonUtil.sendJson(res, "{\"error\" : \"Invalid staff id\"}", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            JsonUtil.sendJson(res, "{\"error\" : \"Internal error\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }




}
