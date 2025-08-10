package com.pahanaedu.business.sellHistory.mapper;

import com.pahanaedu.business.sellHistory.dto.SellHistoryDTO;
import com.pahanaedu.business.sellHistory.model.SellHistory;
import com.pahanaedu.business.sellItem.dto.SellItemDTO;
import com.pahanaedu.business.sellItem.mapper.SellItemMapper;
import com.pahanaedu.business.user.module.customer.dto.CustomerDTO;
import com.pahanaedu.business.user.module.customer.mapper.CustomerMapper;

import java.util.List;

public class SellHistoryMapper {

    public static SellHistoryDTO toSellHistoryDTO(SellHistory sellHistory, CustomerDTO customerDTO, List<SellItemDTO> sellItemDTOS) {
        return SellHistoryDTO.builder()
                .id(sellHistory.getId())
                .customer(customerDTO)
                .sellItems(sellItemDTOS)
                .grandTotal(sellHistory.getGrandTotal())
                .createdAt(sellHistory.getCreatedAt())
                .build();
    }

    public static SellHistoryDTO toSellHistoryDTO(SellHistory sellHistory, CustomerDTO customerDTO) {
        return SellHistoryDTO.builder()
                .id(sellHistory.getId())
                .customer(customerDTO)
                .sellItems(null)
                .grandTotal(sellHistory.getGrandTotal())
                .createdAt(sellHistory.getCreatedAt())
                .build();
    }

}
