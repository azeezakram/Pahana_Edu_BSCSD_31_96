package com.pahanaedu.business.user.module.customer.controller;

import com.pahanaedu.common.utill.JsonUtil;
import com.pahanaedu.business.user.module.customer.dto.CustomerMinimalDTO;
import com.pahanaedu.business.user.module.customer.exception.CustomerAccountNumberAlreadyExistException;
import com.pahanaedu.business.user.module.customer.model.Customer;
import com.pahanaedu.business.user.module.customer.service.CustomerServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
                JsonUtil.sendJson(res, customers, HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Customers not found"), HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            CustomerMinimalDTO customer = customerService.findById(id);

            if (customer != null) {
                JsonUtil.sendJson(res, customer, HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Customer not found"), HttpServletResponse.SC_NOT_FOUND);

        } catch (NumberFormatException e) {

            String accountNumber = pathInfo.substring(1);
            CustomerMinimalDTO customer = customerService.findByAccountNumber(accountNumber);

            if (customer != null) {
                JsonUtil.sendJson(res, customer, HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Customer not found"), HttpServletResponse.SC_NOT_FOUND);

        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {

                Customer customer = JsonUtil.extract(req, Customer.class);

                if (customer == null) {
                    JsonUtil.sendJson(res, Map.of("error", "Request body is empty"), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                CustomerMinimalDTO createdCustomer = customerService.create(customer);

                if (createdCustomer != null) {
                    JsonUtil.sendJson(res, createdCustomer, HttpServletResponse.SC_CREATED);
                    return;
                }

                JsonUtil.sendJson(res, Map.of("error", "Customer could not be created"), HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (CustomerAccountNumberAlreadyExistException e) {
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

                Customer customer = JsonUtil.extract(req, Customer.class);

                if (customer == null) {
                    JsonUtil.sendJson(res, Map.of("error", "Request body is empty"), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                CustomerMinimalDTO updatedCustomer = customerService.update(customer);

                if (updatedCustomer != null) {
                    JsonUtil.sendJson(res, updatedCustomer, HttpServletResponse.SC_OK);
                    return;
                }

                JsonUtil.sendJson(res, Map.of("error", "Customer you are trying update not found"), HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (CustomerAccountNumberAlreadyExistException e) {
            JsonUtil.sendJson(res, Map.of("error", e.getMessage()), HttpServletResponse.SC_CONFLICT);
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtil.sendJson(res, Map.of("error", "Internal error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            JsonUtil.sendJson(res, Map.of("error", "Customer id or account number required"), HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            boolean isDeleted = customerService.delete(id);

            if (isDeleted) {
                JsonUtil.sendJson(res, Map.of("success", true), HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Customer not found"), HttpServletResponse.SC_NOT_FOUND);

        } catch (NumberFormatException e) {

            String accountNumber = pathInfo.substring(1);
            boolean isDeleted = customerService.deleteByAccountNumber(accountNumber);

            if (isDeleted) {
                JsonUtil.sendJson(res, Map.of("success", true), HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Customer not found"), HttpServletResponse.SC_NOT_FOUND);

        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
