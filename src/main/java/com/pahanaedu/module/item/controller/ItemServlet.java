package com.pahanaedu.module.item.controller;

import com.pahanaedu.common.interfaces.IServicePrototype;
import com.pahanaedu.common.utill.JsonUtil;
import com.pahanaedu.module.item.dto.ItemMinimalDTO;
import com.pahanaedu.module.item.model.Item;
import com.pahanaedu.module.item.service.ItemServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/item/*")
public class ItemServlet extends HttpServlet {

    private IServicePrototype<Item, ItemMinimalDTO> itemService;

    public void init() {
        this.itemService = new ItemServiceImpl();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {

            try {
                List<ItemMinimalDTO> items = itemService.findAll();

                if (items != null) {
                    JsonUtil.sendJson(res, items, HttpServletResponse.SC_OK);
                    return;
                }
            } catch (Exception e) {
                JsonUtil.sendJson(res, Map.of("error", "Internal server error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Item not found"), HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            ItemMinimalDTO item = itemService.findById(id);

            if (item != null) {
                JsonUtil.sendJson(res, item, HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Item not found"), HttpServletResponse.SC_NOT_FOUND);

        }catch(NumberFormatException e) {
            JsonUtil.sendJson(res, Map.of("error", "Invalid item id"), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal server error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }


}
