package com.pahanaedu.business.saleshistory.service;

import com.pahanaedu.business.saleshistory.dto.SalesHistoryDTO;
import com.pahanaedu.business.saleshistory.model.SalesHistory;
import com.pahanaedu.common.interfaces.Service;

public interface SalesHistoryService extends Service<SalesHistory, SalesHistoryDTO> {
    SalesHistoryDTO findByIdWithItems(Long id);
}
