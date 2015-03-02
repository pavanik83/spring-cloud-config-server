/**
 * 
 */
package com.wdpr.ee.authz;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.wdpr.ee.authz.model.TokenDO;

/**
 * @author georn021
 *
 */
public class RestConnectorTest {
	Map<String, String> tokenList = new HashMap<>();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		tokenList.put("access_token", "uz3588_tywiTt0a9l9MROA");
		tokenList.put("client_id", "WDPRO-NGE.PEPCOM-STAGE");
		tokenList.put("username", "api");
		tokenList.put("assertion_type", "public");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

//	@Test
	public void testcallGoDotComPost() throws IOException {
		RestConnector connector = Mockito.mock(RestConnector.class);		
		CloseableHttpClient  httpClient = Mockito.mock(CloseableHttpClient.class);;

		Mockito.doThrow(new RuntimeException()).when(connector).callGoDotComValidateScope(tokenList);
		
	}

	@Test
	public void testcallGoDotCom() throws IOException {
		RestConnector connector = RestConnector.getInstance();	
		CloseableHttpClient  httpClient = Mockito.mock(CloseableHttpClient.class);;
		CloseableHttpResponse response = Mockito.mock(CloseableHttpResponse.class);
//		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpGet getRequest = Mockito.mock(HttpGet.class);
		
		Mockito.doReturn(response).when(httpClient).execute(getRequest);;
		
		Mockito.when(httpClient.execute(getRequest)).thenReturn( response);
//		HttpResponse response = httpClient.execute(postRequest);
		connector.callGoDotComValidateScope(tokenList);
		
		

//		Mockito.doThrow(new RuntimeException()).when(connector).callGoDotComValidateScope(tokenList);
		
	}	
	
}
