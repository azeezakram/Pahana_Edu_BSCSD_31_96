package com.pahanaedu.module.user.module.staff.service;

import com.pahanaedu.common.interfaces.IServicePrototype;
import com.pahanaedu.module.user.module.staff.dto.StaffWithoutPasswordDTO;
import com.pahanaedu.module.user.module.staff.mapper.StaffMapper;
import com.pahanaedu.module.user.module.staff.model.Staff;
import com.pahanaedu.module.user.module.staff.repository.StaffRepository;

import java.util.List;
import java.util.Objects;

public class StaffService implements IServicePrototype<StaffWithoutPasswordDTO> {

    private final StaffRepository staffRepository;

    public StaffService() {
        this.staffRepository = new StaffRepository();
    }


    @Override
    public StaffWithoutPasswordDTO findById(Long id) {
        Staff staff = staffRepository.findById(id);

        return staff != null ? StaffMapper.toStaffWithoutPasswordDTO(staff) : null;
    }

    @Override
    public List<StaffWithoutPasswordDTO> findAll() {

        List<Staff> staffs = staffRepository.findAll();

        return staffs != null ? staffs
                .stream()
                .map(staff -> StaffMapper.toStaffWithoutPasswordDTO(staff))
                .toList() : null;

    }

    public StaffWithoutPasswordDTO findByUsername(String username) {

        Staff staff = staffRepository.findByUsername(username);

        return Objects.nonNull(staff) ? StaffMapper.toStaffWithoutPasswordDTO(staff) : null;
    }

    @Override
    public StaffWithoutPasswordDTO create(StaffWithoutPasswordDTO obj) {
        return null;
    }

    @Override
    public StaffWithoutPasswordDTO update(StaffWithoutPasswordDTO obj) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return true;
    }
}
