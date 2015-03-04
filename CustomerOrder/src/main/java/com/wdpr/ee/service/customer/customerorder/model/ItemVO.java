package com.wdpr.ee.service.customer.customerorder.model;

/**
 *
 */
public class ItemVO
{
    private int pricePerUnit;
    private String itemName;
    private String description;

    /**
     * @return pricePerUnit
     */
    public int getPricePerUnit()
    {
        return this.pricePerUnit;
    }

    /**
     * @param pricePerUnit
     */
    public void setPricePerUnit(int pricePerUnit)
    {
        this.pricePerUnit = pricePerUnit;
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
     * @return description
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    // {"pricePerUnit":5,"itemName":"Celebrations 300-ct. Clear Mini Light Set","description":"Ace Hardware >> Hardware/Tools"}
    // customerOrder.setItemPricePerUnit(rs.getInt("PRICEPERUNIT"));
    // customerOrder.setItemName(rs.getString("ITEM"));
    // customerOrder.setItemDescription(rs.getString("STORE")+" >> "+rs.getString("CATEGORY"));
}
