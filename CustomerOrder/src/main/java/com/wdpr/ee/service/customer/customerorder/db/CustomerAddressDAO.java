package com.wdpr.ee.service.customer.customerorder.db;

import com.wdpr.ee.loggingapi.annotation.WdprMethodLog;
import com.wdpr.ee.service.customer.customerorder.model.Customer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CustomerAddressDAO extends CustomerDAO {

    private static final Logger logging =  LogManager.getLogger(CustomerAddressDAO.class);

    public void loadCustomerAddress(Customer customer) throws SQLException, ClassNotFoundException, IOException {
        if(customer.getAddress()!=null)
        {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs =null;
        try
        {

            logging.debug(" Start  of load customerAddres : ");
            DBConnectionFactory dbFactory = DBConnectionFactory.getInstance();
            conn = dbFactory.getConnection();
            long queryStartTime = System.nanoTime();
            String sql = buildAddressInsertQuery(customer);
            statement = conn.prepareStatement(sql);
            statement =populateAddressValues(customer, statement);
            statement.executeUpdate();
            long queryEndTime  = System.nanoTime();
            logging.info("Total_time_PS_loadCustomerAddress : Total time taken to execute customerAddres :="+TimeUnit.MILLISECONDS.convert((queryEndTime-queryStartTime), TimeUnit.NANOSECONDS));

            logging.debug(" End of load customerAddres Query ");
        }
        finally
        {
            if(rs!=null)
             rs.close();
            if(statement!=null)
            statement.close();
            if(conn!=null)
            conn.close();
        }
        }

    }

    private PreparedStatement populateAddressValues(Customer customer,
            PreparedStatement statement) throws SQLException {

        statement.setString(1, customer.getAddress().getUuid());
        statement.setString(2, customer.getCustomerId());
        //TODO -- dynamic
        int i =3;
        if(isValidCriteria(customer.getAddress().getFirstAddressLine()))
        {
        statement.setString(i, customer.getAddress().getFirstAddressLine());
        i++;
        }
        if(isValidCriteria(customer.getAddress().getSecondAddressLine()))
        {
        statement.setString(i, customer.getAddress().getSecondAddressLine());
        i++;
        }
        if(isValidCriteria(customer.getAddress().getCity()))
        {
        statement.setString(i, customer.getAddress().getCity());
        i++;
        }
        if(isValidCriteria(customer.getAddress().getZip()))
        {
        statement.setString(i, customer.getAddress().getZip());
        i++;
        }
        if(isValidCriteria(customer.getAddress().getState()))
        {
        statement.setString(i, customer.getAddress().getState());
        i++;
        }
        return statement;
    }

    private String buildAddressInsertQuery(Customer customer) {

        StringBuilder sb = new StringBuilder("INSERT INTO address (ID ");
        StringBuilder valueSb = new StringBuilder("values ( ?");

        sb.append(", CUSTOMERID");
        valueSb.append(",?");

        if(isValidCriteria(customer.getAddress().getFirstAddressLine()))
        {
            sb.append(", ADDRESS_LINE1");
            valueSb.append(",?");
        }
        if(isValidCriteria(customer.getAddress().getSecondAddressLine()))
        {
            sb.append(", ADDRESS_LINE2");
            valueSb.append(",?");
        }
        if(isValidCriteria(customer.getAddress().getCity()))
        {
            sb.append(", city");
            valueSb.append(",?");
        }
        if(isValidCriteria(customer.getAddress().getZip()))
        {
            sb.append(", zip");
            valueSb.append(",?");
        }
        if(isValidCriteria(customer.getAddress().getState()))
        {
            sb.append(", state");
            valueSb.append(",?");
        }
        valueSb.append(")");
        sb.append(") "+valueSb.toString());

        return sb.toString();    }


    @WdprMethodLog
    public boolean validateCustomerAddressId(String customerId,
            String shippingAddressId) throws SQLException, ClassNotFoundException, IOException {

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs =null;
        try
        {

            logging.debug(" Start  of validateCustomerAddress : ");
            DBConnectionFactory dbFactory = DBConnectionFactory.getInstance();
            conn = dbFactory.getConnection();
            long queryStartTime = System.nanoTime();

            Integer countOfRows =0;
            queryStartTime = System.nanoTime();
            String sql = "SELECT COUNT(*) FROM ADDRESS WHERE CUSTOMERID = ? AND  ID =  ?" ;
            statement = conn.prepareStatement(sql);
            statement.setString(1,customerId);
            statement.setString(2, shippingAddressId);
            rs = statement.executeQuery();
            while(rs.next())
            {
                countOfRows =    rs.getInt(1);
            }
            long queryEndTime = System.nanoTime();
            logging.info("Total_time_PS_val_CustomerAddress taken to process validateCustomerAddress :="+TimeUnit.MILLISECONDS.convert((queryEndTime-queryStartTime), TimeUnit.NANOSECONDS));

            logging.debug(" End  of validateCustomerAddress Query : ");
            if(countOfRows ==1)
                return true;
            else
                //Disabled validation
                return true;
        }
        finally
        {
            if(rs!=null)
             rs.close();
            if(statement!=null)
            statement.close();
            if(conn!=null)
            conn.close();
        }


    }

}
