package com.pahanaedu.business.sellHistory.service;

import com.pahanaedu.business.sellHistory.dto.SellHistoryDTO;
import com.pahanaedu.business.sellHistory.mapper.SellHistoryMapper;
import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellItem.mapper.SellItemMapper;
import com.pahanaedu.business.user.module.customer.mapper.CustomerMapper;
import com.pahanaedu.common.interfaces.Service;
import com.pahanaedu.persistence.sellhistory.SellHistoryRepositoryImpl;

import java.util.List;
import java.util.Objects;

public class SellHistoryServiceImpl implements Service<SellHistory, SellHistoryDTO> {

    private final SellHistoryRepositoryImpl sellHistoryRepository;

    public SellHistoryServiceImpl() {
        this.sellHistoryRepository = new SellHistoryRepositoryImpl();
    }

    @Override
    public SellHistoryDTO findById(Long id) {
        SellHistory sellHistory = sellHistoryRepository.findById(id);
        return Objects.nonNull(sellHistory) ? SellHistoryMapper.toSellHistoryDTO(sellHistory) : null;
    }

    @Override
    public List<SellHistoryDTO> findAll() {
        return List.of();
    }

    @Override
    public SellHistoryDTO create(SellHistory sellHistory) {
        return null;
    }

    @Override
    public SellHistoryDTO update(SellHistory sellHistory) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
