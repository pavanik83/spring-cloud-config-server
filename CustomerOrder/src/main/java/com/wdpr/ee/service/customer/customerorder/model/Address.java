package com.wdpr.ee.service.customer.customerorder.model;

import java.util.UUID;

public class Address
{

    public String getCity()
    {
        return this.city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getFirstAddressLine()
    {
        return this.firstAddressLine;
    }

    public void setFirstAddressLine(String firstAddressLine)
    {
        this.firstAddressLine = firstAddressLine;
    }

    public String getSecondAddressLine()
    {
        return this.secondAddressLine;
    }

    public void setSecondAddressLine(String secondAddressLine)
    {
        this.secondAddressLine = secondAddressLine;
    }

    private String firstAddressLine;
    private String secondAddressLine;
    private String city;
    private String zip;

    public String getZip()
    {
        return this.zip;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }

    private String state;
    private String uuid;

    public String getUuid()
    {
        if (this.uuid == null)
            this.uuid = UUID.randomUUID().toString();
        return this.uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getState()
    {
        return this.state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

}
