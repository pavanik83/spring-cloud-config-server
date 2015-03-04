package com.wdpr.ee.authz.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/***************************************************************************************************
 * FileName - AuthConfig.java Desc: Authentication Config class to read and
 * populate ip,host, context path etc. (c) Disney. All rights reserved.
 *
 * $Author: $nixon $Revision: $ $Change: $ $Date: $
 ********************************************************************************************************/
public class AuthConfig
{

    private static AuthConfig instance;
    private static final List<String> propKeys = new ArrayList<String>();
    private static final Map<String, String> propertyMap = new HashMap<String, String>();
    public final String propFileName = "auth-config.properties";

    private AuthConfig()
    {
        loadAppLogEntryValues(propFileName);
    }

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
    public Map<String, String> getPropertyMap()
    {
        return propertyMap;
    }

    public String getPropertyVal(String key)
    {
        return propertyMap.get(key);
    }

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

    private void loadAppLogEntryValues(final String propFileName)
    {

        Properties props = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

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
            }
            System.out.println(" property invoked");

        }
        catch (IOException | NullPointerException | IllegalArgumentException e)
        {

        }

    }

}
