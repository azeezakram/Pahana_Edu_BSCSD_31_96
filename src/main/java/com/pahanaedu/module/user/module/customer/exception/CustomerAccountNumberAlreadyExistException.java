package com.pahanaedu.module.user.module.customer.exception;

public class CustomerAccountNumberAlreadyExistException extends RuntimeException {
    public CustomerAccountNumberAlreadyExistException(String message) {
        super(message);
    }
}
