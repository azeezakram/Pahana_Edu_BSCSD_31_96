package com.pahanaedu.business.salesItem.model;

public class SalesItem {

    private Long id;
    private Long itemId;
    private Long salesHistoryId;
    private Integer sellPrice;
    private Integer unit;
    private Integer subTotal;

    public SalesItem(Long id, Long itemId, Long salesHistoryId, Integer sellPrice, Integer unit, Integer subTotal) {
        this.id = id;
        this.itemId = itemId;
        this.salesHistoryId = salesHistoryId;
        this.sellPrice = sellPrice;
        this.unit = unit;
        this.subTotal = subTotal;
    }

    public SalesItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getSalesHistoryId() {
        return salesHistoryId;
    }

    public void setSalesHistoryId(Long salesHistoryId) {
        this.salesHistoryId = salesHistoryId;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Integer sellPrice) {
        this.sellPrice = sellPrice;
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
                ", itemId=" + itemId +
                ", salesHistoryId=" + salesHistoryId +
                ", sellPrice=" + sellPrice +
                ", unit=" + unit +
                ", subTotal=" + subTotal +
                '}';
    }
}
