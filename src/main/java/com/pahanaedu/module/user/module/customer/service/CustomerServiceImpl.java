package com.pahanaedu.module.user.module.customer.service;

import com.pahanaedu.common.interfaces.IServicePrototype;
import com.pahanaedu.module.user.module.customer.dto.CustomerMinimalDTO;
import com.pahanaedu.module.user.module.customer.mapper.CustomerMapper;
import com.pahanaedu.module.user.module.customer.model.Customer;
import com.pahanaedu.module.user.module.customer.repository.CustomerRepositoryImpl;
import com.pahanaedu.module.user.module.staff.dto.StaffWithoutPasswordDTO;

import java.util.List;
import java.util.Objects;

public class CustomerServiceImpl implements IServicePrototype<Customer, CustomerMinimalDTO> {

    private final CustomerRepositoryImpl customerRepository;

    public CustomerServiceImpl() {
        this.customerRepository = new CustomerRepositoryImpl();
    }


    @Override
    public CustomerMinimalDTO findById(Long id) {
        Customer customer = customerRepository.findById(id);

        return customer != null ? CustomerMapper.toCustomerMinimalDTO(customer) : null;
    }

    @Override
    public List<CustomerMinimalDTO> findAll() {
        List<Customer> customers = customerRepository.findAll();

        return customers != null ? customers
                .stream()
                .map(CustomerMapper::toCustomerMinimalDTO)
                .toList() : null;
    }

    @Override
    public CustomerMinimalDTO create(Customer customer) {
        Customer newCustomer = customerRepository.save(customer);
        return CustomerMapper.toCustomerMinimalDTO(newCustomer);
    }

    @Override
    public CustomerMinimalDTO update(Customer customer) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }


    public CustomerMinimalDTO findByAccountNumber(String accountNumber) {
        Customer customer = customerRepository.findByAccountNumber(accountNumber);
        return Objects.nonNull(customer) ? CustomerMapper.toCustomerMinimalDTO(customer) : null;
    }
}
