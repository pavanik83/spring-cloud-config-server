package com.wdpr.ee.authz;

import com.wdpr.ee.authz.util.AuthConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Test class for Security Check
 */
public class SecurityCheckMain
{
    private static final Logger LOG = LogManager.getLogger(SecurityCheckMain.class);

    /**
     *
     */
    @Test
    public void testAuthConfig()
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
            while (i<20)
            {
                Map<String, String> tokenList = new HashMap<>();
                String token = generateSessionKey();
                tokenList.put("access_token", token);
                LOG.info(++i + " " + token);
                String resp = connector.callGoDotComValidateToken(tokenList);
                if (resp != null)
                {
                    LOG.info("Authenticated");
                }
                i++;
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
