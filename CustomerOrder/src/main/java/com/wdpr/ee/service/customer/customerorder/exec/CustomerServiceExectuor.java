package com.wdpr.ee.service.customer.customerorder.exec;

import com.wdpr.ee.service.customer.customerorder.db.CustomerAddressDAO;
import com.wdpr.ee.service.customer.customerorder.db.CustomerDAO;
import com.wdpr.ee.service.customer.customerorder.db.CustomerOrderDAO;
import com.wdpr.ee.service.customer.customerorder.model.Customer;
import com.wdpr.ee.service.customer.customerorder.model.CustomerOrder;
import com.wdpr.ee.service.customer.customerorder.util.LocalHeader;
import com.wdpr.ee.service.customer.customerorder.util.RestConnector;
import java.io.IOException;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomerServiceExectuor
{
    private static final Logger logger = LogManager.getLogger("CustomerServiceExectuor");

    private static CustomerDAO customerDAO = new CustomerDAO();
    private static CustomerAddressDAO customerAddressDAO = new CustomerAddressDAO();
    private static CustomerOrderDAO customerOrderDAO = new CustomerOrderDAO();

    public static CustomerOrderDAO getCustomerOrderDAO()
    {
        return customerOrderDAO;
    }

    public static void setCustomerOrderDAO(CustomerOrderDAO customerOrderDAO)
    {
        CustomerServiceExectuor.customerOrderDAO = customerOrderDAO;
    }

    // @WdprMethodLog
    public void createCustomer(Customer customer) throws ClassNotFoundException, SQLException,
            IOException, Exception
    {
        logger.debug("entry createCustomer");
        customerDAO.loadCustomer(customer);
        customerAddressDAO.loadCustomerAddress(customer);
        logger.debug("exit createCustomer");
    }

    public static CustomerDAO getCustomerDAO()
    {
        return customerDAO;
    }

    public static void setCustomerDAO(CustomerDAO customerDAO)
    {
        CustomerServiceExectuor.customerDAO = customerDAO;
    }

    public static CustomerAddressDAO getCustomerAddressDAO()
    {
        return customerAddressDAO;
    }

    public static void setCustomerAddressDAO(CustomerAddressDAO customerAddressDAO)
    {
        CustomerServiceExectuor.customerAddressDAO = customerAddressDAO;
    }

    public void createCustomerOrder(CustomerOrder customerOrder) throws ClassNotFoundException,
            SQLException, IOException
    {
        RestConnector.priceItemNumber(customerOrder, LocalHeader.userThreadLocal.get());
        customerOrderDAO.loadCustomerOrder(customerOrder);

    }

}
