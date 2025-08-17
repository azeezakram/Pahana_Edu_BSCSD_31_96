package com.pahanaedu.business.saleshistory.model;

import com.pahanaedu.business.salesItem.model.SalesItem;

import java.time.LocalDateTime;
import java.util.List;

public class SalesHistory {

    private Long id;
    private Long customerId;
    private List<SalesItem> salesItems;
    private Integer grandTotal;
    private LocalDateTime createdAt;

    public SalesHistory(Long id, Long customerId, List<SalesItem> salesItems, Integer grandTotal, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.salesItems = salesItems;
        this.grandTotal = grandTotal;
        this.createdAt = createdAt;
    }

    public SalesHistory(Long customerId, List<SalesItem> salesItems, Integer grandTotal, LocalDateTime createdAt) {
        this.customerId = customerId;
        this.salesItems = salesItems;
        this.grandTotal = grandTotal;
        this.createdAt = createdAt;
    }

    public SalesHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<SalesItem> getSalesItems() {
        return salesItems;
    }

    public void setSalesItems(List<SalesItem> salesItems) {
        this.salesItems = salesItems;
    }

    public Integer getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Integer grandTotal) {
        this.grandTotal = grandTotal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "SellHistory{" +
                "id=" + id +
                ", customerIdd=" + customerId +
                ", sellItems=" + salesItems +
                ", grandTotal=" + grandTotal +
                ", createdAt=" + createdAt +
                '}';
    }
}
