package com.pahanaedu.business.sellItem.service;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.business.item.service.ItemServiceImpl;
import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellItem.dto.SellItemDTO;
import com.pahanaedu.business.sellItem.mapper.SellItemMapper;
import com.pahanaedu.business.sellItem.model.SellItem;
import com.pahanaedu.common.interfaces.Service;
import com.pahanaedu.persistence.sellitem.SellItemRepository;

import java.util.ArrayList;
import java.util.List;

public class SellItemServiceImpl implements Service<SellItem, SellItemDTO> {

    private final SellItemRepository sellItemRepository;
    private final ItemServiceImpl itemService;

    public SellItemServiceImpl() {
        this.sellItemRepository = new SellItemRepository();
        this.itemService = new ItemServiceImpl();
    }

    @Override
    public SellItemDTO findById(Long id) {
        SellItem sellItem = sellItemRepository.findById(id);
        ItemDTO item = null;

        if (sellItem != null) {
            item = itemService.findById(sellItem.getItemId());
        }

        return sellItem != null ? SellItemMapper.toSellItemDTO(sellItem, item) : null;
    }

    public List<SellItemDTO> findBySellHistoryId(Long sellHistoryId) {
        List<SellItemDTO> sellItemDTOS = new ArrayList<>();
        List<SellItem> sellItems = sellItemRepository.findBySellHistoryId(sellHistoryId);
        sellItems.forEach(System.out::println);

        if (!sellItems.isEmpty()) {
            for (SellItem sellItem : sellItems) {
                if (sellItem != null) {
                    ItemDTO itemDTO = itemService.findById(sellItem.getItemId());
                    sellItemDTOS.add(SellItemMapper.toSellItemDTO(sellItem, itemDTO));
                }
            }
        }

        return sellItemDTOS;
    }

    @Override
    public List<SellItemDTO> findAll() {
        return List.of();
    }

    @Override
    public SellItemDTO create(SellItem obj) {
        return null;
    }

    public void createBySellHistory(SellHistory sellHistory) {
        sellItemRepository.saveBySellHistory(sellHistory);
    }

    @Override
    public boolean delete(Long id) {
        return sellItemRepository.delete(id);
    }
}
