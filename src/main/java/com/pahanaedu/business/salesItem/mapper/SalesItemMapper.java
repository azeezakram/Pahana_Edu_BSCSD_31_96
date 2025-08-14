package com.pahanaedu.business.salesItem.mapper;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.salesItem.dto.SalesItemDTO;
import com.pahanaedu.business.salesItem.model.SalesItem;

public class SalesItemMapper {

    public static SalesItemDTO toSalesItemDTO(SalesItem salesItem, ItemDTO itemDTO) {
        return SalesItemDTO.builder()
                .id(salesItem.getId())
                .item(itemDTO)
                .salesHistoryId(salesItem.getSalesHistoryId())
                .sellPrice(salesItem.getSellPrice())
                .unit(salesItem.getUnit())
                .subTotal(salesItem.getSubTotal())
                .build();
    }

}
