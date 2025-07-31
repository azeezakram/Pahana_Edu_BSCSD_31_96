package com.pahanaedu.business.sellItem.model;

import com.pahanaedu.business.item.model.Item;
import com.pahanaedu.business.sellHistory.model.SellHistory;

public class SellItem {

    private Long id;
    private Item item;
    private Long sellHistoryId;
    private Integer unit;
    private Integer subTotal;

    public SellItem(Long id, Item item, Long sellHistoryId, Integer unit, Integer subTotal) {
        this.id = id;
        this.item = item;
        this.sellHistoryId = sellHistoryId;
        this.unit = unit;
        this.subTotal = subTotal;
    }

    public SellItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Long getSellHistoryId() {
        return sellHistoryId;
    }

    public void setSellHistoryId(Long sellHistoryId) {
        this.sellHistoryId = sellHistoryId;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Integer getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Integer subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public String toString() {
        return "SellItem{" +
                "id=" + id +
                ", item=" + item +
                ", sellHistoryId=" + sellHistoryId +
                ", unit=" + unit +
                ", subTotal=" + subTotal +
                '}';
    }

}
