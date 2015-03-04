package com.wdpr.ee.service.customer.customerorder.db;

import com.wdpr.ee.service.customer.customerorder.model.CustomerOrder;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomerOrderDAO extends CustomerAddressDAO
{

    private static final Logger logging = LogManager.getLogger(CustomerOrderDAO.class);

    public void loadCustomerOrder(CustomerOrder customerOrder) throws SQLException,
            ClassNotFoundException, IOException
    {

        // if(validateCustomerAddressId(customerOrder.getCustomerId(),customerOrder.getShippingAddressId()))
        // {

        if (customerOrder.getItemPricePerUnit() != 0)
        {

            loadCustomerOrderDetails(customerOrder);
        }
        else
        {
            throw new IllegalArgumentException("Not a valid item ",
                    new Exception("Invalid Pricing"));
        }

        // }
        // else
        // {
        // throw new
        // IllegalArgumentException("No customer found for requested Customer and address id",
        // new Exception("Invalid Params"));
        // }

    }

    private static void loadCustomerOrderDetails(CustomerOrder customerOrder)
            throws ClassNotFoundException, IOException, SQLException
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            logging.debug(" Start of CustomerOrder : ");
            DBConnectionFactory dbFactory = DBConnectionFactory.getInstance();
            conn = dbFactory.getConnection();
            long queryStartTime = System.nanoTime();

            queryStartTime = System.nanoTime();
            String sql = buildInsertQuery(customerOrder);
            statement = conn.prepareStatement(sql);
            statement = populateValues(customerOrder, statement);
            rs = statement.executeQuery();

            long queryEndTime = System.nanoTime();
            logging.info("Total_time_PS_loadCustOrder to execute loadCustomerOrder:="
                    + TimeUnit.MILLISECONDS.convert((queryEndTime - queryStartTime),
                            TimeUnit.NANOSECONDS));

            logging.debug(" End of CustomerOrder: ");
        }
        finally
        {
            if (rs != null)
                rs.close();
            if (statement != null)
                statement.close();
            if (conn != null)
                conn.close();
        }
    }

    private static PreparedStatement populateValues(CustomerOrder customerOrder,
            PreparedStatement statement) throws SQLException
    {
        statement.setString(1, customerOrder.getOrderNumber());
        statement.setString(2, customerOrder.getCustomerId());
        // TODO -- dynamic
        int i = 3;
        if (isValidCriteria(customerOrder.getItemNumber()))
        {
            statement.setString(i, customerOrder.getItemNumber());
            i++;
        }
        if (isValidCriteria(customerOrder.getItemName()))
        {
            statement.setString(i, customerOrder.getItemName());
            i++;
        }
        if (isValidCriteria(customerOrder.getItemDescription()))
        {
            statement.setString(i, customerOrder.getItemDescription());
            i++;
        }
        if (isValidCriteria(customerOrder.getItemQuantity()))
        {
            statement.setInt(i, customerOrder.getItemQuantity());
            i++;
        }
        if (isValidCriteria(customerOrder.getItemPricePerUnit()))
        {
            statement.setFloat(i, customerOrder.getItemPricePerUnit());
            i++;
            statement.setFloat(i, customerOrder.getTotalPrice());
        }
        return statement;
    }

    private static String buildInsertQuery(CustomerOrder customerorder)
    {
        StringBuilder sb = new StringBuilder("INSERT INTO customer_order.customer_Order (ID ");
        StringBuilder valueSb = new StringBuilder("values ( ?");
        sb.append(", CUSTOMERID");
        valueSb.append(",?");
        if (isValidCriteria(customerorder.getItemNumber()))
        {
            sb.append(", itemid");
            valueSb.append(",?");
        }
        if (isValidCriteria(customerorder.getItemName()))
        {
            sb.append(", itemname");
            valueSb.append(",?");
        }
        if (isValidCriteria(customerorder.getItemDescription()))
        {
            sb.append(", itemdescription");
            valueSb.append(",?");
        }
        if (isValidCriteria(customerorder.getItemQuantity()))
        {
            sb.append(", ItemQuantity");
            valueSb.append(",?");
        }
        if (isValidCriteria(customerorder.getItemPricePerUnit()))
        {
            sb.append(", ItemPricePerUnit");
            valueSb.append(",?");

            sb.append(", totalprice");
            valueSb.append(",?");

        }
        valueSb.append(")");
        sb.append(") " + valueSb.toString());

        return sb.toString();
    }

    public static boolean isValidCriteria(Object criteria)
    {
        boolean isValid = false;
        if ((criteria != null) && (!criteria.equals("")))
        {
            isValid = true;
        }
        return isValid;
    }

}
