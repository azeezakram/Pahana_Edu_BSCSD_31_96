package com.pahanaedu.business.sellItem.dto;

import com.pahanaedu.business.item.dto.ItemDTO;
import com.pahanaedu.business.item.model.Item;

public class SellItemDTO {

    private final Long id;
    private final ItemDTO item;
    private final Long sellHistoryId;
    private final Integer sellPrice;
    private final Integer unit;
    private final Integer subTotal;

    private SellItemDTO(Builder builder) {
        this.id = builder.id;
        this.item = builder.item;
        this.sellHistoryId = builder.sellHistoryId;
        this.sellPrice = builder.sellPrice;
        this.unit = builder.unit;
        this.subTotal = builder.subTotal;
    }

    public Long getId() {
        return id;
    }

    public ItemDTO getItem() {
        return item;
    }

    public Long getSellHistoryId() {
        return sellHistoryId;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public Integer getUnit() {
        return unit;
    }

    public Integer getSubTotal() {
        return subTotal;
    }

    @Override
    public String toString() {
        return "SellItemDTO{" +
                "id=" + id +
                ", item=" + item +
                ", sellHistoryId=" + sellHistoryId +
                ", unit=" + unit +
                ", subTotal=" + subTotal +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private ItemDTO item;
        private Long sellHistoryId;
        private Integer sellPrice;
        private Integer unit;
        private Integer subTotal;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder item(ItemDTO item) {
            this.item = item;
            return this;
        }

        public Builder sellHistoryId(Long sellHistoryId) {
            this.sellHistoryId = sellHistoryId;
            return this;
        }

        public Builder sellPrice(Integer sellPrice) {
            this.sellPrice = sellPrice;
            return this;
        }

        public Builder unit(Integer unit) {
            this.unit = unit;
            return this;
        }

        public Builder subTotal(Integer subTotal) {
            this.subTotal = subTotal;
            return this;
        }

        public SellItemDTO build() {
            return new SellItemDTO(this);
        }
    }
}

