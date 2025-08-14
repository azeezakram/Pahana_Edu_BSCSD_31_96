package com.pahanaedu.persistence.salesitem;

import com.pahanaedu.business.saleshistory.model.SalesHistory;
import com.pahanaedu.business.salesItem.model.SalesItem;
import com.pahanaedu.common.interfaces.Repository;

import java.util.List;

public interface SalesItemRepository extends Repository<SalesItem> {
    List<SalesItem> findBySellHistoryId(Long sellHistoryId);
    void saveBySellHistory(SalesHistory salesHistory);
}
