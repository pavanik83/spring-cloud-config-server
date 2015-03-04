package com.wdpr.ee.service.customer.customerorder.model;

import com.wdpr.ee.service.customer.customerorder.model.CustomerServiceOption.paramOption;
import java.util.UUID;
import javax.ws.rs.core.MultivaluedMap;


public class Customer {
    public Customer(){

    }


    public Customer(MultivaluedMap<String, String> params) {


        if(params!=null)
        {
            Address address = new Address();


        for(paramOption paramName : CustomerServiceOption.paramOption.values())
        {
            this.setCustomerId(UUID.randomUUID().toString());
            if(paramName.toString().equals("namePrefix") && params.getFirst(paramName.toString())!=null)
            this.setFirstName(params.getFirst(paramName.toString()));

            if(paramName.toString().equals("firstName") && params.getFirst(paramName.toString())!=null)
            this.setFirstName(params.getFirst(paramName.toString()));
            if(paramName.toString().equals("lastName") && params.getFirst(paramName.toString())!=null)
            this.setLastName(params.getFirst(paramName.toString()));
            if(paramName.toString().equals("middleName") && params.getFirst(paramName.toString())!=null)
            this.setMiddleName(params.getFirst(paramName.toString()));
            if(paramName.toString().equals("gender") && params.getFirst(paramName.toString())!=null)
            this.setGender(params.getFirst(paramName.toString()));
            if(paramName.toString().equals("firstAddressLine") && params.getFirst(paramName.toString())!=null)
            address.setFirstAddressLine(params.getFirst(paramName.toString()));
            if(paramName.toString().equals("secondAddressLine") && params.getFirst(paramName.toString())!=null)
            address.setSecondAddressLine(params.getFirst(paramName.toString()));
            if(paramName.toString().equals("city") && params.getFirst(paramName.toString())!=null)
            address.setCity(params.getFirst(paramName.toString()));
            if(paramName.toString().equals("state") && params.getFirst(paramName.toString())!=null)
            address.setState(params.getFirst(paramName.toString()));
            try{
            if(paramName.toString().equals("zip") && params.getFirst(paramName.toString())!=null)
            {
            String[] args = params.getFirst(paramName.toString()).split("-");
            if(args[0].length()==5  && args[1].length()==4)
            {
            Integer fiveDigitzip = Integer.parseInt(args[0]);
            Integer fourDigitzip = Integer.parseInt(args[1]);

            address.setZip(fiveDigitzip.toString()+"-"+fourDigitzip.toString());
            }

            }
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException("Illegal values for Zip code",  new Exception("Invalid Params"));
            }



        }
        if(address.getFirstAddressLine()!=null && address.getCity()!=null && address.getZip()!=null)
        this.setAddress(address);
        if(this.firstName==null || this.lastName==null)
        {


            throw new IllegalArgumentException("Illegal values for first name and/or last name",  new Exception("Invalid Params"));
        }

        }

    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return Address;
    }
    public void setAddress(Address address) {
        Address = address;
    }
    private String firstName;
    private String lastName;
    private String namePrefix;
    public String getNamePrefix() {
        return namePrefix;
    }
    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }
    private String middleName;
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    private String customerId;
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    private String gender;
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    private Address Address;

}
