package com.wdpr.ee.service.customer.customerorder.model;

public class ItemVO
{
    private int pricePerUnit;
    private String itemName;
    private String description;

    public int getPricePerUnit()
    {
        return pricePerUnit;
    }

    public void setPricePerUnit(int pricePerUnit)
    {
        this.pricePerUnit = pricePerUnit;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    // {"pricePerUnit":5,"itemName":"Celebrations 300-ct. Clear Mini Light Set","description":"Ace Hardware >> Hardware/Tools"}
    // customerOrder.setItemPricePerUnit(rs.getInt("PRICEPERUNIT"));
    // customerOrder.setItemName(rs.getString("ITEM"));
    // customerOrder.setItemDescription(rs.getString("STORE")+" >> "+rs.getString("CATEGORY"));

}
