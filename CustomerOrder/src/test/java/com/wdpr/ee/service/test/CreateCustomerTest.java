/**
 *
 */
package com.wdpr.ee.service.test;

import static org.junit.Assert.assertTrue;
import com.wdpr.ee.service.customer.customerorder.exec.CustomerServiceExectuor;
import com.wdpr.ee.service.customer.customerorder.model.Address;
import com.wdpr.ee.service.customer.customerorder.model.Customer;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author NARAA011
 *
 *
 */
public class CreateCustomerTest
{
    /**
     *
     */
    CustomerServiceExectuor customerServiceExecutor;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {
        this.customerServiceExecutor = new CustomerServiceExectuor();
        // DBConnectionFactory dbFactory = DBConnectionFactory.getInstance();
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception
    {
        // No tear down
    }

    /**
     *
     */
    @Test
    public void testCustomerservice()
    {
        Customer customer = new Customer();
        customer.setFirstName("Nixon-xx");
        customer.setLastName("George-xx");
        customer.setCustomerId(UUID.randomUUID().toString());
        Address address = new Address();
        address.setFirstAddressLine("4768 Quaking Spring Chase");
        address.setState("North Carolina");
        address.setCity("Cape Horn");
        address.setZip("28464-4045");
        customer.setAddress(address);

        try
        {
            this.customerServiceExecutor.createCustomer(customer);
            assertTrue(" Success", true);
        }
        catch (Exception e)
        {
            assertTrue(" failure", false);
        }
    }
}
