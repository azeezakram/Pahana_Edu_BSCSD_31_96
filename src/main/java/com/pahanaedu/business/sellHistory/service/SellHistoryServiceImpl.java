package com.pahanaedu.business.sellHistory.service;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.exception.ItemException;
import com.pahanaedu.business.item.service.ItemServiceImpl;
import com.pahanaedu.business.sellHistory.dto.SellHistoryDTO;
import com.pahanaedu.business.sellHistory.exception.SellHistoryException;
import com.pahanaedu.business.sellHistory.mapper.SellHistoryMapper;
import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellHistory.util.SellHistoryUtils;
import com.pahanaedu.business.sellItem.model.SellItem;
import com.pahanaedu.common.interfaces.Service;
import com.pahanaedu.persistence.sellhistory.SellHistoryRepositoryImpl;

import java.util.List;
import java.util.Objects;

public class SellHistoryServiceImpl implements Service<SellHistory, SellHistoryDTO> {

    private final SellHistoryRepositoryImpl sellHistoryRepository;
    private final ItemServiceImpl itemService;

    public SellHistoryServiceImpl() {
        this.sellHistoryRepository = new SellHistoryRepositoryImpl();
        this.itemService = new ItemServiceImpl();
    }

    @Override
    public SellHistoryDTO findById(Long id) {
        SellHistory sellHistory = sellHistoryRepository.findById(id);
        return Objects.nonNull(sellHistory) ? SellHistoryMapper.toSellHistoryDTO(sellHistory) : null;
    }

    @Override
    public List<SellHistoryDTO> findAll() {
        List<SellHistory> items = sellHistoryRepository.findAll();

        return !items.isEmpty() ? items.stream()
                .map(SellHistoryMapper::toSellHistoryDTO)
                .toList() : null;
    }

    @Override
    public SellHistoryDTO create(SellHistory sellHistory) {
        if (SellHistoryUtils.isInvalid(sellHistory)) {
            throw new SellHistoryException("Invalid sell history detail/s provided");
        }

        Integer grandTotal = 0;
        for (SellItem sellItem : sellHistory.getSellItems()) {
            ItemDTO item = itemService.findById(sellItem.getItem().getId());
            if (item == null) {
                throw new SellHistoryException("Item with ID " + sellItem.getItem().getId() + " not found");
            }

            if (item.getStock() < sellItem.getUnit()) {
                throw new SellHistoryException(item.getItemName() + " is out of stock. Available: " + item.getStock() + ", Requested: " + sellItem.getUnit());
            }

            sellItem.setSellPrice(item.getPrice());
            sellItem.setSubTotal(sellItem.getSellPrice() * sellItem.getUnit());
            grandTotal += sellItem.getSubTotal();

            try {
                itemService.updateStock(item.getId(), item.getStock(), sellItem.getUnit());
            } catch (ItemException e) {
                throw new SellHistoryException("Failed to update stock for " + item.getId() + ": " + e.getMessage());
            }
        }

        sellHistory.setGrandTotal(grandTotal);

        System.out.println(sellHistory);
        System.out.println(sellHistory.getSellItems());

        SellHistory newSellHistory = sellHistoryRepository.save(sellHistory);
        return findById(newSellHistory.getId());
    }

    @Override
    public SellHistoryDTO update(SellHistory sellHistory) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return sellHistoryRepository.delete(id);
    }
}
