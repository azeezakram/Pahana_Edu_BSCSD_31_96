package com.pahanaedu.module.user.module.customer.service;

import com.pahanaedu.common.interfaces.Service;
import com.pahanaedu.module.user.module.customer.dto.CustomerMinimalDTO;
import com.pahanaedu.module.user.module.customer.exception.CustomerAccountNumberAlreadyExistException;
import com.pahanaedu.module.user.module.customer.mapper.CustomerMapper;
import com.pahanaedu.module.user.module.customer.model.Customer;
import com.pahanaedu.module.user.module.customer.repository.CustomerRepositoryImpl;

import java.util.List;
import java.util.Objects;

public class CustomerServiceImpl implements Service<Customer, CustomerMinimalDTO> {

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

        return !customers.isEmpty() ? customers
                .stream()
                .map(CustomerMapper::toCustomerMinimalDTO)
                .toList() : null;
    }

    @Override
    public CustomerMinimalDTO create(Customer customer) {

        CustomerMinimalDTO existing = findByAccountNumber(customer.getAccountNumber());
        if (existing != null) {
            throw  new CustomerAccountNumberAlreadyExistException("Account number already taken by another customer");
        }

        Customer newCustomer = customerRepository.save(customer);
        return findById(newCustomer.getId());
    }

    @Override
    public CustomerMinimalDTO update(Customer customer) {

        CustomerMinimalDTO existing = findByAccountNumber(customer.getAccountNumber());
        if (existing != null && !existing.id().equals(customer.getId())) {
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


    public CustomerMinimalDTO findByAccountNumber(String accountNumber) {
        Customer customer = customerRepository.findByAccountNumber(accountNumber);
        return Objects.nonNull(customer) ? CustomerMapper.toCustomerMinimalDTO(customer) : null;
    }

    public boolean deleteByAccountNumber(String accountNumber) {
        return customerRepository.deleteByAccountNumber(accountNumber);
    }
}
