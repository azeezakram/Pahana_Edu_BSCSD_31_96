package com.pahanaedu.business.user.module.customer.mapper;


import com.pahanaedu.business.user.module.customer.dto.CustomerDto;
import com.pahanaedu.business.user.module.customer.model.Customer;

public class CustomerMapper {

    public static CustomerDto toCustomerDto(Customer customer) {

        return new CustomerDto.Builder()
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
