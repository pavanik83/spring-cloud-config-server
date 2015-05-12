/**
 *
 */
package com.wdpr.ee.authz;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author georn021
 *
 */
public class RestConnectorTest
{
    /**
     * token list
     */
    Map<String, String> tokenList = new HashMap<>();
    Map<String, String> tokenListBlank = new HashMap<>();
    Map<String, String> bearertokenList = new HashMap<>();

    /**
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        // No setup required
    }

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {
        this.tokenList.put("access_token", "BEARERHUX8dgEzP91Z4RgTrCuMfw");
        this.tokenList.put("token_type", "BEARER");
        this.tokenList.put("client_id", "WDPRO-NGE.PEPCOM-STAGE");
        this.tokenList.put("username", "api");
        this.tokenList.put("assertion_type", "public");
        
        
        
        this.bearertokenList.put("authorization", "BEARERHUX8dgEzP91Z4RgTrCuMfw");
        this.bearertokenList.put("token_type", "BEARER");
        this.bearertokenList.put("client_id", "WDPRO-NGE.PEPCOM-STAGE");
        this.bearertokenList.put("username", "api");
        this.bearertokenList.put("assertion_type", "public");
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception
    {
    	
    	
    }
    @Test
    public void testCallGoDotComGetBlankToken() throws IOException
    {
    	RestConnector connector	=	 RestConnector.getInstance();
    	connector.callGoDotComGet(tokenListBlank, "/validate/");
    }
    @Test
    public void testCallGoDotComGetwithbearer() throws IOException
    {
    	RestConnector connector	=	 RestConnector.getInstance();
    	connector.callGoDotComGet(bearertokenList, "/validate/");
    }
    /**
     * @throws IOException
     */
    @Test
    public void testcallGoDotComPost() throws IOException
    {
        RestConnector connector = Mockito.mock(RestConnector.class);
        //CloseableHttpClient httpClient =
            Mockito.mock(CloseableHttpClient.class);

        Mockito.doThrow(new RuntimeException()).when(connector)
                .callGoDotComValidateScope(this.tokenList);
    }

    /**
     * @throws IOException
     */
    @Test
    public void testcallGoDotCom() throws IOException
    {
        RestConnector connector = RestConnector.getInstance();
        CloseableHttpClient httpClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse response = Mockito.mock(CloseableHttpResponse.class);
        // HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpGet getRequest = Mockito.mock(HttpGet.class);

        Mockito.doReturn(response).when(httpClient).execute(getRequest);

        Mockito.when(httpClient.execute(getRequest)).thenReturn(response);
        // HttpResponse response = httpClient.execute(postRequest);
        connector.callGoDotComValidateScope(this.tokenList);

        // Mockito.doThrow(new
        // RuntimeException()).when(connector).callGoDotComValidateScope(tokenList);
        httpClient.close();
        response.close();
    }
}
