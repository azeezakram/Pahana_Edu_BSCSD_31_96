package com.pahanaedu.business.item.dto;

import com.pahanaedu.business.category.model.Category;

import java.time.LocalDateTime;

public class ItemDto {

    private final Long id;
    private final String itemName;
    private final String description;
    private final String brand;
    private final Category category;
    private final Integer price;
    private final Integer stock;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private ItemDto(Builder builder) {
        this.id = builder.id;
        this.itemName = builder.itemName;
        this.description = builder.description;
        this.brand = builder.brand;
        this.category = builder.category;
        this.price = builder.price;
        this.stock = builder.stock;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public Category getCategory() {
        return category;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public static class Builder {
        private Long id;
        private String itemName;
        private String description;
        private String brand;
        private Category category;
        private Integer price;
        private Integer stock;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder itemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Builder price(Integer price) {
            this.price = price;
            return this;
        }

        public Builder stock(Integer stock) {
            this.stock = stock;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ItemDto build() {
            return new ItemDto(this);
        }
    }

}
