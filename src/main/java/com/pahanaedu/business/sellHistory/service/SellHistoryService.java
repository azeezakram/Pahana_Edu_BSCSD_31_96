package com.pahanaedu.business.sellHistory.service;

import com.pahanaedu.business.sellHistory.dto.SellHistoryDTO;
import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.common.interfaces.Service;

import java.util.List;

public interface SellHistoryService extends Service<SellHistory, SellHistoryDTO> {
    SellHistoryDTO findByIdWithItems(Long id);
    List<SellHistoryDTO> findAllWithItems();

}
