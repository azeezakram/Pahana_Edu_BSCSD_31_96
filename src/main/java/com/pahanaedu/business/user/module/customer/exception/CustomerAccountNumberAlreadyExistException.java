package com.pahanaedu.business.user.module.customer.exception;

public class CustomerAccountNumberAlreadyExistException extends RuntimeException {
    public CustomerAccountNumberAlreadyExistException(String message) {
        super(message);
    }
}
