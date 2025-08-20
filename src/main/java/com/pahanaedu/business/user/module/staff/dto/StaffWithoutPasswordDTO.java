package com.pahanaedu.business.user.module.staff.dto;

import java.time.LocalDateTime;

public record StaffWithoutPasswordDTO(
        Long id,
        String name,
        String role,
        String username,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}


