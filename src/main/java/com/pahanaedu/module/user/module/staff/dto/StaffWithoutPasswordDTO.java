package com.pahanaedu.module.user.module.staff.dto;

public record StaffWithoutPasswordDTO(Long id, String name, String role, String username, Boolean isActive) {
}
