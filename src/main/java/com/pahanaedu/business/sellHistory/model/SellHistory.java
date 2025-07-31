package com.pahanaedu.business.sellHistory.model;

import com.pahanaedu.business.sellItem.model.SellItem;
import com.pahanaedu.business.user.module.customer.model.Customer;

import java.time.LocalDateTime;
import java.util.List;

public class SellHistory {

    private Long id;
    private Customer customer;
    private List<SellItem> sellItems;
    private Integer grandTotal;
    private LocalDateTime createdAt;

    public SellHistory(Long id, Customer customer, List<SellItem> sellItems, Integer grandTotal, LocalDateTime createdAt) {
        this.id = id;
        this.customer = customer;
        this.sellItems = sellItems;
        this.grandTotal = grandTotal;
        this.createdAt = createdAt;
    }

    public SellHistory(Customer customer, List<SellItem> sellItems, Integer grandTotal, LocalDateTime createdAt) {
        this.customer = customer;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
                ", sellItems=" + sellItems +
                ", grandTotal=" + grandTotal +
                ", createdAt=" + createdAt +
                '}';
    }
}
