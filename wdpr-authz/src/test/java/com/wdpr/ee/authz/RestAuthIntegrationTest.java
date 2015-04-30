/**
 *
 */
package com.wdpr.ee.authz;

import static org.junit.Assert.*;
import com.wdpr.ee.authz.model.TokenDO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Integration Test to validate authorization token against live server
 */
public class RestAuthIntegrationTest
{
    private static final Logger LOG = LogManager.getLogger(RestAuthIntegrationTest.class);

    /**
     * token list
     */
    Map<String, String> validateTokens = new HashMap<>();
    Map<String, String> grantTokens = new HashMap<>();
    /**
     * RestConnector singleton instance
     */
    RestConnector connector = RestConnector.getInstance();

    /**
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
    }

    /**
     * @throws Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
    }

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {
        this.validateTokens.put("access_token", "1T2O31jShW10ThGX6dFpuQ");
        this.validateTokens.put("token_type", "BEARER");
        this.validateTokens.put("client_id", "WDPRO-NGE.PEPCOM-STAGE");
        this.validateTokens.put("username", "api");
        this.validateTokens.put("assertion_type", "public");
        this.grantTokens.put("grant_type", "client_credentials");
        this.grantTokens.put("client_id", "WDPRO-NGE.PEPCOM-STAGE");
        this.grantTokens.put("client_secret", "E2050034-0C95-11E1-872D-1BB84724019B");
        this.grantTokens.put("scope", "RETURN_ALL_CLIENT_SCOPES");
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.wdpr.ee.authz.RestConnector#callGoDotComValidateToken(java.util.Map)}.
     */
    // @Test
    public void testCallGoDotComValidateToken()
    {
        try
        {
            long now = System.currentTimeMillis();
            String rtn = this.connector.callGoDotComValidateToken(this.validateTokens);
            long elapsed1 = System.currentTimeMillis() - now;
            System.out.println(elapsed1 + " ms: " + rtn);
            // Verify that caching is working - return should be <10 ms
            for (int i=0; i<10; i++)
            {
                now = System.currentTimeMillis();
                rtn = this.connector.callGoDotComValidateToken(this.validateTokens);
                long elapsed = System.currentTimeMillis() - now;
                System.out.println(elapsed + " ms: " + rtn);
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }

    /**
     * Test method for {@link com.wdpr.ee.authz.RestConnector#callGoDotComValidateScope(java.util.Map)}.
     */
    //@Test
    public void testCallGoDotComValidateScope()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.wdpr.ee.authz.RestConnector#callGoDotComGet(java.util.Map, String)}.
     */
    //@Test
    public void testCallGoDotComGet()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.wdpr.ee.authz.RestConnector#callGoDotComPost(java.util.Map, String, Class)}.
     */
    @Test
    public void testCallGoDotComPost()
    {
        try
        {
            TokenDO token = this.connector.callGoDotComPost(this.grantTokens, "/token", TokenDO.class);
            LOG.info(token);
            if (token != null)
            {
                this.validateTokens.put("access_token", token.getAccess_token());
                long now = System.currentTimeMillis();
                String rtn = this.connector.callGoDotComValidateToken(this.validateTokens);
                long elapsed1 = System.currentTimeMillis() - now;
                System.out.println(elapsed1 + " ms: " + rtn);
                // Verify that caching is working - return should be <10 ms
                for (int i=0; i<10; i++)
                {
                    now = System.currentTimeMillis();
                    rtn = this.connector.callGoDotComValidateToken(this.validateTokens);
                    long elapsed = System.currentTimeMillis() - now;
                    System.out.println(elapsed + " ms: " + rtn);
                }
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }

}
