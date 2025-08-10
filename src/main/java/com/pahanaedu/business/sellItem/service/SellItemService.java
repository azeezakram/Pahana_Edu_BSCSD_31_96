package com.pahanaedu.business.sellItem.service;

import com.pahanaedu.business.sellHistory.dto.SellHistoryDTO;
import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellItem.dto.SellItemDTO;
import com.pahanaedu.business.sellItem.model.SellItem;
import com.pahanaedu.common.interfaces.Service;

import java.util.List;

public interface SellItemService extends Service<SellItem, SellItemDTO> {
    List<SellItemDTO> findBySellHistoryId(Long sellHistoryId);
    void createBySellHistory(SellHistory sellHistory);
}
