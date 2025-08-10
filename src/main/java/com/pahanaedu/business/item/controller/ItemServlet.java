package com.pahanaedu.business.item.controller;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.exception.ItemException;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.business.item.service.ItemServiceImpl;
import com.pahanaedu.common.utill.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/item/*")
public class ItemServlet extends HttpServlet {

    private ItemServiceImpl itemService;

    public void init() {
        this.itemService = new ItemServiceImpl();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {

            try {
                List<ItemDTO> items = itemService.findAll();

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
            ItemDTO item = itemService.findById(id);

            if (item != null) {
                JsonUtil.sendJson(res, item, HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Item not found"), HttpServletResponse.SC_NOT_FOUND);

        } catch (NumberFormatException e) {
            JsonUtil.sendJson(res, Map.of("error", "Invalid item id"), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal server error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {

                Item item = JsonUtil.extract(req, Item.class);

                if (item == null) {
                    JsonUtil.sendJson(res, Map.of("error", "Request body is empty"), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                ItemDTO createdItem = itemService.create(item);

                if (createdItem != null) {
                    JsonUtil.sendJson(res, createdItem, HttpServletResponse.SC_CREATED);
                    return;
                }

                JsonUtil.sendJson(res, Map.of("error", "Item could not be created"), HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (ItemException e) {
            JsonUtil.sendJson(res, Map.of("error", e.getMessage()), HttpServletResponse.SC_CONFLICT);
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {

                Item item = JsonUtil.extract(req, Item.class);

                if (item == null) {
                    JsonUtil.sendJson(res, Map.of("error", "Request body is empty"), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                ItemDTO updatedItem = itemService.update(item);

                if (updatedItem != null) {
                    JsonUtil.sendJson(res, updatedItem, HttpServletResponse.SC_OK);
                    return;
                }

                JsonUtil.sendJson(res, Map.of("error", "Item you are trying update not found"), HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                JsonUtil.sendJson(res, Map.of("error", "Item ID is required in URL"), HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            long id = Long.parseLong(pathInfo.substring(1));
            boolean result = itemService.delete(id);

            if (result) {
                JsonUtil.sendJson(res, Map.of("message", "Successfully deleted"), HttpServletResponse.SC_OK);
            } else {
                JsonUtil.sendJson(res, Map.of("error", "Item not found"), HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (NumberFormatException e) {
            JsonUtil.sendJson(res, Map.of("error", "Invalid item ID "), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal server error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


}
