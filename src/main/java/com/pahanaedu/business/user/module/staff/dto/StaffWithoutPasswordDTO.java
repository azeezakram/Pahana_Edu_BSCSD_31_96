package com.pahanaedu.business.user.module.staff.dto;

public record StaffWithoutPasswordDTO(Long id, String name, String role, String username, Boolean isActive) {
}
