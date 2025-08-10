package com.pahanaedu.persistence.sellitem;

import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellItem.model.SellItem;
import com.pahanaedu.common.interfaces.Repository;

import java.util.List;

public interface SellItemRepository extends Repository<SellItem> {
    List<SellItem> findBySellHistoryId(Long sellHistoryId);
    void saveBySellHistory(SellHistory sellHistory);
}
