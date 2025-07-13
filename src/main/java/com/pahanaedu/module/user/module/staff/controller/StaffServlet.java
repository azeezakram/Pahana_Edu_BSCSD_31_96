package com.pahanaedu.module.user.module.staff.controller;

import com.pahanaedu.common.utill.JsonUtil;
import com.pahanaedu.module.user.module.staff.dto.StaffWithoutPasswordDTO;
import com.pahanaedu.module.user.module.staff.model.Staff;
import com.pahanaedu.module.user.module.staff.service.StaffServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/staff/*")
public class StaffServlet extends HttpServlet {
    private StaffServiceImpl staffService;

    public void init() {
        this.staffService = new StaffServiceImpl();
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

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
            throw new RuntimeException(e);
        }

    }


    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {

                Staff staff = JsonUtil.extract(req, Staff.class);

                StaffWithoutPasswordDTO createdStaff = staffService.create(staff);

                if (createdStaff != null) {
                    JsonUtil.sendJson(res, createdStaff, HttpServletResponse.SC_CREATED);
                    return;
                }

                JsonUtil.sendJson(res, "{\"error\" : \"Staff not found\"}", HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            JsonUtil.sendJson(res, "{\"error\" : \"Internal error\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        try {

            if (pathInfo == null || pathInfo.equals("/")) {

                Staff staff = JsonUtil.extract(req, Staff.class);

                StaffWithoutPasswordDTO createdStaff = staffService.update(staff);

                if (createdStaff != null) {
                    JsonUtil.sendJson(res, createdStaff, HttpServletResponse.SC_CREATED);
                    return;
                }

                JsonUtil.sendJson(res, "{\"error\" : \"Staff not found\"}", HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (Exception e) {
            JsonUtil.sendJson(res, "{\"error\" : \"Internal error\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                JsonUtil.sendJson(res, "{\"error\" : \"Staff ID is required in URL\"}", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            long id = Long.parseLong(pathInfo.substring(1));
            boolean result = staffService.delete(id);

            if (result) {
                JsonUtil.sendJson(res, "{\"message\": \"Successfully deleted\"}", HttpServletResponse.SC_OK);
            } else {
                JsonUtil.sendJson(res, "{\"error\" : \"Staff not found\"}", HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (NumberFormatException e) {
            JsonUtil.sendJson(res, "{\"error\" : \"Invalid staff ID format\"}", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            JsonUtil.sendJson(res, "{\"error\" : \"Internal server error\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
