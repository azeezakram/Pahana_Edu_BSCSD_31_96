package com.pahanaedu.business.sellHistory.dto;

import com.pahanaedu.business.sellItem.dto.SellItemDTO;
import com.pahanaedu.business.user.module.customer.dto.CustomerDTO;

import java.time.LocalDateTime;
import java.util.List;

public class SellHistoryDTO {

    private final Long id;
    private final CustomerDTO customer;
    private final List<SellItemDTO> sellItems;
    private final Integer grandTotal;
    private final LocalDateTime createdAt;

    private SellHistoryDTO(Builder builder) {
        this.id = builder.id;
        this.customer = builder.customer;
        this.sellItems = builder.sellItems;
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

    public List<SellItemDTO> getSellItems() {
        return sellItems;
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
                ", sellItems=" + sellItems +
                ", grandTotal=" + grandTotal +
                ", createdAt=" + createdAt +
                '}';
    }

    public static class Builder {
        private Long id;
        private CustomerDTO customer;
        private List<SellItemDTO> sellItems;
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

        public Builder sellItems(List<SellItemDTO> sellItems) {
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

        public SellHistoryDTO build() {
            return new SellHistoryDTO(this);
        }
    }
}

