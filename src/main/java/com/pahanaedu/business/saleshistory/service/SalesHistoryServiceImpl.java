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

public class SalesHistoryServiceImpl implements SalesHistoryService {

    private final SalesHistoryRepository salesHistoryRepository;
    private final SalesItemService salesItemService;
    private final CustomerServiceImpl customerService;

    public SalesHistoryServiceImpl(String dbType) {
        this.salesHistoryRepository = new SalesHistoryRepositoryImpl(dbType);
        this.salesItemService = new SalesItemServiceImpl(dbType);
        this.customerService = new CustomerServiceImpl(dbType);
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
    public SalesHistoryDTO create(SalesHistory salesHistory) {
        SalesHistory createdSalesHistory = salesHistoryRepository.save(salesHistory);

        return createdSalesHistory != null ? SalesHistoryMapper.toSellHistoryDTO(salesHistory) : null;
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
