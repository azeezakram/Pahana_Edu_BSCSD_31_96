package com.pahanaedu.persistence.user.staff;

import com.pahanaedu.business.user.module.staff.dto.StaffWithoutPasswordDTO;
import com.pahanaedu.business.user.module.staff.model.Staff;
import com.pahanaedu.common.interfaces.Repository;

public interface StaffRepository extends Repository<Staff> {
    Staff findByUsername(String username);
    Staff update(Staff staff);
}
