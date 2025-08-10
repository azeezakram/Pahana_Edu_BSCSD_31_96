package com.pahanaedu.business.user.module.customer.service;

import com.pahanaedu.business.user.module.customer.dto.CustomerDTO;
import com.pahanaedu.business.user.module.customer.exception.CustomerAccountNumberAlreadyExistException;
import com.pahanaedu.business.user.module.customer.mapper.CustomerMapper;
import com.pahanaedu.business.user.module.customer.model.Customer;
import com.pahanaedu.persistence.user.customer.CustomerRepository;
import com.pahanaedu.persistence.user.customer.CustomerRepositoryImpl;

import java.util.List;
import java.util.Objects;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl() {
        this.customerRepository = new CustomerRepositoryImpl();
    }

    @Override
    public CustomerDTO findById(Long id) {
        Customer customer = customerRepository.findById(id);
        return customer != null ? CustomerMapper.toCustomerDto(customer) : null;
    }

    @Override
    public List<CustomerDTO> findAll() {
        List<Customer> customers = customerRepository.findAll();

        return !customers.isEmpty() ? customers
                .stream()
                .map(CustomerMapper::toCustomerDto)
                .toList() : null;
    }

    @Override
    public CustomerDTO create(Customer customer) {

        CustomerDTO existing = findByAccountNumber(customer.getAccountNumber());
        if (existing != null) {
            throw  new CustomerAccountNumberAlreadyExistException("Account number already taken by another customer");
        }

        Customer newCustomer = customerRepository.save(customer);
        return findById(newCustomer.getId());
    }

    @Override
    public CustomerDTO update(Customer customer) {

        CustomerDTO existing = findByAccountNumber(customer.getAccountNumber());
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

    @Override
    public CustomerDTO findByAccountNumber(String accountNumber) {
        Customer customer = customerRepository.findByAccountNumber(accountNumber);
        return Objects.nonNull(customer) ? CustomerMapper.toCustomerDto(customer) : null;
    }

    @Override
    public boolean deleteByAccountNumber(String accountNumber) {
        return customerRepository.deleteByAccountNumber(accountNumber);
    }
}
