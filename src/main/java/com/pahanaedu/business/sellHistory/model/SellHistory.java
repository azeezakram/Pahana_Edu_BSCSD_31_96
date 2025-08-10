package com.pahanaedu.business.sellHistory.model;

import com.pahanaedu.business.sellItem.model.SellItem;
import com.pahanaedu.business.user.module.customer.model.Customer;

import java.time.LocalDateTime;
import java.util.List;

public class SellHistory {

    private Long id;
    private Long customer_id;
    private List<SellItem> sellItems;
    private Integer grandTotal;
    private LocalDateTime createdAt;

    public SellHistory(Long id, Long customer_id, List<SellItem> sellItems, Integer grandTotal, LocalDateTime createdAt) {
        this.id = id;
        this.customer_id = customer_id;
        this.sellItems = sellItems;
        this.grandTotal = grandTotal;
        this.createdAt = createdAt;
    }

    public SellHistory(Long customer_id, List<SellItem> sellItems, Integer grandTotal, LocalDateTime createdAt) {
        this.customer_id = customer_id;
        this.sellItems = sellItems;
        this.grandTotal = grandTotal;
        this.createdAt = createdAt;
    }

    public SellHistory() {
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

    public List<SellItem> getSellItems() {
        return sellItems;
    }

    public void setSellItems(List<SellItem> sellItems) {
        this.sellItems = sellItems;
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
                ", sellItems=" + sellItems +
                ", grandTotal=" + grandTotal +
                ", createdAt=" + createdAt +
                '}';
    }
}
