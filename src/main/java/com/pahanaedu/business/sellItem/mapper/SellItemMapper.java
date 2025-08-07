package com.pahanaedu.business.sellItem.mapper;

import com.pahanaedu.business.item.mapper.ItemMapper;
import com.pahanaedu.business.sellItem.dto.SellItemDTO;
import com.pahanaedu.business.sellItem.model.SellItem;

public class SellItemMapper {

    public static SellItemDTO toSellItemDTO(SellItem sellItem) {
        return SellItemDTO.builder()
                .id(sellItem.getId())
                .item(ItemMapper.toItemDTO(sellItem.getItem()))
                .sellHistoryId(sellItem.getSellHistoryId())
                .sellPrice(sellItem.getSellPrice())
                .unit(sellItem.getUnit())
                .subTotal(sellItem.getSubTotal())
                .build();
    }


}
