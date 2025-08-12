package com.pahanaedu.business.user.module.staff.controller;

import com.pahanaedu.business.user.module.staff.dto.StaffAuthDTO;
import com.pahanaedu.business.user.module.staff.dto.StaffWithoutPasswordDTO;
import com.pahanaedu.business.user.module.staff.exception.StaffUsernameAlreadyExistException;
import com.pahanaedu.business.user.module.staff.model.Staff;
import com.pahanaedu.business.user.module.staff.service.StaffService;
import com.pahanaedu.business.user.module.staff.service.StaffServiceImpl;
import com.pahanaedu.business.user.module.staff.util.StaffUtils;
import com.pahanaedu.common.utill.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/staff/*")
public class StaffServlet extends HttpServlet {
    private StaffService staffService;

    public void init() {
        this.staffService = new StaffServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (!StaffUtils.isAuthenticated(req)) {
            JsonUtil.sendJson(res, Map.of("error", "Unauthorized - please login"), HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (pathInfo == null || pathInfo.equals("/")) {
            try {
                List<StaffWithoutPasswordDTO> staffs = staffService.findAll();

                if (staffs != null) {
                    JsonUtil.sendJson(res, staffs, HttpServletResponse.SC_OK);
                    return;
                }
            } catch (Exception e) {
                JsonUtil.sendJson(res, Map.of("error", "Internal server error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Staffs not found"), HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            StaffWithoutPasswordDTO staff = staffService.findById(id);

            if (staff != null) {
                JsonUtil.sendJson(res, staff, HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Staffs not found"), HttpServletResponse.SC_NO_CONTENT);

        } catch (NumberFormatException e) {
            JsonUtil.sendJson(res, Map.of("error", "Invalid staff id"), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal server error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (!"/login".equalsIgnoreCase(pathInfo) && !"/current".equalsIgnoreCase(pathInfo) && !"/".equalsIgnoreCase(pathInfo)) {
            if (!StaffUtils.isAuthenticated(req)) {
                JsonUtil.sendJson(res, Map.of("error", "Unauthorized - please login"), HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        try {
            if (pathInfo == null || pathInfo.equals("/")) {

                Staff staff = JsonUtil.extract(req, Staff.class);

                if (staff == null) {
                    JsonUtil.sendJson(res, Map.of("error", "Request body is empty"), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                StaffWithoutPasswordDTO createdStaff = staffService.create(staff);

                if (createdStaff != null) {
                    JsonUtil.sendJson(res, createdStaff, HttpServletResponse.SC_CREATED);
                    return;
                }

                JsonUtil.sendJson(res, Map.of("error", "Staff could not be created"), HttpServletResponse.SC_CONFLICT);
            } else if (pathInfo.equals("/login")) {
                if (StaffUtils.isAuthenticated(req)) {
                    JsonUtil.sendJson(res, Map.of("success", "You have already logged in"), HttpServletResponse.SC_OK);
                    return;
                }
                StaffAuthDTO auth = JsonUtil.extract(req, StaffAuthDTO.class);

                if (auth == null ||
                        auth.username() == null || auth.username().isEmpty() ||
                        auth.password() == null || auth.password().isEmpty()) {

                    JsonUtil.sendJson(res, Map.of("error", "Username and password required"), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                boolean isAuthenticated = staffService.login(auth, req);

                if (isAuthenticated) {
                    JsonUtil.sendJson(res, Map.of("success", "Staff successfully logged in"), HttpServletResponse.SC_OK);
                } else {
                    JsonUtil.sendJson(res, Map.of("error", "Incorrect username or password"), HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else if (pathInfo.equals("/logout")) {
                HttpSession session = req.getSession(false);
                if (session != null) {
                    session.invalidate();
                    JsonUtil.sendJson(res, Map.of("message", "Logout successful"), HttpServletResponse.SC_OK);
                } else {
                    JsonUtil.sendJson(res, Map.of("error", "No active session"), HttpServletResponse.SC_BAD_REQUEST);
                }
            } else if (pathInfo.equals("/current")) {
                if (StaffUtils.isAuthenticated(req)) {
                    StaffWithoutPasswordDTO staff = staffService.findByUsername((String) req.getSession().getAttribute("staff"));
                    JsonUtil.sendJson(res, staff, HttpServletResponse.SC_OK);
                } else {
                    JsonUtil.sendJson(res, Map.of("error", "Staff not logged in"), HttpServletResponse.SC_UNAUTHORIZED);
                }
            }

        } catch (StaffUsernameAlreadyExistException e) {
            JsonUtil.sendJson(res, Map.of("error", e.getMessage()), HttpServletResponse.SC_CONFLICT);
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal server error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (!StaffUtils.isAuthenticated(req)) {
            JsonUtil.sendJson(res, Map.of("error", "Unauthorized - please login"), HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {

            if (pathInfo == null || pathInfo.equals("/")) {

                Staff staff = JsonUtil.extract(req, Staff.class);
                if (staff == null) {
                    JsonUtil.sendJson(res, Map.of("error", "Request body is empty"), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                StaffWithoutPasswordDTO createdStaff = staffService.update(staff);
                if (createdStaff != null) {
                    JsonUtil.sendJson(res, createdStaff, HttpServletResponse.SC_CREATED);
                    return;
                }

                JsonUtil.sendJson(res, Map.of("error", "Staff could not be updated"), HttpServletResponse.SC_BAD_REQUEST);
            }

        } catch (StaffUsernameAlreadyExistException e) {
            JsonUtil.sendJson(res, Map.of("error", e.getMessage()), HttpServletResponse.SC_CONFLICT);
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal server error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (!StaffUtils.isAuthenticated(req)) {
            JsonUtil.sendJson(res, Map.of("error", "Unauthorized - please login"), HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                JsonUtil.sendJson(res, Map.of("error", "Staff ID is required in URL"), HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            long id = Long.parseLong(pathInfo.substring(1));
            boolean result = staffService.delete(id);

            if (result) {
                JsonUtil.sendJson(res, Map.of("message", "Successfully deleted"), HttpServletResponse.SC_OK);
            } else {
                JsonUtil.sendJson(res, Map.of("error", "Staff not found"), HttpServletResponse.SC_NO_CONTENT);
            }

        } catch (NumberFormatException e) {
            JsonUtil.sendJson(res, Map.of("error", "Invalid staff ID "), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal server error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
