package com.wdpr.ee.service.customer.customerorder.model;

/**
 * Order
 */
public class Order
{
    /**
     * @return orderNumber
     */
    public String getOrderNumber()
    {
        return this.orderNumber;
    }

    /**
     * @param orderNumber
     */
    public void setOrderNumber(String orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    /**
     * @return itemName
     */
    public String getItemName()
    {
        return this.itemName;
    }

    /**
     * @param itemName
     */
    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    /**
     * @return itemDescription
     */
    public String getItemDescription()
    {
        return this.itemDescription;
    }

    /**
     * @param itemDescription
     */
    public void setItemDescription(String itemDescription)
    {
        this.itemDescription = itemDescription;
    }

    private String orderNumber;
    private String itemNumber;

    /**
     * @return itemNumber
     */
    public String getItemNumber()
    {
        return this.itemNumber;
    }

    /**
     * @param itemNumber
     */
    public void setItemNumber(String itemNumber)
    {
        this.itemNumber = itemNumber;
    }

    private String itemName;
    private String itemDescription;
    private Integer itemQuantity;

    /**
     * @return itemQuantity
     */
    public Integer getItemQuantity()
    {
        return this.itemQuantity;
    }

    /**
     * @param itemQuantity
     */
    public void setItemQuantity(Integer itemQuantity)
    {
        this.itemQuantity = itemQuantity;
    }

    private float itemPricePerUnit;
    private float totalPrice;

    /**
     * @return itemPricePerUnit
     */
    public float getItemPricePerUnit()
    {
        return this.itemPricePerUnit;
    }

    /**
     * @param itemPricePerUnit
     */
    public void setItemPricePerUnit(float itemPricePerUnit)
    {
        this.itemPricePerUnit = itemPricePerUnit;
    }

    /**
     * @return totalPrice
     */
    public float getTotalPrice()
    {
        if (this.totalPrice == 0)
        {
            this.totalPrice = this.itemPricePerUnit * this.itemQuantity;
        }
        return this.totalPrice;
    }

    /**
     * @param totalPrice
     */
    public void setTotalPrice(float totalPrice)
    {
        this.totalPrice = totalPrice;
    }
}
