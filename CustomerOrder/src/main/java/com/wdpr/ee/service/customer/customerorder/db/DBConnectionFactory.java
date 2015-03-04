package com.wdpr.ee.service.customer.customerorder.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author KONDP001
 *
 */
public class DBConnectionFactory {
    private static final Logger logger = LogManager.getLogger(DBConnectionFactory.class);
    private static final String configFilePath    = "db.properties";
    final Properties prop = new Properties();
    private static DataSource dataSource;

    /**
     * Private constructor
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private DBConnectionFactory ()
    {
        long dataSourceStartTime = System.nanoTime();
        try{
            dataSource = configureDataSoruce();
        }catch(Exception e){
            logger.fatal("Unable to create DB dataSource");
        }
        long dataSourceEndTime = System.nanoTime();
        logger.info("Total time taken to configureDataSoruce :="+TimeUnit.MILLISECONDS.convert((dataSourceEndTime-dataSourceStartTime), TimeUnit.NANOSECONDS));
    }

    private static class DBConnectionFactoryHelper{
            private static final DBConnectionFactory INSTANCE = new DBConnectionFactory();
    }

    /**
     *
     * @return
     */
    public static DBConnectionFactory getInstance(){
        return DBConnectionFactoryHelper.INSTANCE;
    }
    /**
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
//    private Connection createConnection() throws IOException, ClassNotFoundException, SQLException{
//        logger.info("Inside DBConnectionFactory getConnection method");
//        if (dataSource != null)
//        {
//            return dataSource.getConnection();
//        }
//        throw new SQLException("dataSource is null, please initialize");
//    }
//
    /**
     *
     * @param dbType
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Connection getConnection() throws ClassNotFoundException, IOException, SQLException{
        logger.debug("Inside DBConnectionFactory getConnection method");
        long getConnStartTime = System.nanoTime();
        if (dataSource != null)
        {
            Connection conn = dataSource.getConnection();
            long getConnEndTime = System.nanoTime();
            logger.debug("Total time taken to getConnection :="+TimeUnit.MILLISECONDS.convert((getConnEndTime-getConnStartTime), TimeUnit.NANOSECONDS));
            return conn;
        }
        throw new SQLException("dataSource is null, please initialize");
    }

    /**
     * Load JDBC Driver class
     * Creates an instance of GenericObjectPool that holds our
     * pool of connections object.
     * Creates a connection factory object which will be use by
     * the pool to create the connection object. We passes the
     * JDBC url info, username and password.
     * Creates a PoolableConnectionFactory that will wraps the
     * connection object created by the ConnectionFactory to add
     * object pooling functionality.
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws SQLException
     */

    private DataSource configureDataSoruce() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        logger.debug("Inside DBConnectionFactory configureDataSoruce method");
        prop.load(DBConnectionFactory.class.getClassLoader().getResourceAsStream(configFilePath));
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(prop.getProperty("DB_DRIVER"));
        ds.setUsername(prop.getProperty("DB_USER"));
        ds.setPassword(prop.getProperty("DB_PASSWORD"));
        ds.setUrl(prop.getProperty("DB_CONNECTION"));
        ds.setInitialSize(Integer.valueOf(prop.getProperty("maxTotal")));
        return ds;

       }
}
