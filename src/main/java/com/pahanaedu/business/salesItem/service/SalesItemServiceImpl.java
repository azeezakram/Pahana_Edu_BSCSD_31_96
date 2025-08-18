package com.pahanaedu.business.salesItem.service;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.service.ItemService;
import com.pahanaedu.business.item.service.ItemServiceImpl;
import com.pahanaedu.business.salesItem.dto.SalesItemDTO;
import com.pahanaedu.business.saleshistory.model.SalesHistory;
import com.pahanaedu.business.salesItem.mapper.SalesItemMapper;
import com.pahanaedu.business.salesItem.model.SalesItem;
import com.pahanaedu.persistence.salesitem.SalesItemRepository;
import com.pahanaedu.persistence.salesitem.SalesItemRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class SalesItemServiceImpl implements SalesItemService {

    private final SalesItemRepository salesItemRepository;
    private final ItemService itemService;

    public SalesItemServiceImpl(String dbType) {
        this.salesItemRepository = new SalesItemRepositoryImpl(dbType);
        this.itemService = new ItemServiceImpl(dbType);
    }

    @Override
    public SalesItemDTO findById(Long id) {
        SalesItem salesItem = salesItemRepository.findById(id);
        ItemDTO item = null;

        if (salesItem != null) {
            item = itemService.findById(salesItem.getItemId());
        }

        return salesItem != null ? SalesItemMapper.toSalesItemDTO(salesItem, item) : null;
    }

    @Override
    public List<SalesItemDTO> findBySellHistoryId(Long sellHistoryId) {
        List<SalesItemDTO> salesItemDTOS = new ArrayList<>();
        List<SalesItem> sellItems = salesItemRepository.findBySellHistoryId(sellHistoryId);

        return addSalesItemToList(salesItemDTOS, sellItems);
    }

    @Override
    public List<SalesItemDTO> findAll() {
        List<SalesItemDTO> salesItemDTOS = new ArrayList<>();
        List<SalesItem> sellItems = salesItemRepository.findAll();

        return addSalesItemToList(salesItemDTOS, sellItems);
    }

    private List<SalesItemDTO> addSalesItemToList(List<SalesItemDTO> salesItemDTOS, List<SalesItem> sellItems) {
        if (!sellItems.isEmpty()) {
            for (SalesItem salesItem : sellItems) {
                if (salesItem != null) {
                    ItemDTO itemDTO = itemService.findById(salesItem.getItemId());
                    salesItemDTOS.add(SalesItemMapper.toSalesItemDTO(salesItem, itemDTO));
                }
            }
        }

        return salesItemDTOS;
    }

    @Override
    public SalesItemDTO create(SalesItem obj) {
        return null;
    }

    @Override
    public void createBySellHistory(SalesHistory salesHistory) {
        salesItemRepository.saveBySellHistory(salesHistory);
    }

    @Override
    public boolean delete(Long id) {
        return salesItemRepository.delete(id);
    }
}
