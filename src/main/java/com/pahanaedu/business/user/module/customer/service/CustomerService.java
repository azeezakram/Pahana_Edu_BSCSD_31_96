package com.pahanaedu.business.user.module.customer.service;

import com.pahanaedu.business.user.module.customer.dto.CustomerDTO;
import com.pahanaedu.business.user.module.customer.model.Customer;
import com.pahanaedu.common.interfaces.Service;

public interface CustomerService extends Service<Customer, CustomerDTO> {
    CustomerDTO update(Customer customer);
    CustomerDTO findByAccountNumber(String accountNumber);
    boolean deleteByAccountNumber(String accountNumber);
}
