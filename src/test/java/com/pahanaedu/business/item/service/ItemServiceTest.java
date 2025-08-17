package com.pahanaedu.business.item.service;

import com.pahanaedu.business.category.dto.CategoryDTO;
import com.pahanaedu.business.category.model.Category;
import com.pahanaedu.business.category.service.CategoryService;
import com.pahanaedu.business.category.service.CategoryServiceImpl;
import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.exception.ItemException;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.config.db.init.TestDbInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceImplTest {

    private ItemServiceImpl itemService;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        TestDbInitializer.initializeTestDb();
        this.itemService = new ItemServiceImpl("test");
        this.categoryService = new CategoryServiceImpl("test");
    }

    @Test
    void testCreateItem_success() {
        CategoryDTO createdCategory = categoryService.create(new Category(null, "Electronics"));
        Item item = new Item();
        item.setItemName("Laptop");
        item.setBrand("Lenovo");
        item.setPrice(1500);
        item.setStock(10);
        item.setCategory(new Category(createdCategory.id(), createdCategory.categoryName()));

        ItemDTO created = itemService.create(item);

        assertNotNull(created);
        assertEquals("Laptop", created.getItemName());
        assertEquals(10, created.getStock());
        assertEquals("Electronics", created.getCategory().getCategoryName());
    }

    @Test
    void testCreateItem_invalidItem() {
        Item item = new Item();
        assertThrows(ItemException.class, () -> itemService.create(item));
    }

    @Test
    void testFindById() {
        CategoryDTO createdCategory = categoryService.create(new Category(null, "Electronics"));
        Item item = new Item("Phone", 500, 20);
        item.setCategory(new Category(createdCategory.id(), createdCategory.categoryName()));
        ItemDTO created = itemService.create(item);

        ItemDTO found = itemService.findById(created.getId());

        assertNotNull(found);
        assertEquals("Phone", found.getItemName());
        assertEquals(20, found.getStock());
    }

    @Test
    void testFindAll() {
        CategoryDTO createdCategory = categoryService.create(new Category(null, "Electronics"));

        Item item1 = new Item("Item1", 100, 5);
        item1.setCategory( new Category(createdCategory.id(), createdCategory.categoryName()));

        Item item2 = new Item("Item2", 200, 15);
        item2.setCategory( new Category(createdCategory.id(), createdCategory.categoryName()));

        itemService.create(item1);
        itemService.create(item2);

        List<ItemDTO> allItems = itemService.findAll();
        assertNotNull(allItems);
        assertTrue(allItems.size() >= 2);
    }

    @Test
    void testUpdateItem_success() {
        CategoryDTO createdCategory = categoryService.create(new Category(null, "Electronics"));
        Item item = new Item("Tablet", 300, 8);
        item.setCategory(new Category(createdCategory.id(), createdCategory.categoryName()));
        ItemDTO created = itemService.create(item);

        Item update = new Item();
        update.setId(created.getId());
        update.setItemName("Tablet Pro");
        update.setPrice(350);
        update.setStock(10);
        update.setCategory(item.getCategory());

        ItemDTO updated = itemService.update(update);

        assertEquals("Tablet Pro", updated.getItemName());
        assertEquals(10, updated.getStock());
    }

    @Test
    void testDeleteItem() {
        CategoryDTO createdCategory = categoryService.create(new Category(null, "Electronics"));
        Item item = new Item("ToDelete", 50, 3);
        item.setCategory(new Category(createdCategory.id(), createdCategory.categoryName()));
        ItemDTO created = itemService.create(item);

        boolean deleted = itemService.delete(created.getId());
        assertTrue(deleted);

        ItemDTO afterDelete = itemService.findById(created.getId());
        assertNull(afterDelete);
    }

    @Test
    void testUpdateStock_success() {
        CategoryDTO createdCategory = categoryService.create(new Category(null, "Electronics"));
        Item item = new Item("Keyboard", 20, 15);
        item.setCategory(new Category(createdCategory.id(), createdCategory.categoryName()));
        ItemDTO created = itemService.create(item);

        itemService.updateStock(created, 5);

        ItemDTO updated = itemService.findById(created.getId());
        assertEquals(10, updated.getStock());
    }

    @Test
    void testUpdateStock_invalid() {
        ItemDTO invalidItem = new
                ItemDTO.Builder()
                .id(null)
                .itemName(null)
                .price(null)
                .stock(null)
                .build();

        assertThrows(ItemException.class, () -> itemService.updateStock(invalidItem, 5));
    }
}
