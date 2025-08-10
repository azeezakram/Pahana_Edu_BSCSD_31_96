package com.pahanaedu.business.user.module.staff.service;

import com.pahanaedu.business.user.module.customer.dto.CustomerDTO;
import com.pahanaedu.business.user.module.customer.model.Customer;
import com.pahanaedu.business.user.module.staff.dto.StaffAuthDTO;
import com.pahanaedu.business.user.module.staff.dto.StaffWithoutPasswordDTO;
import com.pahanaedu.business.user.module.staff.model.Staff;
import com.pahanaedu.common.interfaces.Service;
import jakarta.servlet.http.HttpServletRequest;

public interface StaffService extends Service<Staff, StaffWithoutPasswordDTO> {
    StaffWithoutPasswordDTO findByUsername(String username);
    StaffWithoutPasswordDTO update(Staff staff);
    boolean login(StaffAuthDTO auth, HttpServletRequest req);
}
