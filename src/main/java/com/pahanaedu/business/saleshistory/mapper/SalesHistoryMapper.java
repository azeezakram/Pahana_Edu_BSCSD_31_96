package com.pahanaedu.business.saleshistory.mapper;

import com.pahanaedu.business.salesItem.mapper.SalesItemMapper;
import com.pahanaedu.business.saleshistory.dto.SalesHistoryDTO;
import com.pahanaedu.business.saleshistory.model.SalesHistory;
import com.pahanaedu.business.salesItem.dto.SalesItemDTO;
import com.pahanaedu.business.user.module.customer.dto.CustomerDTO;

import java.util.List;

public class SalesHistoryMapper {

    public static SalesHistoryDTO toSellHistoryDTO(SalesHistory salesHistory, CustomerDTO customerDTO, List<SalesItemDTO> salesItemDTOS) {
        return SalesHistoryDTO.builder()
                .id(salesHistory.getId())
                .customer(customerDTO)
                .sellItems(salesItemDTOS)
                .grandTotal(salesHistory.getGrandTotal())
                .createdAt(salesHistory.getCreatedAt())
                .build();
    }

    public static SalesHistoryDTO toSellHistoryDTO(SalesHistory salesHistory, CustomerDTO customerDTO) {
        return SalesHistoryDTO.builder()
                .id(salesHistory.getId())
                .customer(customerDTO)
                .sellItems(null)
                .grandTotal(salesHistory.getGrandTotal())
                .createdAt(salesHistory.getCreatedAt())
                .build();
    }

    public static SalesHistoryDTO toSellHistoryDTO(SalesHistory salesHistory) {
        return SalesHistoryDTO.builder()
                .id(salesHistory.getId())
                .customer(
                        new CustomerDTO.Builder()
                                .setId(salesHistory.getCustomerId())
                                .build()
                )
                .sellItems(null)
                .grandTotal(salesHistory.getGrandTotal())
                .createdAt(salesHistory.getCreatedAt())
                .build();
    }

    public static SalesHistory toSalesHistory(SalesHistoryDTO salesHistoryDTO) {
        return new SalesHistory(
                salesHistoryDTO.getId(),
                salesHistoryDTO.getCustomer().getId(),
                salesHistoryDTO.getSalesItems().stream()
                        .map(SalesItemMapper::toSalesItem)
                        .toList(),
                salesHistoryDTO.getGrandTotal(),
                salesHistoryDTO.getCreatedAt()
        );
    }

}
