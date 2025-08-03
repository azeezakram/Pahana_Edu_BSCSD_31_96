package com.pahanaedu.business.sellHistory.mapper;

import com.pahanaedu.business.sellHistory.dto.SellHistoryDTO;
import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellItem.mapper.SellItemMapper;
import com.pahanaedu.business.user.module.customer.mapper.CustomerMapper;

public class SellHistoryMapper {

    public static SellHistoryDTO toSellHistoryDTO(SellHistory sellHistory) {
        return SellHistoryDTO.builder()
                .id(sellHistory.getId())
                .customer(CustomerMapper.toCustomerDto(sellHistory.getCustomer()))
                .sellItems(sellHistory.getSellItems().stream()
                        .map(SellItemMapper::toSellItemDTO).toList())
                .grandTotal(sellHistory.getGrandTotal())
                .createdAt(sellHistory.getCreatedAt())
                .build();
    }

}
