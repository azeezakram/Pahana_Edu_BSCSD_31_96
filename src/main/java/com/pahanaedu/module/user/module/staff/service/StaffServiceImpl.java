package com.pahanaedu.module.user.module.staff.service;

import com.pahanaedu.common.interfaces.IServicePrototype;
import com.pahanaedu.common.utill.JsonUtil;
import com.pahanaedu.module.user.module.staff.dto.StaffWithoutPasswordDTO;
import com.pahanaedu.module.user.module.staff.exception.StaffUsernameAlreadyExistException;
import com.pahanaedu.module.user.module.staff.mapper.StaffMapper;
import com.pahanaedu.module.user.module.staff.model.Staff;
import com.pahanaedu.module.user.module.staff.repository.StaffRepositoryImpl;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StaffServiceImpl implements IServicePrototype<Staff, StaffWithoutPasswordDTO> {

    private final StaffRepositoryImpl staffRepository;

    public StaffServiceImpl() {
        this.staffRepository = new StaffRepositoryImpl();
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
                .map(StaffMapper::toStaffWithoutPasswordDTO)
                .toList() : null;

    }

    public StaffWithoutPasswordDTO findByUsername(String username) {

        Staff staff = staffRepository.findByUsername(username);
        return Objects.nonNull(staff) ? StaffMapper.toStaffWithoutPasswordDTO(staff) : null;

    }

    @Override
    public StaffWithoutPasswordDTO create(Staff staff) {

        StaffWithoutPasswordDTO existing = findByUsername(staff.getUsername());
        if (existing != null) {
            throw new StaffUsernameAlreadyExistException("Username already taken by another staff");
        }

        Staff newStaff = staffRepository.save(staff);
        return StaffMapper.toStaffWithoutPasswordDTO(newStaff);

    }

    @Override
    public StaffWithoutPasswordDTO update(Staff staff) {

        StaffWithoutPasswordDTO usernameCheck = findByUsername(staff.getUsername());
        if (usernameCheck != null && !usernameCheck.id().equals(staff.getId())) {
            throw new StaffUsernameAlreadyExistException("Username already taken by another staff");
        }

        Staff updatedStaff = staffRepository.update(staff);
        return StaffMapper.toStaffWithoutPasswordDTO(updatedStaff);

    }

    @Override
    public boolean delete(Long id) {

        return staffRepository.delete(id);

    }

}
