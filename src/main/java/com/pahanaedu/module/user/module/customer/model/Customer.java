package com.pahanaedu.module.user.module.customer.model;

import com.pahanaedu.module.user.model.User;

import java.time.LocalDateTime;

public class Customer extends User {

    private String accountNumber;
    private String address;
    private String phoneNumber;

    public Customer(Long id, String name, String role, LocalDateTime createdAt,
                    LocalDateTime updatedAt, String accountNumber, String address, String phoneNumber) {

        super(id, name, role, createdAt, updatedAt);
        this.accountNumber = accountNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;

    }

    public Customer(String name, String accountNumber, String address, String phoneNumber) {

        super(name);
        this.accountNumber = accountNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;

    }

    public Customer(String name, String address, String phoneNumber) {

        super(name);
        this.address = address;
        this.phoneNumber = phoneNumber;

    }

    public Customer() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "accountNumber='" + accountNumber + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
