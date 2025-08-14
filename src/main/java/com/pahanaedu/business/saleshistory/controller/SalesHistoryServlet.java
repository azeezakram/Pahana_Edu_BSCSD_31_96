package com.pahanaedu.business.saleshistory.controller;

import com.pahanaedu.business.saleshistory.dto.SalesHistoryDTO;
import com.pahanaedu.business.saleshistory.exception.SalesHistoryException;
import com.pahanaedu.business.saleshistory.model.SalesHistory;
import com.pahanaedu.business.saleshistory.service.SalesHistoryService;
import com.pahanaedu.business.saleshistory.service.SalesHistoryServiceImpl;
import com.pahanaedu.business.user.module.staff.util.StaffUtils;
import com.pahanaedu.common.utill.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/sales-history/*")
public class SalesHistoryServlet extends HttpServlet {

    private SalesHistoryService salesHistoryService;

    @Override
    public void init() {
        this.salesHistoryService = new SalesHistoryServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (!StaffUtils.isAuthenticated(req)) {
            JsonUtil.sendJson(res, Map.of("error", "Unauthorized - please login"), HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String includeItemsParam = req.getParameter("includeItems");
        boolean includeItems = "true".equalsIgnoreCase(includeItemsParam);

        if (pathInfo == null || pathInfo.equals("/")) {
            List<SalesHistoryDTO> salesHistories;

            if (includeItems) {
                salesHistories = salesHistoryService.findAllWithItems();
            } else {
                salesHistories = salesHistoryService.findAll();
            }

            if (salesHistories != null && !salesHistories.isEmpty()) {
                JsonUtil.sendJson(res, salesHistories, HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Sales history/s not found"), HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));

            SalesHistoryDTO salesHistoryDTO;
            if (includeItems) {
                salesHistoryDTO = salesHistoryService.findByIdWithItems(id);
            } else {
                salesHistoryDTO = salesHistoryService.findById(id);
            }

            if (salesHistoryDTO != null) {
                JsonUtil.sendJson(res, salesHistoryDTO, HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Sales history not found"), HttpServletResponse.SC_NO_CONTENT);

        } catch (NumberFormatException e) {
            JsonUtil.sendJson(res, Map.of("error", "Invalid sales history ID"), HttpServletResponse.SC_BAD_REQUEST);

        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (!StaffUtils.isAuthenticated(req)) {
            JsonUtil.sendJson(res, Map.of("error", "Unauthorized - please login"), HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            if (pathInfo == null || pathInfo.equals("/")) {

                SalesHistory salesHistory = JsonUtil.extract(req, SalesHistory.class);

                if (salesHistory == null) {
                    JsonUtil.sendJson(res, Map.of("error", "Request body is empty"), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                SalesHistoryDTO salesHistoryDTO = salesHistoryService.create(salesHistory);

                if (salesHistoryDTO != null) {
                    JsonUtil.sendJson(res, salesHistoryDTO, HttpServletResponse.SC_CREATED);
                    return;
                }

                JsonUtil.sendJson(res, Map.of("error", "Sales history could not be created"), HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (SalesHistoryException e) {
            JsonUtil.sendJson(res, Map.of("error", e.getMessage()), HttpServletResponse.SC_CONFLICT);
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
                JsonUtil.sendJson(res, Map.of("error", "Sales history ID is missing in URL"), HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            long id = Long.parseLong(pathInfo.substring(1));
            boolean result = salesHistoryService.delete(id);

            if (result) {
                JsonUtil.sendJson(res, Map.of("message", "Successfully deleted sales history ID: " + id), HttpServletResponse.SC_OK);
            } else {
                JsonUtil.sendJson(res, Map.of("error", "Not found sales history ID: " + id), HttpServletResponse.SC_NO_CONTENT);
            }

        } catch (NumberFormatException e) {
            JsonUtil.sendJson(res, Map.of("error", "Invalid sales history ID"), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal server error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}