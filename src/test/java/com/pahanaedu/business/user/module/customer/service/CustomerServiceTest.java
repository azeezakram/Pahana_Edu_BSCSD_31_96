package com.pahanaedu.business.user.module.customer.service;

import com.pahanaedu.business.user.module.customer.dto.CustomerDTO;
import com.pahanaedu.business.user.module.customer.exception.CustomerAccountNumberAlreadyExistException;
import com.pahanaedu.business.user.module.customer.model.Customer;
import com.pahanaedu.config.db.init.TestDbInitializer;
import org.junit.jupiter.api.*;

        import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceImplTest {

    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() throws Exception {
        TestDbInitializer.initializeTestDb();
        customerService = new CustomerServiceImpl("test");
    }

    @Test
    void testCreateCustomer_success() {
        Customer customer = new Customer("customer 1", "CUST-001", "123 Main St", "1234567890");

        var created = customerService.create(customer);

        assertNotNull(created);
        assertEquals("CUST-001", created.getAccountNumber());
        assertEquals("123 Main St", created.getAddress());
    }

    @Test
    void testCreateCustomer_duplicateAccountNumber() {
        Customer customer = new Customer("customer 2", "CUST-002", "123 Main St", "1234567890");
        CustomerDTO createdCustomer = customerService.create(customer);

        Customer customer2 = new Customer("customer 3", "CUST-002", "Address 2", "22222");

        assertThrows(CustomerAccountNumberAlreadyExistException.class, () -> customerService.create(customer2));
    }

    @Test
    void testFindById_and_FindByAccountNumber() {
        Customer customer = new Customer("customer 3", "CUST-003", "Street 3", "33333");

        var created = customerService.create(customer);

        var foundById = customerService.findById(created.getId());
        assertNotNull(foundById);
        assertEquals("CUST-003", foundById.getAccountNumber());

        var foundByAccountNumber = customerService.findByAccountNumber("CUST-003");
        assertNotNull(foundByAccountNumber);
        assertEquals(created.getId(), foundByAccountNumber.getId());
    }

    @Test
    void testFindAll() {
        customerService.create(new Customer("Customer 4", "CUST-004", "Addr 4", "44444"));
        customerService.create(new Customer("Customer 5", "CUST-005", "Addr 5", "55555"));

        List<CustomerDTO> allCustomers = customerService.findAll();
        assertNotNull(allCustomers);
        assertTrue(allCustomers.size() >= 2);
    }

    @Test
    void testUpdateCustomer_success() {
        Customer customer = new Customer("customer 6", "CUST-006", "Old Address", "66666");
        var created = customerService.create(customer);

        var update = new Customer("Customer 66", "CUST-006", "New Address", "66666");
        update.setId(created.getId());

        var updated = customerService.update(update);
        assertEquals("New Address", updated.getAddress());
    }

    @Test
    void testDeleteCustomer() {
        Customer customer = new Customer("Customer 7", "CUST-007", "Address 7", "77777");

        var created = customerService.create(customer);

        boolean deleted = customerService.delete(created.getId());
        assertTrue(deleted);

        var afterDelete = customerService.findById(created.getId());
        assertNull(afterDelete);
    }

}
