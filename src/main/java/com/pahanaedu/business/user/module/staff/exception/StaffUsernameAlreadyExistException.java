package com.pahanaedu.business.user.module.staff.exception;

public class StaffUsernameAlreadyExistException extends RuntimeException {
    public StaffUsernameAlreadyExistException(String message) {
        super(message);
    }
}
