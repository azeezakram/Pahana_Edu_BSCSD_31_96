package com.pahanaedu.persistence.user.customer;

import com.pahanaedu.business.user.module.customer.model.Customer;
import com.pahanaedu.business.user.module.staff.model.Staff;
import com.pahanaedu.common.interfaces.Repository;

public interface CustomerRepository extends Repository<Customer> {
    Customer update(Customer customer);
    Customer findByAccountNumber(String accountNumber);
    boolean deleteByAccountNumber(String accountNumber);
}
