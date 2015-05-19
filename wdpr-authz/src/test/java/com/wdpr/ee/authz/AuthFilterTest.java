/**
 *
 */
package com.wdpr.ee.authz;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.wdpr.ee.authz.model.AuthDO;
import com.wdpr.ee.authz.model.TokenDO;
import com.wdpr.ee.authz.util.AuthConstants;
import com.wdpr.ee.authz.util.JSONConfigLoader;

/**
 * AuthFilterTest
 */

public class AuthFilterTest
{
    /**
     * tokenList
     */
	private static final Logger LOG = LogManager.getLogger(AuthFilter.class);
    Map<String, String> tokenList = new HashMap<>();
    Map<String, String> grantTokens = new HashMap<>();
    Map<String, String> bearertokenList = new HashMap<>();
    Map<String, AuthDO> scopeMap = JSONConfigLoader.getInstance()
			.loadScopeData();
    Map<String, String> cookieMap = new HashMap<>();;
    Pattern[] scopePatterns = new Pattern[scopeMap.size()];

    /**
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        // No setup
    }

    /**
     * @throws Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
        // No teardown
    }

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {
        this.tokenList.put(AuthConstants.AUTHORIZATION, "BEARER uz3588_tywiTt0a9l9MROA");
        this.tokenList.put("client_id", "WDPRO-NGE.PEPCOM-STAGE");
        this.tokenList.put("username", "api");
        this.tokenList.put("assertion_type", "public");
        this.tokenList.put("grant_type", "assertion");
        cookieMap.put("access_token", "uz3588_tywiTt0a9l9MROA");
        
        this.bearertokenList.put(AuthConstants.AUTHORIZATION, "BEARER HUX8dgEzP91Z4RgTrCuMfw");
        this.bearertokenList.put("token_type", "BEARER");
        this.bearertokenList.put("client_id", "WDPRO-NGE.PEPCOM-STAGE");
        this.bearertokenList.put("username", "api");
        this.bearertokenList.put("assertion_type", "public");
        
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
        // No teardown
    }

    /**
     * Tests the utility method for setting a correlation id by the client.
     * Creates an HTTP Filter and check to make sure the thread context
     * variables the filter is responsible for are set
     *
     * @throws IOException
     *             If the doFilter chokes
     * @throws ServletException
     *             If the doFilter chokes
     */
    @Test
    public void testDoFilter() throws IOException, ServletException
    {
        // Mock the HTTP Servlet objects used by the filter
    	MockHttpServletRequest request = new MockHttpServletRequest();
        //HttpSession session = Mockito.mock(HttpSession.class);
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	MockFilterChain chain = new MockFilterChain();
        AuthFilter mockFilter = Mockito.mock(AuthFilter.class);
        request.addHeader(AuthConstants.AUTHORIZATION, "BEARER Un6LHVsOtTphTEANANz0UQ");
        Mockito.doReturn(this.tokenList).when(mockFilter).loadHeaders(request);
        mockFilter.doFilter(request, response, chain);
        assertNotNull(response.getStatus());
    }
    /**
     * 
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testDoFilterWithContextMatch() throws IOException, ServletException
    {
    	MockHttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);
        StringBuilder tokens	=	 new StringBuilder();
    	tokens.append("BEARER ");
    	tokens.append(RestConnector.getInstance().callGoDotComPost(this.grantTokens, "/token", TokenDO.class).getAccess_token());
        request.addHeader(AuthConstants.AUTHORIZATION, tokens.toString());
        request.setContextPath("/customer-service");
        AuthFilter filter = new AuthFilter();
        filter.doFilter(request, response, chain);
        assertNotNull(response.getStatus());
    }
    
    @Test
    public void testDoFilterWithData() throws IOException, ServletException
    {
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	MockHttpServletResponse response	=	new MockHttpServletResponse();
    	MockFilterChain chain = new MockFilterChain();
    	request.setContextPath("/customer-service");
    	request.setMethod("GET");
    	StringBuilder tokens	=	 new StringBuilder();
    	tokens.append("BEARER ");
    	tokens.append(RestConnector.getInstance().callGoDotComPost(this.grantTokens, "/token", TokenDO.class).getAccess_token());
        request.addHeader(AuthConstants.AUTHORIZATION, tokens.toString());
        AuthFilter filter = new AuthFilter();
        filter.doFilter(request, response, chain);
    }
    
    @Test
    public void testinit()
    {
    	MockFilterConfig	mockFilterConfig	=	new MockFilterConfig();
    	AuthFilter filter = new AuthFilter();
    	filter.init(mockFilterConfig);
    }
    @Test
    public void testloadCookieData() 
    {
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setContextPath("/customer-service");
    	Cookie	cookies	=	Mockito.mock(Cookie.class);
    	request.setCookies(cookies);
        AuthFilter filter = new AuthFilter();
        filter.loadCookieData(request, cookieMap);
    }

    /**
     *
     */
    @Test
    public void testLodaHeaders()
    {
    	
        AuthFilter filter = Mockito.mock(AuthFilter.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("access_token", "uz3588_tywiTt0a9l9MROA");
        Map<String, String> tokenListMock	=	filter.loadHeaders(request);
        assertNotNull(tokenListMock);
    }
    private void loadScopePatterns() {

		StringBuilder msg = new StringBuilder();
		int count = 0;
		for (String scope : this.scopeMap.keySet()) {
			scopePatterns[count] = Pattern.compile(scope);

			msg.append("#### Loaded required scope ");
			msg.append(scope);
			LOG.debug(msg.toString());
			count++;
		}
		return;
	}
	/**
	 * finds the scopes defined in the scope.json document that match/apply to
	 * the incoming request URL context, if any.
	 * 
	 * @param reqCtx
	 *            The context of the URL for the incoming request
	 * @return A collection of scopes for the incoming URL to be validated
	 *         against the token for the incoming request or null if no matching
	 *         scopes found
	 */
	private AuthDO loadScopeItem(String reqCtx) {
		AuthDO scopeItem = null;
		StringBuilder msg = new StringBuilder();
		loadScopePatterns();
		for (int i = 0; i < scopePatterns.length; i++) {
			msg.delete(0, msg.length());
			String scopeKey = scopePatterns[i].pattern();
			if (scopePatterns[i].matcher(reqCtx).matches()) {
				scopeItem = this.scopeMap.get(scopeKey);
				msg.append("#### Matched the incoming request context ");
				msg.append(reqCtx);
				msg.append(" with the configured scope context ");
				msg.append(scopeKey);
				LOG.debug(msg.toString());
				return scopeItem;
			} else {
				msg.append("#### Unsuccessful match of configured scope context");
				msg.append(scopeKey);
				msg.append(" with the incoming request context ");
				msg.append(reqCtx);
				LOG.debug(msg.toString());
			}
		}
		return scopeItem;
	}
	
	
}
