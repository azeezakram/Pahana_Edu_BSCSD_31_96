package com.pahanaedu.business.user.module.customer.service;

import com.pahanaedu.business.user.module.customer.dto.CustomerDto;
import com.pahanaedu.business.user.module.customer.exception.CustomerAccountNumberAlreadyExistException;
import com.pahanaedu.business.user.module.customer.mapper.CustomerMapper;
import com.pahanaedu.business.user.module.customer.model.Customer;
import com.pahanaedu.common.interfaces.Service;
import com.pahanaedu.persistence.user.customer.CustomerRepositoryImpl;

import java.util.List;
import java.util.Objects;

public class CustomerServiceImpl implements Service<Customer, CustomerDto> {

    private final CustomerRepositoryImpl customerRepository;

    public CustomerServiceImpl() {
        this.customerRepository = new CustomerRepositoryImpl();
    }


    @Override
    public CustomerDto findById(Long id) {
        Customer customer = customerRepository.findById(id);
        return customer != null ? CustomerMapper.toCustomerDto(customer) : null;
    }

    @Override
    public List<CustomerDto> findAll() {
        List<Customer> customers = customerRepository.findAll();

        return !customers.isEmpty() ? customers
                .stream()
                .map(CustomerMapper::toCustomerDto)
                .toList() : null;
    }

    @Override
    public CustomerDto create(Customer customer) {

        CustomerDto existing = findByAccountNumber(customer.getAccountNumber());
        if (existing != null) {
            throw  new CustomerAccountNumberAlreadyExistException("Account number already taken by another customer");
        }


        Customer newCustomer = customerRepository.save(customer);
        return findById(newCustomer.getId());
    }

    @Override
    public CustomerDto update(Customer customer) {

        CustomerDto existing = findByAccountNumber(customer.getAccountNumber());
        if (existing != null && !existing.getId().equals(customer.getId())) {
            throw new CustomerAccountNumberAlreadyExistException("Account number already taken by another customer");
        }

        if (customer.getAccountNumber() == null || customer.getAccountNumber().isBlank()) {
            customer.setAccountNumber("ph-edu-c-".concat(String.valueOf(customer.getId())));
        }

        Customer newCustomer = customerRepository.update(customer);

        return findById(newCustomer.getId());
    }

    @Override
    public boolean delete(Long id) {
        return customerRepository.delete(id);
    }

    public CustomerDto findByAccountNumber(String accountNumber) {
        Customer customer = customerRepository.findByAccountNumber(accountNumber);
        return Objects.nonNull(customer) ? CustomerMapper.toCustomerDto(customer) : null;
    }

    public boolean deleteByAccountNumber(String accountNumber) {
        return customerRepository.deleteByAccountNumber(accountNumber);
    }
}
