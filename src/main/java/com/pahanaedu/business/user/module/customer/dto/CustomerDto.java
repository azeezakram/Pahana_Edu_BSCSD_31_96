package com.pahanaedu.business.user.module.customer.dto;

import java.time.LocalDateTime;

public class CustomerDto {

    private final Long id;
    private final String name;
    private final String role;
    private final String accountNumber;
    private final String address;
    private final String phoneNumber;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private CustomerDto(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.role = builder.role;
        this.accountNumber = builder.accountNumber;
        this.address = builder.address;
        this.phoneNumber = builder.phoneNumber;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String role;
        private String accountNumber;
        private String address;
        private String phoneNumber;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setRole(String role) {
            this.role = role;
            return this;
        }

        public Builder setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public CustomerDto build() {
            return new CustomerDto(this);
        }

    }
}
