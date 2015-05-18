package com.wdpr.ee.authz;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.wdpr.ee.authz.util.AuthConstants;
/**
 * @author sanwa001
 *
 */
public class AuthFilterBearerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	@Test
    public void testDoFilterWithBearerToken() throws IOException, ServletException
    {
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.addHeader(AuthConstants.AUTHORIZATIONS, "BEARER HUX8dgEzP91Z4RgTrCuMfw");
    	request.addHeader("token_type", "BEARER");
    	request.setContextPath("/customer-service");
    	MockHttpServletResponse response	=	new MockHttpServletResponse();
    	MockFilterChain chain = new MockFilterChain();
        AuthFilter filter = new AuthFilter();
        filter.doFilter(request, response, chain);
    }
}
