package com.pahanaedu.business.user.module.staff.mapper;


import com.pahanaedu.business.user.module.staff.dto.StaffAuthorizationDTO;
import com.pahanaedu.business.user.module.staff.dto.StaffCreationDTO;
import com.pahanaedu.business.user.module.staff.dto.StaffRequestAuthorizationDTO;
import com.pahanaedu.business.user.module.staff.dto.StaffWithoutPasswordDTO;
import com.pahanaedu.business.user.module.staff.model.Staff;

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

    public static StaffCreationDTO toStaffCreationDTO(Staff staff) {
        return new StaffCreationDTO(staff.getName(), staff.getUsername(), staff.getPassword());
    }

    public static Staff toStaff(StaffCreationDTO staffCreationDTO) {
        return new Staff(staffCreationDTO.name(), staffCreationDTO.username(), staffCreationDTO.password());
    }

}
