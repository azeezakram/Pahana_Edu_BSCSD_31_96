package com.pahanaedu.business.saleshistory.model;

import com.pahanaedu.business.salesItem.model.SalesItem;

import java.time.LocalDateTime;
import java.util.List;

public class SalesHistory {

    private Long id;
    private Long customer_id;
    private List<SalesItem> salesItems;
    private Integer grandTotal;
    private LocalDateTime createdAt;

    public SalesHistory(Long id, Long customer_id, List<SalesItem> salesItems, Integer grandTotal, LocalDateTime createdAt) {
        this.id = id;
        this.customer_id = customer_id;
        this.salesItems = salesItems;
        this.grandTotal = grandTotal;
        this.createdAt = createdAt;
    }

    public SalesHistory(Long customer_id, List<SalesItem> salesItems, Integer grandTotal, LocalDateTime createdAt) {
        this.customer_id = customer_id;
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
        return customer_id;
    }

    public void setCustomerId(Long customer_id) {
        this.customer_id = customer_id;
    }

    public List<SalesItem> getSellItems() {
        return salesItems;
    }

    public void setSellItems(List<SalesItem> salesItems) {
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
                ", customer_id=" + customer_id +
                ", sellItems=" + salesItems +
                ", grandTotal=" + grandTotal +
                ", createdAt=" + createdAt +
                '}';
    }
}
