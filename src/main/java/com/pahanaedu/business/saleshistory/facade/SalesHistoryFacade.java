package com.pahanaedu.business.saleshistory.facade;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.exception.ItemException;
import com.pahanaedu.business.item.service.ItemService;
import com.pahanaedu.business.item.service.ItemServiceImpl;
import com.pahanaedu.business.salesItem.dto.SalesItemDTO;
import com.pahanaedu.business.salesItem.model.SalesItem;
import com.pahanaedu.business.salesItem.service.SalesItemService;
import com.pahanaedu.business.salesItem.service.SalesItemServiceImpl;
import com.pahanaedu.business.saleshistory.dto.SalesHistoryDTO;
import com.pahanaedu.business.saleshistory.exception.SalesHistoryException;
import com.pahanaedu.business.saleshistory.model.SalesHistory;
import com.pahanaedu.business.saleshistory.service.SalesHistoryService;
import com.pahanaedu.business.saleshistory.service.SalesHistoryServiceImpl;
import com.pahanaedu.business.saleshistory.util.SalesHistoryUtils;
import com.pahanaedu.business.user.module.customer.dto.CustomerDTO;
import com.pahanaedu.business.user.module.customer.service.CustomerServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalesHistoryFacade {

    private final SalesHistoryService salesHistoryService;
    private final SalesItemService salesItemService;
    private final ItemService itemService;
    private final CustomerServiceImpl customerService;

    public SalesHistoryFacade(String dbType) {
        this.salesHistoryService = new SalesHistoryServiceImpl(dbType);
        this.salesItemService = new SalesItemServiceImpl(dbType);
        this.itemService = new ItemServiceImpl(dbType);
        this.customerService = new CustomerServiceImpl(dbType);
    }

    public List<SalesHistoryDTO> fetchAllWithItems() {
        List<SalesHistoryDTO> salesHistories = salesHistoryService.findAll();
        if (salesHistories == null || salesHistories.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, CustomerDTO> customerMap = customerService.findAll()
                .stream().collect(Collectors.toMap(CustomerDTO::getId, c -> c));

        Map<Long, List<SalesItemDTO>> salesItemMap = salesItemService.findAll()
                .stream().collect(Collectors.groupingBy(SalesItemDTO::getSalesHistoryId));

        return salesHistories.stream()
                .map(sh -> SalesHistoryDTO.builder()
                        .id(sh.getId())
                        .customer(sh.getCustomer() != null ? customerMap.get(sh.getCustomer().getId()) : null)
                        .sellItems(salesItemMap.getOrDefault(sh.getId(), Collections.emptyList()))
                        .grandTotal(sh.getGrandTotal())
                        .createdAt(sh.getCreatedAt())
                        .build())
                .toList();
    }

    public SalesHistoryDTO createSalesHistory(SalesHistory salesHistory) {
        if (SalesHistoryUtils.isInvalid(salesHistory)) {
            throw new SalesHistoryException("Invalid sales history detail/s provided");
        }

        List<ItemDTO> items = itemService.findAll();
        if (items == null || items.isEmpty()) {
            throw new SalesHistoryException("No items available");
        }

        Map<Long, ItemDTO> itemMap = items.stream()
                .collect(Collectors.toMap(ItemDTO::getId, i -> i));

        int grandTotal = 0;

        for (SalesItem salesItem : salesHistory.getSalesItems()) {
            ItemDTO item = itemMap.get(salesItem.getItemId());
            if (item == null) {
                throw new SalesHistoryException("Item with ID " + salesItem.getItemId() + " not found");
            }

            if (item.getStock() < salesItem.getUnit()) {
                throw new SalesHistoryException(item.getItemName() + " is out of stock. Available: "
                        + item.getStock() + ", Requested: " + salesItem.getUnit());
            }

            salesItem.setSellPrice(item.getPrice());
            salesItem.setSubTotal(salesItem.getSellPrice() * salesItem.getUnit());
            grandTotal += salesItem.getSubTotal();

            try {
                itemService.updateStock(item, salesItem.getUnit());
            } catch (ItemException e) {
                throw new SalesHistoryException("Failed to update stock for "
                        + item.getId() + ": " + e.getMessage());
            }

        }

        salesHistory.setGrandTotal(grandTotal);

        SalesHistoryDTO newSalesHistory = salesHistoryService.create(salesHistory);

        salesHistory.getSalesItems()
                .forEach(salesItem -> salesItem.setSalesHistoryId(newSalesHistory.getId()));

        salesItemService.createBySellHistory(salesHistory);

        return salesHistoryService.findByIdWithItems(newSalesHistory.getId());
    }

}


