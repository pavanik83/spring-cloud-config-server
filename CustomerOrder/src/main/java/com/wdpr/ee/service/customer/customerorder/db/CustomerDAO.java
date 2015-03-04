package com.wdpr.ee.service.customer.customerorder.db;

import com.wdpr.ee.service.customer.customerorder.model.Customer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomerDAO
{
    private static final Logger logging = LogManager.getLogger(CustomerDAO.class);

    public void loadCustomer(Customer customer) throws SQLException, ClassNotFoundException,
            IOException, Exception
    {

        loadCustomerDetails(customer);

    }

    // @WdprMethodLog
    private static void loadCustomerDetails(Customer customer) throws ClassNotFoundException,
            IOException, SQLException, Exception
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            logging.debug(" Start loadCustomer");
            DBConnectionFactory dbFactory = DBConnectionFactory.getInstance();
            conn = dbFactory.getConnection();
            long queryStartTime = System.nanoTime();

            queryStartTime = System.nanoTime();
            String sql = buildInsertQuery(customer);
            statement = conn.prepareStatement(sql);
            statement = populateValues(customer, statement);
            rs = statement.executeQuery();
            long queryEndTime = System.nanoTime();
            logging.info("Total_time_PS_loadCustomer : taken to execute loadCustomer query:="
                    + TimeUnit.MILLISECONDS.convert((queryEndTime - queryStartTime),
                            TimeUnit.NANOSECONDS));

            logging.debug(" End time of loadCustomer Query : ");
        }
        catch (Exception ex)
        {
            logging.error(ex.getLocalizedMessage());
            throw ex;

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

    private static PreparedStatement populateValues(Customer customer, PreparedStatement statement)
            throws SQLException
    {
        statement.setString(1, customer.getCustomerId());
        // TODO -- dynamic
        int i = 2;
        if (isValidCriteria(customer.getFirstName()))
        {
            statement.setString(i, customer.getFirstName());
            i++;
        }
        if (isValidCriteria(customer.getLastName()))
        {
            statement.setString(i, customer.getLastName());
            i++;
        }
        if (isValidCriteria(customer.getMiddleName()))
        {
            statement.setString(i, customer.getMiddleName());
            i++;
        }
        if (isValidCriteria(customer.getNamePrefix()))
        {
            statement.setString(i, customer.getNamePrefix());
            i++;
        }
        if (isValidCriteria(customer.getGender()))
        {
            statement.setString(i, customer.getGender());
            i++;
        }
        return statement;
    }

    private static String buildInsertQuery(Customer customer)
    {
        StringBuilder sb = new StringBuilder("INSERT INTO customer_order.customer (ID ");
        StringBuilder valueSb = new StringBuilder("values ( ?");

        if (isValidCriteria(customer.getFirstName()))
        {
            sb.append(", FIRSTNAME");
            valueSb.append(",?");
        }
        if (isValidCriteria(customer.getLastName()))
        {
            sb.append(", LASTNAME");
            valueSb.append(",?");
        }
        if (isValidCriteria(customer.getMiddleName()))
        {
            sb.append(", MIDDLENAME");
            valueSb.append(",?");
        }
        if (isValidCriteria(customer.getNamePrefix()))
        {
            sb.append(", PREFIX");
            valueSb.append(",?");
        }
        if (isValidCriteria(customer.getGender()))
        {
            sb.append(", gender");
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
