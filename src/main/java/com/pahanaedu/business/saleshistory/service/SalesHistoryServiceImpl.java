package com.pahanaedu.business.saleshistory.service;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.exception.ItemException;
import com.pahanaedu.business.item.service.ItemService;
import com.pahanaedu.business.item.service.ItemServiceImpl;
import com.pahanaedu.business.salesItem.dto.SalesItemDTO;
import com.pahanaedu.business.saleshistory.dto.SalesHistoryDTO;
import com.pahanaedu.business.saleshistory.exception.SalesHistoryException;
import com.pahanaedu.business.saleshistory.mapper.SalesHistoryMapper;
import com.pahanaedu.business.saleshistory.model.SalesHistory;
import com.pahanaedu.business.saleshistory.util.SalesHistoryUtils;
import com.pahanaedu.business.salesItem.model.SalesItem;
import com.pahanaedu.business.salesItem.service.SalesItemService;
import com.pahanaedu.business.salesItem.service.SalesItemServiceImpl;
import com.pahanaedu.business.user.module.customer.dto.CustomerDTO;
import com.pahanaedu.business.user.module.customer.service.CustomerServiceImpl;
import com.pahanaedu.persistence.saleshistory.SalesHistoryRepository;
import com.pahanaedu.persistence.saleshistory.SalesHistoryRepositoryImpl;

import java.util.*;
import java.util.stream.Collectors;

public class SalesHistoryServiceImpl implements SalesHistoryService {

    private final SalesHistoryRepository salesHistoryRepository;
    private final SalesItemService salesItemService;
    private final ItemService itemService;
    private final CustomerServiceImpl customerService;

    public SalesHistoryServiceImpl() {
        this.salesHistoryRepository = new SalesHistoryRepositoryImpl();
        this.salesItemService = new SalesItemServiceImpl();
        this.itemService = new ItemServiceImpl();
        this.customerService = new CustomerServiceImpl();
    }

    @Override
    public SalesHistoryDTO findById(Long id) {
        SalesHistoryDTO salesHistoryDTO = null;
        SalesHistory salesHistory = salesHistoryRepository.findById(id);

        if (salesHistory != null) {
            CustomerDTO customerDTO = customerService.findById(salesHistory.getCustomerId());
            salesHistoryDTO = SalesHistoryMapper.toSellHistoryDTO(salesHistory, customerDTO);
        }

        return Objects.nonNull(salesHistoryDTO) ? salesHistoryDTO : null;
    }

    @Override
    public SalesHistoryDTO findByIdWithItems(Long id) {
        SalesHistoryDTO salesHistoryDTO = null;
        SalesHistory salesHistory = salesHistoryRepository.findById(id);
        if (salesHistory != null) {
            List<SalesItemDTO> salesItemDTOS = salesItemService.findBySellHistoryId(id);
            System.out.println(salesItemDTOS);
            CustomerDTO customerDTO = customerService.findById(salesHistory.getCustomerId());
            salesHistoryDTO = SalesHistoryMapper.toSellHistoryDTO(salesHistory, customerDTO, salesItemDTOS);
        }
        return Objects.nonNull(salesHistoryDTO) ? salesHistoryDTO : null;
    }


    @Override
    public List<SalesHistoryDTO> findAll() {
        List<SalesHistory> sellHistories = salesHistoryRepository.findAll();
        List<SalesHistoryDTO> salesHistoryDTOS = new ArrayList<>();

        if (sellHistories != null) {
            for (SalesHistory salesHistory : sellHistories) {
                CustomerDTO customerDTO = customerService.findById(salesHistory.getCustomerId());
                salesHistoryDTOS.add(SalesHistoryMapper.toSellHistoryDTO(salesHistory, customerDTO));
            }
        }

        return salesHistoryDTOS;
    }

    @Override
    public List<SalesHistoryDTO> findAllWithItems() {
        List<SalesHistory> sellHistories = salesHistoryRepository.findAll();
        if (sellHistories == null || sellHistories.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> customerIds = sellHistories.stream()
                .map(SalesHistory::getCustomerId)
                .collect(Collectors.toSet());

        Map<Long, CustomerDTO> customerMap = new HashMap<>();
        for (Long customerId : customerIds) {
            CustomerDTO customerDTO = customerService.findById(customerId);
            if (customerDTO != null) {
                customerMap.put(customerId, customerDTO);
            }
        }

        Set<Long> sellHistoryIds = sellHistories.stream()
                .map(SalesHistory::getId)
                .collect(Collectors.toSet());

        Map<Long, List<SalesItemDTO>> sellItemMap = new HashMap<>();
        for (Long sellHistoryId : sellHistoryIds) {
            List<SalesItemDTO> salesItemDTOS = salesItemService.findBySellHistoryId(sellHistoryId);
            sellItemMap.put(sellHistoryId, salesItemDTOS != null ? salesItemDTOS : Collections.emptyList());
        }

        List<SalesHistoryDTO> salesHistoryDTOS = new ArrayList<>();
        for (SalesHistory salesHistory : sellHistories) {
            CustomerDTO customerDTO = customerMap.get(salesHistory.getCustomerId());
            List<SalesItemDTO> salesItemDTOS = sellItemMap.getOrDefault(salesHistory.getId(), Collections.emptyList());
            salesHistoryDTOS.add(SalesHistoryMapper.toSellHistoryDTO(salesHistory, customerDTO, salesItemDTOS));
        }

        return salesHistoryDTOS;
    }

    @Override
    public SalesHistoryDTO create(SalesHistory salesHistory) {
        if (SalesHistoryUtils.isInvalid(salesHistory)) {
            throw new SalesHistoryException("Invalid sales history detail/s provided");
        }

        Integer grandTotal = 0;
        for (SalesItem salesItem : salesHistory.getSalesItems()) {
            ItemDTO item = itemService.findById(salesItem.getItemId());
            if (item == null) {
                throw new SalesHistoryException("Item with ID " + salesItem.getItemId() + " not found");
            }

            if (item.getStock() < salesItem.getUnit()) {
                throw new SalesHistoryException(item.getItemName() + " is out of stock. Available: " + item.getStock() + ", Requested: " + salesItem.getUnit());
            }

            salesItem.setSellPrice(item.getPrice());
            salesItem.setSubTotal(salesItem.getSellPrice() * salesItem.getUnit());
            grandTotal += salesItem.getSubTotal();

            try {
                itemService.updateStock(item, salesItem.getUnit());
            } catch (ItemException e) {
                throw new SalesHistoryException("Failed to update stock for " + item.getId() + ": " + e.getMessage());
            }
        }

        salesHistory.setGrandTotal(grandTotal);

        System.out.println(salesHistory);
        System.out.println(salesHistory.getSalesItems());

        SalesHistory newSalesHistory = salesHistoryRepository.save(salesHistory);
        salesItemService.createBySellHistory(salesHistory);

        return findByIdWithItems(newSalesHistory.getId());
    }

    @Override
    public boolean delete(Long id) {
        boolean isSellItemDeleted = salesItemService.delete(id);
        if (isSellItemDeleted) {
            return salesHistoryRepository.delete(id);
        } else {
            return false;
        }

    }
}
