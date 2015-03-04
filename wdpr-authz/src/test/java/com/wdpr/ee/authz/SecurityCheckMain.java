package com.wdpr.ee.authz;

import com.wdpr.ee.authz.util.AuthConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Test class for Security Check
 */
public class SecurityCheckMain
{
    private static final Logger LOG = LogManager.getLogger(SecurityCheckMain.class);

    public static void main(String[] args)
    {
        AuthConfig config = AuthConfig.getInstance();
        RestConnector connector = RestConnector.getInstance();
        for (String key : config.getPropkeys())
        {
            LOG.info("key" + key + ":" + config.getPropertyMap().get(key));
        }
        try
        {
            int i = 0;
            while (true)
            {
                Map<String, String> tockenList = new HashMap<>();
                String token = generateSessionKey();
                tockenList.put("access_token", token);
                LOG.info(++i + " " + token);
                if (connector.callGoDotComValidateToken(tockenList))
                {
                    LOG.info("Suthenticated");
                }
            }
        }
        catch (Exception e)
        {
            LOG.error(e);
        }
    }

    /**
     * @return session key
     */
    public static String generateSessionKey()
    {
        String alphabet = new String(
                "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); // 9
        int n = alphabet.length(); // 10
        int length = "Un6LHVsOtTphTEANANz0UQ".length();
        String result = new String();
        Random r = new Random(); // 11

        for (int i = 0; i < length; i++)
            // 12
            result = result + alphabet.charAt(r.nextInt(n)); // 13
        // LOG.info(result);
        return result;
    }
}
