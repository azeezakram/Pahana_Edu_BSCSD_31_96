package com.pahanaedu.business.sellHistory.servlet;

import com.pahanaedu.business.sellHistory.dto.SellHistoryDTO;
import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellHistory.service.SellHistoryServiceImpl;
import com.pahanaedu.business.user.module.customer.dto.CustomerDTO;
import com.pahanaedu.common.interfaces.Service;
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

//        if (pathInfo == null || pathInfo.equals("/")) {
//            List<SellHistoryDTO> customers = sellHistoryService.findAll();
//
//            if (customers != null) {
//                JsonUtil.sendJson(res, customers, HttpServletResponse.SC_OK);
//                return;
//            }
//
//            JsonUtil.sendJson(res, Map.of("error", "Customers not found"), HttpServletResponse.SC_NOT_FOUND);
//            return;
//        }

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

}
