package com.pahanaedu.business.user.module.staff.service;

import com.pahanaedu.business.user.module.staff.dto.StaffAuthDTO;
import com.pahanaedu.business.user.module.staff.dto.StaffWithoutPasswordDTO;
import com.pahanaedu.business.user.module.staff.exception.StaffUsernameAlreadyExistException;
import com.pahanaedu.business.user.module.staff.mapper.StaffMapper;
import com.pahanaedu.business.user.module.staff.model.Staff;
import com.pahanaedu.business.user.module.staff.util.StaffUtils;
import com.pahanaedu.persistence.user.staff.StaffRepository;
import com.pahanaedu.persistence.user.staff.StaffRepositoryImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Objects;

public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

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

    @Override
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
        return findById(newStaff.getId());

    }

    @Override
    public StaffWithoutPasswordDTO update(Staff staff) {

        Staff usernameCheck = staffRepository.findByUsername(staff.getUsername());
        if (usernameCheck != null && !usernameCheck.getId().equals(staff.getId())) {
            throw new StaffUsernameAlreadyExistException("Username already taken by another staff");
        }

        if (usernameCheck != null && usernameCheck.getPassword() != null && staff.getPassword() == null) {
            staff.setPassword(usernameCheck.getPassword());
        } else {
            Staff passwordData = staffRepository.findById(staff.getId());
            staff.setPassword(passwordData.getPassword());
        }

        Staff updatedStaff = staffRepository.update(staff);
        return findById(updatedStaff.getId());

    }

    @Override
    public boolean delete(Long id) {
        return staffRepository.delete(id);
    }

    @Override
    public boolean login(StaffAuthDTO auth, HttpServletRequest req) {
        Staff staff = staffRepository.findByUsername(auth.username());

        if (staff != null) {
            String hashedPassword = StaffUtils.hashPassword(auth.password());
            if (staff.getPassword().equals(hashedPassword)) {
                HttpSession session = req.getSession(true);
                session.setAttribute("staff", auth.username());
                return true;
            }
        }
        return false;
    }
}
