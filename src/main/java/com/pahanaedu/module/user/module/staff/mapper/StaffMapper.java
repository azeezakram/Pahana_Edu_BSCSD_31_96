package com.pahanaedu.module.user.module.staff.mapper;


import com.pahanaedu.module.user.module.staff.dto.StaffAuthorizationDTO;
import com.pahanaedu.module.user.module.staff.dto.StaffRequestAuthorizationDTO;
import com.pahanaedu.module.user.module.staff.dto.StaffWithoutPasswordDTO;
import com.pahanaedu.module.user.module.staff.model.Staff;

public class StaffMapper {

    public static StaffAuthorizationDTO toStaffAuthorizationDTO(Staff staff) {
        return new StaffAuthorizationDTO(staff.getId(), staff.getUsername(), staff.getPassword());
    }

    public static StaffRequestAuthorizationDTO toStaffRequestAuthorizationDTO(Staff staff) {
        return new StaffRequestAuthorizationDTO(staff.getId(), staff.getUsername());
    }

    public static StaffWithoutPasswordDTO toStaffWithoutPasswordDTO(Staff staff) {
        return new StaffWithoutPasswordDTO(staff.getId(), staff.getName(), staff.getRole(), staff.getUsername(), staff.getIsActive());
    }

}
