package com.wdpr.ee.service.customer.customerorder.model;

public class CustomerServiceOption {
    public  enum   paramOption
        {
        namePrefix,
        firstName,
        middleName,
        lastName,
        gender,
        firstAddressLine,
        secondAddressLine,
        city,
        state,
        zip
    }

    public  enum   orderOption
    {
    customerId,
    shippingAddressId,
    itemNumber,
    itemQuantity,
    }
}
