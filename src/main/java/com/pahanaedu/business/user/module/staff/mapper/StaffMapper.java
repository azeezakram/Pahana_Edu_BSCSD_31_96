package com.pahanaedu.business.user.module.staff.mapper;


import com.pahanaedu.business.user.module.staff.dto.StaffWithoutPasswordDTO;
import com.pahanaedu.business.user.module.staff.model.Staff;

public class StaffMapper {

    public static StaffWithoutPasswordDTO toStaffWithoutPasswordDTO(Staff staff) {
        return new StaffWithoutPasswordDTO(staff.getId(), staff.getName(), staff.getRole(), staff.getUsername(), staff.getIsActive(), staff.getCreatedAt(), staff.getUpdatedAt());
    }

}
