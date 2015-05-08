package com.wdpr.ee.authz.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/***************************************************************************************************
 * FileName - AuthConfig.java Desc: Authentication Config class to read and
 * populate ip,host, context path etc. (c) Disney. All rights reserved.
 *
 * $Author: $nixon $Revision: $ $Change: $ $Date: $
 ********************************************************************************************************/
public class AuthConfig
{
    private static final Logger LOG = LogManager.getLogger(AuthConfig.class);
    private static AuthConfig instance;
    private static final List<String> propKeys = new ArrayList<>();
    private static final Map<String, String> propertyMap = new HashMap<>();
    /**
     * auth-config.properties
     */
    public final String propFileName = "auth-config.properties";

    private AuthConfig()
    {
        loadAppLogEntryValues(this.propFileName);
    }

    /**
     * @return singleton instance of AuthConfig
     */
    public static AuthConfig getInstance()
    {
        if (instance == null)
        {
            instance = new AuthConfig();
        }
        return instance;
    }

    static
    {
        propKeys.add(AuthConstants.PROTOCOL);
        propKeys.add(AuthConstants.HOST);
        propKeys.add(AuthConstants.PORT);
        propKeys.add(AuthConstants.AUTH_CTX_PATH);
        propKeys.add(AuthConstants.SCOPE_CTX_PATH);
        propKeys.add(AuthConstants.TIME_OUT);
    }

    // static{
    // Map<String, String> propertyMap = new HashMap<String, String>();
    // propertyMap.put(LoggerConstants.APP_NAME, "DefaultApplication");
    // propertyMap.put(LoggerConstants.VERSION, "1.0");
    // defaultPropertyMap= propertyMap;
    // }
    /**
     * @return property map
     */
    public Map<String, String> getPropertyMap()
    {
        return propertyMap;
    }

    /**
     * @param key
     * @return property value for key
     */
    public String getPropertyVal(String key)
    {
        return propertyMap.get(key);
    }

    /**
     * @return all property keys
     */
    public List<String> getPropkeys()
    {
        return propKeys;
    }

    /**
     * Loads application-specific log entry values from this app's
     * appLogEntryValues.properties file. These values will be added to all log
     * events sent by this appender The from either an application-supplied or a
     * globally shared external properties file should be supplied by the system
     * at deployment time. The file name should be set as plug-in attribute for
     * this appender via the 'propFileName' attribute.
     *
     * @propFileName Name of the property file to fetch the app-specific values
     *               from
     *
     */

    private void loadAppLogEntryValues(final String propFileNameParam)
    {

        Properties props = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileNameParam);
        try
        {
            if (inputStream != null)
            {
                props.load(inputStream);
                for (String key : propKeys)
                {
                    String value = props.getProperty(key);
                    propertyMap.put(key, value);

                }
                inputStream.close();
            }
//            LOG.debug(" property invoked");

        }
        catch (IOException | NullPointerException | IllegalArgumentException e)
        {
            LOG.error(e);
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (Exception ex)
                {
                    LOG.error(ex);
                }
            }
        }
    }
}
