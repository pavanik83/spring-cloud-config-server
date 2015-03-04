package com.wdpr.ee.service.test;

import com.wdpr.ee.service.customer.customerorder.db.DBConnectionFactory;
import com.wdpr.ee.service.customer.customerorder.util.StringUtil;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author georn021
 *
 */
public class SampleRestClient
{
    private static final Logger LOG = LogManager.getLogger(SampleRestClient.class);
    /** 8080 */
    private static final int PORT = 8080;
    /** localhost */
    public static final String HOST = "localhost";
    /** /CustomerOrder/customer-services/customer-create */
    public static final String PATH1 = "/CustomerOrder/customer-services/customer-create";
    /** /CustomerOrder/customer-services/customer-order */
    public static final String PATH2 = "/CustomerOrder/customer-services/customer-order";
    /** null */
    public static BufferedReader nameReader = null;
    /** null */
    public static BufferedReader addressReader = null;

    /** 3 */
    public static int COUNTOFCUSTOMER = 3;
    /** 5 */
    public static int COUNTOFORDER = 5;

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            nameReader = new BufferedReader(new FileReader("src/main/resources/names.txt"));
            String line = null;
            while ((line = nameReader.readLine()) != null)
            {
                String[] names = line.split(",");
                firstName.add(names[0]);
                lastName.add(names[1]);

            }
            addressReader = new BufferedReader(new FileReader("src/main/resources/address.txt"));
            while ((line = addressReader.readLine()) != null)
            {
                String[] names = line.split(",");
                addressLine1.add(names[0]);
                city.add(names[1]);
                state.add(names[2]);
                zip.add(names[3]);

            }
            gender.add("M");
            gender.add("F");
            loadCustomerAddressIds(COUNTOFORDER);
            loadinventory(COUNTOFORDER);
        }
        catch (FileNotFoundException e)
        {
            LOG.error(e);
        }
        catch (IOException e)
        {
            LOG.error(e);
        }
        catch (ClassNotFoundException e)
        {
            LOG.error(e);
        }
        catch (SQLException e)
        {
            LOG.error(e);
        }
        createCust();
        createOrder();
    }

    public static void createCust()
    {
        for (int i = 0; i < COUNTOFCUSTOMER; i++)
        {
            try
            {
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();

                URIBuilder builder = new URIBuilder();
                builder.setScheme("http")
                        .setHost(HOST)
                        .setPort(PORT)
                        .setPath(PATH1)
                        .setParameter("firstName",
                                firstName.get(randomGenerator.nextInt(firstName.size())));

                builder.setParameter("lastName",
                        lastName.get(randomGenerator.nextInt(lastName.size())));
                builder.setParameter("gender", gender.get(randomGenerator.nextInt(gender.size())));
                // StringUtil.generateRandomString(6));
                builder.setParameter("firstAddressLine",
                        addressLine1.get(randomGenerator.nextInt(addressLine1.size())).trim());
                builder.setParameter("city", city.get(randomGenerator.nextInt(city.size())).trim());
                builder.setParameter("state", state.get(randomGenerator.nextInt(state.size()))
                        .trim());
                builder.setParameter("zip", zip.get(randomGenerator.nextInt(zip.size())).trim());

                // HttpGet get = new HttpGet(builder.build());

                HttpGet getRequest = new HttpGet(builder.build());

                // TODO : to set appropriate header
                loadHeader(getRequest);

                LOG.info(i + ">>> uri" + getRequest.getURI());
                HttpResponse response = httpClient.execute(getRequest);

                if (response.getStatusLine().getStatusCode() != 200)
                {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + response.getStatusLine().getStatusCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (response.getEntity().getContent())));

                LOG.info("br --" + br.toString());

                String output;

                StringBuffer sb = new StringBuffer();
                LOG.info(i + ">>> Output from Server .... \n");
                while ((output = br.readLine()) != null)
                {
                    LOG.info(output);
                    sb.append(output);

                }

                LOG.info(" SB---" + sb.toString());

                httpClient.close();
            }
            catch (URISyntaxException ex)
            {
                LOG.error(ex);
            }
            catch (ClientProtocolException e)
            {
                LOG.error(e);
            }
            catch (IOException e)
            {
                LOG.error(e);
            }
        }
    }

    public static void createOrder()
    {
        for (int i = 0; i < COUNTOFORDER; i++)
        {
            try
            {
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                URIBuilder builder = new URIBuilder();
                String[] ids = customerIdShippingId.get(i).split(";");
                builder.setScheme("http").setHost(HOST).setPort(PORT).setPath(PATH2)
                        .setParameter("customerId", ids[1]);

                builder.setParameter("shippingAddressId", ids[0]);
                builder.setParameter("itemNumber", "" + inventory.get(i));
                builder.setParameter("itemQuantity", "" + StringUtil.generateRandomInt(1));

                HttpGet getRequest = new HttpGet(builder.build());

                loadHeader(getRequest);
                LOG.info(i + ">>>createOrder uri" + getRequest.getURI());
                HttpResponse response = httpClient.execute(getRequest);

                if (response.getStatusLine().getStatusCode() != 200)
                {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + response.getStatusLine().getStatusCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (response.getEntity().getContent())));

                String output;

                LOG.info(i + ">>> Output from Server .... \n");
                while ((output = br.readLine()) != null)
                {
                    LOG.info(output);
                }

                LOG.info("\n output Br" + br.toString());
                httpClient.close();
            }
            catch (URISyntaxException ex)
            {
                LOG.error(ex);
            }
            catch (ClientProtocolException e)
            {
                LOG.error(e);
            }
            catch (IOException e)
            {
                LOG.error(e);
            }
        }
    }

    // Correlation-Id
    // public static final String TRANS_ID = "Transaction-Id";
    static void loadHeader(HttpGet getRequest)
    {
        getRequest.setHeader("x-disney-internal-conversation-id", UUID.randomUUID().toString());
        String corrid = UUID.randomUUID().toString();
        getRequest.setHeader("Correlation-Id", corrid);
        LOG.info("Correlation id is " + corrid);
        getRequest.setHeader("X-CorrelationId", corrid);
        getRequest.setHeader("x-disney-internal-correlation-id", corrid);
        getRequest.setHeader("x-disney-internal-page-id",
                new Integer(StringUtil.generateRandomInt(1)).toString());
        getRequest.setHeader("x-disney-internal-client-id",
                new Integer(StringUtil.generateRandomInt(3)).toString());
        String tranId = UUID.randomUUID().toString();
        LOG.info("tranId is " + tranId);
        getRequest.setHeader("x-disney-internal-transaction-id", tranId);
        getRequest.setHeader("Transaction-Id", tranId);

    }

    private static void loadCustomerAddressIds(Integer c) throws ClassNotFoundException,
            IOException, SQLException
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            DBConnectionFactory dbFactory = DBConnectionFactory.getInstance();
            conn = dbFactory.getConnection();
            String sql = "select id, customerid from " + "(select * "
                    + "from customer_order.address " + "order by DBMS_RANDOM.RANDOM)"
                    + " where rownum <= ?";
            statement = conn.prepareStatement(sql);
            statement.setInt(1, c);
            rs = statement.executeQuery();
            String custshippingid = "";
            while (rs.next())
            {
                custshippingid = rs.getString("ID");
                custshippingid = custshippingid + ";" + rs.getString("customerid");
                customerIdShippingId.add(custshippingid);
            }
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

    private static void loadinventory(Integer c) throws ClassNotFoundException, IOException,
            SQLException
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            DBConnectionFactory dbFactory = DBConnectionFactory.getInstance();
            conn = dbFactory.getConnection();

            String sql = "select SLNO from (select * from customer_order.inventory order by DBMS_RANDOM.RANDOM) where rownum <=?";
            statement = conn.prepareStatement(sql);
            statement.setInt(1, c);
            rs = statement.executeQuery();
            while (rs.next())
            {
                inventory.add(rs.getString("SLNO"));
            }

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

    public static ArrayList<String> firstName = new ArrayList<>();
    public static ArrayList<String> lastName = new ArrayList<>();
    public static ArrayList<String> addressLine1 = new ArrayList<>();
    public static ArrayList<String> city = new ArrayList<>();
    public static ArrayList<String> state = new ArrayList<>();
    public static ArrayList<String> zip = new ArrayList<>();
    public static ArrayList<String> gender = new ArrayList<>();
    public static ArrayList<String> customerIdShippingId = new ArrayList<>();
    public static ArrayList<String> inventory = new ArrayList<>();
    private static final Random randomGenerator = new Random();
}
