package com.pahanaedu.business.sellHistory.service;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.exception.ItemException;
import com.pahanaedu.business.item.service.ItemService;
import com.pahanaedu.business.item.service.ItemServiceImpl;
import com.pahanaedu.business.sellHistory.dto.SellHistoryDTO;
import com.pahanaedu.business.sellHistory.exception.SellHistoryException;
import com.pahanaedu.business.sellHistory.mapper.SellHistoryMapper;
import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellHistory.util.SellHistoryUtils;
import com.pahanaedu.business.sellItem.dto.SellItemDTO;
import com.pahanaedu.business.sellItem.model.SellItem;
import com.pahanaedu.business.sellItem.service.SellItemService;
import com.pahanaedu.business.sellItem.service.SellItemServiceImpl;
import com.pahanaedu.business.user.module.customer.dto.CustomerDTO;
import com.pahanaedu.business.user.module.customer.service.CustomerServiceImpl;
import com.pahanaedu.common.interfaces.Service;
import com.pahanaedu.persistence.sellhistory.SellHistoryRepository;
import com.pahanaedu.persistence.sellhistory.SellHistoryRepositoryImpl;

import java.util.*;
import java.util.stream.Collectors;

public class SellHistoryServiceImpl implements SellHistoryService {

    private final SellHistoryRepository sellHistoryRepository;
    private final SellItemService sellItemService;
    private final ItemService itemService;
    private final CustomerServiceImpl customerService;

    public SellHistoryServiceImpl() {
        this.sellHistoryRepository = new SellHistoryRepositoryImpl();
        this.sellItemService = new SellItemServiceImpl();
        this.itemService = new ItemServiceImpl();
        this.customerService = new CustomerServiceImpl();
    }

    @Override
    public SellHistoryDTO findById(Long id) {
        SellHistoryDTO sellHistoryDTO = null;
        SellHistory sellHistory = sellHistoryRepository.findById(id);

        if (sellHistory != null) {
            CustomerDTO customerDTO = customerService.findById(sellHistory.getCustomerId());
            sellHistoryDTO = SellHistoryMapper.toSellHistoryDTO(sellHistory, customerDTO);
        }

        return Objects.nonNull(sellHistoryDTO) ? sellHistoryDTO : null;
    }

    @Override
    public SellHistoryDTO findByIdWithItems(Long id) {
        SellHistoryDTO sellHistoryDTO = null;
        SellHistory sellHistory = sellHistoryRepository.findById(id);
        if (sellHistory != null) {
            List<SellItemDTO> sellItemDTOS = sellItemService.findBySellHistoryId(id);
            System.out.println(sellItemDTOS);
            CustomerDTO customerDTO = customerService.findById(sellHistory.getCustomerId());
            sellHistoryDTO = SellHistoryMapper.toSellHistoryDTO(sellHistory, customerDTO, sellItemDTOS);
        }
        return Objects.nonNull(sellHistoryDTO) ? sellHistoryDTO : null;
    }


    @Override
    public List<SellHistoryDTO> findAll() {
        List<SellHistory> sellHistories = sellHistoryRepository.findAll();
        List<SellHistoryDTO> sellHistoryDTOS = new ArrayList<>();

        if (sellHistories != null) {
            for (SellHistory sellHistory : sellHistories) {
                CustomerDTO customerDTO = customerService.findById(sellHistory.getCustomerId());
                sellHistoryDTOS.add(SellHistoryMapper.toSellHistoryDTO(sellHistory, customerDTO));
            }
        }

        return sellHistoryDTOS ;
    }

    @Override
    public List<SellHistoryDTO> findAllWithItems() {
        List<SellHistory> sellHistories = sellHistoryRepository.findAll();
        if (sellHistories == null || sellHistories.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> customerIds = sellHistories.stream()
                .map(SellHistory::getCustomerId)
                .collect(Collectors.toSet());

        Map<Long, CustomerDTO> customerMap = new HashMap<>();
        for (Long customerId : customerIds) {
            CustomerDTO customerDTO = customerService.findById(customerId);
            if (customerDTO != null) {
                customerMap.put(customerId, customerDTO);
            }
        }

        Set<Long> sellHistoryIds = sellHistories.stream()
                .map(SellHistory::getId)
                .collect(Collectors.toSet());

        Map<Long, List<SellItemDTO>> sellItemMap = new HashMap<>();
        for (Long sellHistoryId : sellHistoryIds) {
            List<SellItemDTO> sellItems = sellItemService.findBySellHistoryId(sellHistoryId);
            sellItemMap.put(sellHistoryId, sellItems != null ? sellItems : Collections.emptyList());
        }

        List<SellHistoryDTO> sellHistoryDTOS = new ArrayList<>();
        for (SellHistory sellHistory : sellHistories) {
            CustomerDTO customerDTO = customerMap.get(sellHistory.getCustomerId());
            List<SellItemDTO> sellItemDTOs = sellItemMap.getOrDefault(sellHistory.getId(), Collections.emptyList());
            sellHistoryDTOS.add(SellHistoryMapper.toSellHistoryDTO(sellHistory, customerDTO, sellItemDTOs));
        }

        return sellHistoryDTOS;
    }

    @Override
    public SellHistoryDTO create(SellHistory sellHistory) {
        if (SellHistoryUtils.isInvalid(sellHistory)) {
            throw new SellHistoryException("Invalid sell history detail/s provided");
        }

        Integer grandTotal = 0;
        for (SellItem sellItem : sellHistory.getSellItems()) {
            ItemDTO item = itemService.findById(sellItem.getItemId());
            if (item == null) {
                throw new SellHistoryException("Item with ID " + sellItem.getItemId() + " not found");
            }

            if (item.getStock() < sellItem.getUnit()) {
                throw new SellHistoryException(item.getItemName() + " is out of stock. Available: " + item.getStock() + ", Requested: " + sellItem.getUnit());
            }

            sellItem.setSellPrice(item.getPrice());
            sellItem.setSubTotal(sellItem.getSellPrice() * sellItem.getUnit());
            grandTotal += sellItem.getSubTotal();

            try {
                itemService.updateStock(item, sellItem.getUnit());
            } catch (ItemException e) {
                throw new SellHistoryException("Failed to update stock for " + item.getId() + ": " + e.getMessage());
            }
        }

        sellHistory.setGrandTotal(grandTotal);

        System.out.println(sellHistory);
        System.out.println(sellHistory.getSellItems());

        SellHistory newSellHistory = sellHistoryRepository.save(sellHistory);
        sellItemService.createBySellHistory(sellHistory);

        return findByIdWithItems(newSellHistory.getId());
    }

    @Override
    public boolean delete(Long id) {
        boolean isSellItemDeleted = sellItemService.delete(id);

        if (isSellItemDeleted) {
            return sellHistoryRepository.delete(id);
        } else {
            return false;
        }

    }
}
