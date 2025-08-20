package com.pahanaedu.business.user.module.staff.service;

import com.pahanaedu.business.user.module.staff.dto.StaffWithoutPasswordDTO;
import com.pahanaedu.business.user.module.staff.exception.StaffUsernameAlreadyExistException;
import com.pahanaedu.business.user.module.staff.model.Staff;
import com.pahanaedu.config.db.init.TestDbInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StaffServiceImplTest {

    private StaffServiceImpl staffService;
    private static final String DATABASE_TYPE = "test";

    @BeforeEach
    void setUp() {
        staffService = new StaffServiceImpl(DATABASE_TYPE);
        TestDbInitializer.initializeTestDb();
    }

    @Test
    void testCreateStaff_success() {
        Staff staff = new Staff("John Doe", "john_doe", "password123");

        StaffWithoutPasswordDTO created = staffService.create(staff);

        assertNotNull(created);
        assertEquals("john_doe", created.username());
    }

    @Test
    void testCreateStaff_duplicateUsername() {
        Staff staff1 = new Staff("Alice", "alice", "pass1");
        staffService.create(staff1);

        Staff staff2 = new Staff();
        staff2.setName("Alice");
        staff2.setUsername("alice");
        staff2.setPassword("pass2");

        assertThrows(StaffUsernameAlreadyExistException.class, () -> staffService.create(staff2));
    }

    @Test
    void testFindById_and_FindByUsername() {
        Staff staff = new Staff();
        staff.setName("Bob");
        staff.setUsername("bob");
        staff.setPassword("secret");
        StaffWithoutPasswordDTO created = staffService.create(staff);

        StaffWithoutPasswordDTO byId = staffService.findById(created.id());
        assertNotNull(byId);
        assertEquals("bob", byId.username());

        StaffWithoutPasswordDTO byUsername = staffService.findByUsername("bob");
        assertNotNull(byUsername);
        assertEquals(created.id(), byUsername.id());
    }

    @Test
    void testFindAll() {
        staffService.create(new Staff("User 1", "user1", "pass1"));
        staffService.create(new Staff("User 2", "user2", "pass2"));

        List<StaffWithoutPasswordDTO> allStaff = staffService.findAll();
        assertNotNull(allStaff);
        assertTrue(allStaff.size() >= 2);
    }

    @Test
    void testUpdateStaff_success() {
        Staff staff = new Staff("Charlie", "charlie", "pass123");
        StaffWithoutPasswordDTO created = staffService.create(staff);

        Staff update = new Staff();
        update.setId(created.id());
        update.setName("Charlie the will");
        update.setUsername("charlie_updated");
        update.setIsActive(false);
        update.setPassword("");

        StaffWithoutPasswordDTO updated = staffService.update(update);

        assertEquals("charlie_updated", updated.username());
        assertFalse(updated.isActive());
    }

    @Test
    void testDeleteStaff() {
        Staff staff = new Staff("To delete", "to_delete", "pass");
        StaffWithoutPasswordDTO created = staffService.create(staff);

        boolean deleted = staffService.delete(created.id());
        assertTrue(deleted);

        StaffWithoutPasswordDTO afterDelete = staffService.findById(created.id());
        assertNull(afterDelete);
    }
}
