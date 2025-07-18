package com.pahanaedu.module.user.module.customer.mapper;


import com.pahanaedu.module.user.module.customer.dto.CustomerMinimalDTO;
import com.pahanaedu.module.user.module.customer.model.Customer;

public class CustomerMapper {

//    public static CustomerCreationDTO toCustomerCreationDTO(Customer customer) {
//        return new CustomerCreationDTO(customer.getName(), customer.getAddress(), customer.getPhoneNumber());
//    }

//    public static Customer toCustomer(CustomerCreationDTO customerCreationDTO) {
//        return new Customer(customerCreationDTO.name(), customerCreationDTO.address(), customerCreationDTO.phoneNumber());
//    }

    public static CustomerMinimalDTO toCustomerMinimalDTO(Customer customer) {
        return new CustomerMinimalDTO(customer.getId(), customer.getName(), customer.getRole(),
                customer.getAccountNumber(), customer.getAddress(), customer.getPhoneNumber());
    }

}
