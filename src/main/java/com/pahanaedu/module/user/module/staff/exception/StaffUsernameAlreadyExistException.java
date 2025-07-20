package com.pahanaedu.module.user.module.staff.exception;

public class StaffUsernameAlreadyExistException extends RuntimeException {
    public StaffUsernameAlreadyExistException(String message) {
        super(message);
    }
}
