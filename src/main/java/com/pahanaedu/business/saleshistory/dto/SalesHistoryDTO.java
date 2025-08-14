package com.pahanaedu.business.saleshistory.dto;

import com.pahanaedu.business.salesItem.dto.SalesItemDTO;
import com.pahanaedu.business.user.module.customer.dto.CustomerDTO;

import java.time.LocalDateTime;
import java.util.List;

public class SalesHistoryDTO {

    private final Long id;
    private final CustomerDTO customer;
    private final List<SalesItemDTO> salesItemDTOS;
    private final Integer grandTotal;
    private final LocalDateTime createdAt;

    private SalesHistoryDTO(Builder builder) {
        this.id = builder.id;
        this.customer = builder.customer;
        this.salesItemDTOS = builder.sellItems;
        this.grandTotal = builder.grandTotal;
        this.createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public List<SalesItemDTO> getSalesItems() {
        return salesItemDTOS;
    }

    public Integer getGrandTotal() {
        return grandTotal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "SellHistoryDTO{" +
                "id=" + id +
                ", customer=" + customer +
                ", sellItems=" + salesItemDTOS +
                ", grandTotal=" + grandTotal +
                ", createdAt=" + createdAt +
                '}';
    }

    public static class Builder {
        private Long id;
        private CustomerDTO customer;
        private List<SalesItemDTO> sellItems;
        private Integer grandTotal;
        private LocalDateTime createdAt;

        private Builder() {}

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder customer(CustomerDTO customer) {
            this.customer = customer;
            return this;
        }

        public Builder sellItems(List<SalesItemDTO> sellItems) {
            this.sellItems = sellItems;
            return this;
        }

        public Builder grandTotal(Integer grandTotal) {
            this.grandTotal = grandTotal;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public SalesHistoryDTO build() {
            return new SalesHistoryDTO(this);
        }
    }
}

