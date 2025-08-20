package com.pahanaedu.business.saleshistory.service;

import com.pahanaedu.business.category.dto.CategoryDTO;
import com.pahanaedu.business.category.model.Category;
import com.pahanaedu.business.category.service.CategoryService;
import com.pahanaedu.business.category.service.CategoryServiceImpl;
import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.business.item.service.ItemService;
import com.pahanaedu.business.item.service.ItemServiceImpl;
import com.pahanaedu.business.salesItem.model.SalesItem;
import com.pahanaedu.business.saleshistory.facade.SalesHistoryFacade;
import com.pahanaedu.business.saleshistory.model.SalesHistory;
import com.pahanaedu.business.user.module.customer.dto.CustomerDTO;
import com.pahanaedu.business.user.module.customer.model.Customer;
import com.pahanaedu.business.user.module.customer.service.CustomerService;
import com.pahanaedu.business.user.module.customer.service.CustomerServiceImpl;
import com.pahanaedu.config.db.init.TestDbInitializer;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalesHistoryServiceImplTest {
    private SalesHistoryFacade salesHistoryFacade;
    private SalesHistoryService salesHistoryService;
    private ItemService itemService;
    private CustomerService customerService;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        TestDbInitializer.initializeTestDb();
        this.salesHistoryFacade = new SalesHistoryFacade("test");
        this.salesHistoryService = new SalesHistoryServiceImpl("test");
        this.itemService = new ItemServiceImpl("test");
        this.customerService = new CustomerServiceImpl("test");
        this.categoryService = new CategoryServiceImpl("test");
    }

    @Test
    void testCreateSalesHistory_success() {
        CategoryDTO category = categoryService.create(new Category(null, "Electronics"));
        Item item1 = new Item("Laptop", 1500, 10);
        item1.setCategory(new Category(category.id(), category.categoryName()));
        ItemDTO createdItem1 = itemService.create(item1);

        Item item2 = new Item("Mouse", 50, 20);
        item2.setCategory(new Category(category.id(), category.categoryName()));
        ItemDTO createdItem2 = itemService.create(item2);
        CustomerDTO createdCustomer = customerService.create(new Customer("John Doe", "johndoe1",
                "123 Main St", "1234567890"));

        SalesItem salesItem = new SalesItem();
        salesItem.setItemId(createdItem1.getId());
        salesItem.setUnit(2);
        salesItem.setSubTotal(item1.getPrice() * salesItem.getUnit());

        SalesItem salesItem2 = new SalesItem();
        salesItem2.setItemId(createdItem2.getId());
        salesItem2.setUnit(5);
        salesItem2.setSubTotal(item2.getPrice() * salesItem2.getUnit());

        List<SalesItem> salesItems = new ArrayList<>();
        salesItems.add(salesItem);
        salesItems.add(salesItem2);

        SalesHistory salesHistory = new SalesHistory();
        salesHistory.setCustomerId(createdCustomer.getId());
        salesHistory.setSalesItems(salesItems);
        salesHistory.setGrandTotal(salesItem.getSubTotal() + salesItem2.getSubTotal());

        var created = salesHistoryFacade.createSalesHistory(salesHistory);

        assertNotNull(created);
        assertEquals(createdCustomer.getId(), created.getCustomer().getId());
        assertEquals(2, created.getSalesItems().size());

        int expectedGrandTotal = (2 * 1500) + (5 * 50);
        assertEquals(expectedGrandTotal, created.getGrandTotal());

        assertEquals(8, itemService.findById(createdItem1.getId()).getStock());
        assertEquals(15, itemService.findById(createdItem2.getId()).getStock());
    }

    @Test
    void testCreateSalesHistory_itemOutOfStock() {
        CategoryDTO category = categoryService.create(new Category(null, "Electronics"));
        Item item = new Item("Laptop", 1500, 10);
        item.setCategory(new Category(category.id(), category.categoryName()));
        ItemDTO createdItem = itemService.create(item);

        SalesItem salesItem = new SalesItem();
        salesItem.setItemId(createdItem.getId());
        salesItem.setUnit(100);

        CustomerDTO createdCustomer = customerService.create(new Customer("John Doe", "johndoe2",
                "123 Main St", "1234567890"));

        SalesHistory salesHistory = new SalesHistory();
        salesHistory.setCustomerId(createdCustomer.getId());
        salesHistory.setSalesItems(List.of(salesItem));

        assertThrows(RuntimeException.class, () -> salesHistoryService.create(salesHistory));
    }

    @Test
    void testFindByIdAndFindAll() {
        CategoryDTO category = categoryService.create(new Category(null, "Electronics"));
        Item item = new Item("Laptop", 1500, 10);
        item.setCategory(new Category(category.id(), category.categoryName()));
        ItemDTO createdItem = itemService.create(item);
        CustomerDTO createdCustomer = customerService.create(new Customer("John Doe", "johndoe3",
                "123 Main St", "1234567890"));

        SalesItem sellItem = new SalesItem();
        sellItem.setItemId(createdItem.getId());
        sellItem.setUnit(1);

        SalesHistory salesHistory = new SalesHistory();
        salesHistory.setCustomerId(createdCustomer.getId());
        salesHistory.setSalesItems(List.of(sellItem));
        salesHistory.setGrandTotal(546);

        var created = salesHistoryFacade.createSalesHistory(salesHistory);

        var found = salesHistoryService.findById(created.getId());
        assertNotNull(found);
        assertEquals(created.getId(), found.getId());

        var all = salesHistoryService.findAll();
        assertFalse(all.isEmpty());
    }

    @Test
    void testDeleteSalesHistory() {
        CategoryDTO category = categoryService.create(new Category(null, "Electronics"));
        Item item = new Item("Laptop", 1500, 10);
        item.setCategory(new Category(category.id(), category.categoryName()));
        ItemDTO createdItem = itemService.create(item);
        CustomerDTO createdCustomer = customerService.create(new Customer("John Doe", "johndoe4",
                "123 Main St", "1234567890"));

        SalesItem salesItem = new SalesItem();
        salesItem.setItemId(createdItem.getId());
        salesItem.setUnit(1);

        SalesHistory salesHistory = new SalesHistory();
        salesHistory.setCustomerId(createdCustomer.getId());
        salesHistory.setSalesItems(List.of(salesItem));
        salesHistory.setGrandTotal(546);

        var created = salesHistoryFacade.createSalesHistory(salesHistory);
        boolean deleted = salesHistoryService.delete(created.getId());
        assertTrue(deleted);

        var afterDelete = salesHistoryService.findById(created.getId());
        assertNull(afterDelete);
    }
}
