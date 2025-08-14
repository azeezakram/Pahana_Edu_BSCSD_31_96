package com.pahanaedu.business.salesItem.service;

import com.pahanaedu.business.salesItem.dto.SalesItemDTO;
import com.pahanaedu.business.saleshistory.model.SalesHistory;
import com.pahanaedu.business.salesItem.model.SalesItem;
import com.pahanaedu.common.interfaces.Service;

import java.util.List;

public interface SalesItemService extends Service<SalesItem, SalesItemDTO> {
    List<SalesItemDTO> findBySellHistoryId(Long sellHistoryId);
    void createBySellHistory(SalesHistory salesHistory);
}
