package com.pahanaedu.business.user.module.customer.controller;

import com.pahanaedu.business.user.module.customer.dto.CustomerDTO;
import com.pahanaedu.business.user.module.customer.service.CustomerService;
import com.pahanaedu.business.user.module.staff.util.StaffUtils;
import com.pahanaedu.common.utill.JsonUtil;
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
    private CustomerService customerService;

    @Override
    public void init() {
        this.customerService = new CustomerServiceImpl("production");
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
            List<CustomerDTO> customers = customerService.findAll();

            if (customers != null) {
                JsonUtil.sendJson(res, customers, HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Customers not found"), HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        try {

            String accno = req.getParameter("accno");

            if (accno == null) {
                Long id = Long.parseLong(pathInfo.substring(1));
                CustomerDTO customer = customerService.findById(id);

                if (customer != null) {
                    JsonUtil.sendJson(res, customer, HttpServletResponse.SC_OK);
                    return;
                }
            } else {
                String accountNumber = pathInfo.substring(1);
                CustomerDTO customer = customerService.findByAccountNumber(accountNumber);

                if (customer != null) {
                    JsonUtil.sendJson(res, customer, HttpServletResponse.SC_OK);
                    return;
                }
            }

            JsonUtil.sendJson(res, Map.of("error", "Customer not found"), HttpServletResponse.SC_NO_CONTENT);

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

                Customer customer = JsonUtil.extract(req, Customer.class);

                if (customer == null) {
                    JsonUtil.sendJson(res, Map.of("error", "Request body is empty"), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                CustomerDTO createdCustomer = customerService.create(customer);

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

                Customer customer = JsonUtil.extract(req, Customer.class);

                if (customer == null) {
                    JsonUtil.sendJson(res, Map.of("error", "Request body is empty"), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                CustomerDTO updatedCustomer = customerService.update(customer);

                if (updatedCustomer != null) {
                    JsonUtil.sendJson(res, updatedCustomer, HttpServletResponse.SC_OK);
                    return;
                }

                JsonUtil.sendJson(res, Map.of("error", "Customer you are trying update not found"), HttpServletResponse.SC_NO_CONTENT);
            }
        } catch (CustomerAccountNumberAlreadyExistException e) {
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

        if (pathInfo == null || pathInfo.equals("/")) {
            JsonUtil.sendJson(res, Map.of("error", "Customer id or account number required"), HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            boolean isDeleted = customerService.delete(id);

            if (isDeleted) {
                JsonUtil.sendJson(res, Map.of("success", true), HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Customer not found"), HttpServletResponse.SC_NO_CONTENT);

        } catch (NumberFormatException e) {

            String accountNumber = pathInfo.substring(1);
            boolean isDeleted = customerService.deleteByAccountNumber(accountNumber);

            if (isDeleted) {
                JsonUtil.sendJson(res, Map.of("success", true), HttpServletResponse.SC_OK);
                return;
            }

            JsonUtil.sendJson(res, Map.of("error", "Customer not found"), HttpServletResponse.SC_NO_CONTENT);

        } catch (Exception e) {
            JsonUtil.sendJson(res, Map.of("error", "Internal error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
