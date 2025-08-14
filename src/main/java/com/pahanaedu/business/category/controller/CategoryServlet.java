package com.pahanaedu.business.category.controller;

import com.pahanaedu.business.category.dto.CategoryDTO;
import com.pahanaedu.business.category.model.Category;
import com.pahanaedu.business.category.service.CategoryService;
import com.pahanaedu.business.category.service.CategoryServiceImpl;
import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.exception.ItemException;
import com.pahanaedu.business.user.module.staff.util.StaffUtils;
import com.pahanaedu.common.utill.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/category/*")
public class CategoryServlet extends HttpServlet {

    private CategoryService categoryService;

    public void init() {
        this.categoryService = new CategoryServiceImpl();
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
                List<CategoryDTO> categories = categoryService.findAll();

                if (categories != null) {
                    JsonUtil.sendJson(res, categories, HttpServletResponse.SC_OK);
                    return;
                }
            } catch (Exception e) {
                JsonUtil.sendJson(res, Map.of("error", "Internal server error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Category not found"), HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            CategoryDTO category = categoryService.findById(id);

            if (category != null) {
                JsonUtil.sendJson(res, category, HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Category not found"), HttpServletResponse.SC_NO_CONTENT);

        } catch (NumberFormatException e) {
            JsonUtil.sendJson(res, Map.of("error", "Invalid category id"), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal server error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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

                Category category = JsonUtil.extract(req, Category.class);

                if (category == null) {
                    JsonUtil.sendJson(res, Map.of("error", "Request body is empty"), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                CategoryDTO createdItem = categoryService.create(category);

                if (createdItem != null) {
                    JsonUtil.sendJson(res, createdItem, HttpServletResponse.SC_CREATED);
                    return;
                }

                JsonUtil.sendJson(res, Map.of("error", "Category could not be created"), HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (ItemException e) {
            JsonUtil.sendJson(res, Map.of("error", e.getMessage()), HttpServletResponse.SC_CONFLICT);
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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

                Category item = JsonUtil.extract(req, Category.class);

                if (item == null) {
                    JsonUtil.sendJson(res, Map.of("error", "Request body is empty"), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                CategoryDTO updatedCategory = categoryService.update(item);

                if (updatedCategory != null) {
                    JsonUtil.sendJson(res, updatedCategory, HttpServletResponse.SC_OK);
                    return;
                }

                JsonUtil.sendJson(res, Map.of("error", "Category you are trying update not found"), HttpServletResponse.SC_NO_CONTENT);
            }
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
                JsonUtil.sendJson(res, Map.of("error", "Category ID is required in URL"), HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            long id = Long.parseLong(pathInfo.substring(1));
            boolean result = categoryService.delete(id);

            if (result) {
                JsonUtil.sendJson(res, Map.of("message", "Successfully deleted"), HttpServletResponse.SC_OK);
            } else {
                JsonUtil.sendJson(res, Map.of("error", "Category not found"), HttpServletResponse.SC_NO_CONTENT);
            }

        } catch (NumberFormatException e) {
            JsonUtil.sendJson(res, Map.of("error", "Invalid category ID "), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal server error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
