package com.pahanaedu.business.user.module.customer.mapper;


import com.pahanaedu.business.user.module.customer.dto.CustomerDTO;
import com.pahanaedu.business.user.module.customer.model.Customer;

public class CustomerMapper {

    public static CustomerDTO toCustomerDto(Customer customer) {

        return new CustomerDTO.Builder()
                .setId(customer.getId())
                .setName(customer.getName())
                .setRole(customer.getRole())
                .setAccountNumber(customer.getAccountNumber())
                .setAddress(customer.getAddress())
                .setPhoneNumber(customer.getPhoneNumber())
                .setCreatedAt(customer.getCreatedAt())
                .setUpdatedAt(customer.getUpdatedAt())
                .build();

    }

}

