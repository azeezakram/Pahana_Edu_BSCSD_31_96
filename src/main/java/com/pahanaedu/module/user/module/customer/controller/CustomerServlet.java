package com.pahanaedu.module.user.module.customer.controller;

import com.pahanaedu.common.utill.JsonUtil;
import com.pahanaedu.module.user.module.customer.dto.CustomerMinimalDTO;
import com.pahanaedu.module.user.module.customer.model.Customer;
import com.pahanaedu.module.user.module.customer.service.CustomerServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/customer/*")
public class CustomerServlet extends HttpServlet {
    private CustomerServiceImpl customerService;

    public void init() {
        this.customerService = new CustomerServiceImpl();
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<CustomerMinimalDTO> customers = customerService.findAll();

            if (customers != null) {
                JsonUtil.sendJson(res, customers, HttpServletResponse.SC_FOUND);
                return;
            }

            JsonUtil.sendJson(res, "{\"error\" : \"Customers not found\"}", HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            CustomerMinimalDTO customer = customerService.findById(id);

            if (customer != null) {
                JsonUtil.sendJson(res, customer, HttpServletResponse.SC_FOUND);
                return;
            }

            JsonUtil.sendJson(res, "{\"error\" : \"Customer not found\"}", HttpServletResponse.SC_NOT_FOUND);

        } catch (NumberFormatException e) {

            String accountNumber = pathInfo.substring(1);
            CustomerMinimalDTO customer = customerService.findByAccountNumber(accountNumber);

            if (customer != null) {
                JsonUtil.sendJson(res, customer, HttpServletResponse.SC_FOUND);
                return;
            }

            JsonUtil.sendJson(res, "{\"error\" : \"Customer not found\"}", HttpServletResponse.SC_NOT_FOUND);

        } catch (Exception e) {
            JsonUtil.sendJson(res, "{\"error\" : \"Internal error\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {

                Customer customer = JsonUtil.extract(req, Customer.class);

                if (customer == null) {
                    JsonUtil.sendJson(res, "{\"error\" : \"Request body is empty\"}", HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                CustomerMinimalDTO existing = customerService.findByAccountNumber(customer.getAccountNumber());
                if (existing != null) {
                    JsonUtil.sendJson(res, "{\"error\" : \"Account number already taken by another customer\"}", HttpServletResponse.SC_CONFLICT);
                    return;
                }

                CustomerMinimalDTO createdCustomer = customerService.create(customer);

                if (createdCustomer != null) {
                    JsonUtil.sendJson(res, createdCustomer, HttpServletResponse.SC_CREATED);
                    return;
                }

                JsonUtil.sendJson(res, "{\"error\" : \"Customer could not be created\"}", HttpServletResponse.SC_BAD_REQUEST);
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

                Customer customer = JsonUtil.extract(req, Customer.class);

                if (customer == null) {
                    JsonUtil.sendJson(res, "{\"error\" : \"Request body is empty\"}", HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                CustomerMinimalDTO existing = customerService.findByAccountNumber(customer.getAccountNumber());
                if (existing != null && !existing.id().equals(customer.getId())) {
                    JsonUtil.sendJson(res, "{\"error\" : \"Account number already taken by another customer\"}", HttpServletResponse.SC_CONFLICT);
                    return;
                }

                CustomerMinimalDTO updatedCustomer = customerService.update(customer);

                if (updatedCustomer != null) {
                    JsonUtil.sendJson(res, updatedCustomer, HttpServletResponse.SC_OK);
                    return;
                }

                JsonUtil.sendJson(res, "{\"error\" : \"Customer could not be updated\"}", HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            JsonUtil.sendJson(res, "{\"error\" : \"Internal error\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


}
