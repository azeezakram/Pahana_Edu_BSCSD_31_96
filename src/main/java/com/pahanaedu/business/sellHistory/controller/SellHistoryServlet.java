package com.pahanaedu.business.sellHistory.controller;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.exception.ItemException;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.business.sellHistory.dto.SellHistoryDTO;
import com.pahanaedu.business.sellHistory.exception.SellHistoryException;
import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellHistory.service.SellHistoryServiceImpl;
import com.pahanaedu.common.utill.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/sell-history/*")
public class SellHistoryServlet extends HttpServlet {

    private SellHistoryServiceImpl sellHistoryService;

    @Override
    public void init() {
        this.sellHistoryService = new SellHistoryServiceImpl();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<SellHistoryDTO> sellHistories = sellHistoryService.findAll();

            if (sellHistories != null) {
                JsonUtil.sendJson(res, sellHistories, HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Sell history/s not found"), HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            SellHistoryDTO sellHistoryDTO = sellHistoryService.findById(id);

            if (sellHistoryDTO != null) {
                JsonUtil.sendJson(res, sellHistoryDTO, HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Sell history not found"), HttpServletResponse.SC_NOT_FOUND);

        } catch (NumberFormatException e) {
            JsonUtil.sendJson(res, Map.of("error", "Invalid sell history id"), HttpServletResponse.SC_NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.sendJson(res, Map.of("error", "Internal error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {

                SellHistory sellHistory = JsonUtil.extract(req, SellHistory.class);

                if (sellHistory == null) {
                    JsonUtil.sendJson(res, Map.of("error", "Request body is empty"), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                SellHistoryDTO sellHistoryDTO = sellHistoryService.create(sellHistory);

                if (sellHistoryDTO != null) {
                    JsonUtil.sendJson(res, sellHistoryDTO, HttpServletResponse.SC_CREATED);
                    return;
                }

                JsonUtil.sendJson(res, Map.of("error", "Sell history could not be created"), HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (SellHistoryException e) {
            JsonUtil.sendJson(res, Map.of("error", e.getMessage()), HttpServletResponse.SC_CONFLICT);
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                JsonUtil.sendJson(res, Map.of("error", "Sell history ID is missing in URL"), HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            long id = Long.parseLong(pathInfo.substring(1));
            boolean result = sellHistoryService.delete(id);

            if (result) {
                JsonUtil.sendJson(res, Map.of("message","Successfully deleted sell history ID: " + id
                ), HttpServletResponse.SC_OK);
            } else {
                JsonUtil.sendJson(res, Map.of("error", "Not found sell history ID: " + id), HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (NumberFormatException e) {
            JsonUtil.sendJson(res, Map.of("error", "Invalid sell history ID "), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal server error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
