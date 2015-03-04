package com.wdpr.ee.service.customer.customerorder.model;

public class Order {
    public String getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }


    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getItemDescription() {
        return itemDescription;
    }
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
    private String orderNumber;
    private String itemNumber;
    public String getItemNumber() {
        return itemNumber;
    }
    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }
    private String itemName;
    private String itemDescription;
    private Integer itemQuantity;
    public Integer getItemQuantity() {
        return itemQuantity;
    }
    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
    private float itemPricePerUnit;
    private float totalPrice;
    public float getItemPricePerUnit() {
        return itemPricePerUnit;
    }
    public void setItemPricePerUnit(float itemPricePerUnit) {
        this.itemPricePerUnit = itemPricePerUnit;
    }
    public float getTotalPrice() {
        if(this.totalPrice ==0)
        {
            this.totalPrice=this.itemPricePerUnit*this.itemQuantity;
        }
        return totalPrice;
    }
    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }


}
