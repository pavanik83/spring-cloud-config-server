package com.wdpr.ee.authz;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.wdpr.ee.authz.model.TokenDO;
import com.wdpr.ee.authz.util.AuthConstants;

/**
 * @author sanwa001
 *
 */
public class AuthFilterNullTokenTest
{
 Map<String, String> tokenList = new HashMap<>();
 @Before
 public void setUp() throws Exception
 {
  this.tokenList.put("grant_type", "client_credentials");
  this.tokenList.put("client_id", "WDPRO-NGE.PEPCOM-STAGE");
  this.tokenList.put("client_secret", "E2050034-0C95-11E1-872D-1BB84724019B");
  this.tokenList.put("scope", "RETURN_ALL_CLIENT_SCOPES");
 }

 @After
 public void tearDown() throws Exception
 {
 }

 @Test
 public void testDoFilterWithNullToken() throws IOException, ServletException
 {
  // AuthFilter filter = PowerMockito.mock(AuthFilter.class);
  MockHttpServletRequest request = new MockHttpServletRequest();
  request.setContextPath("/customer-service");
  MockHttpServletResponse response = new MockHttpServletResponse();
  MockFilterChain chain = new MockFilterChain();
  AuthFilter filter = new AuthFilter();
  filter.doFilter(request, response, chain);
 }

 @Test
 public void testDoFilterKeyStoneToken() throws IOException, ServletException
 {
  MockHttpServletRequest request = new MockHttpServletRequest();
  request.setContextPath("/customer-service");
  request.setMethod("GET");
  MockHttpServletResponse response = new MockHttpServletResponse();
  MockFilterChain chain = new MockFilterChain();
  StringBuilder tokens = new StringBuilder();
  tokens.append(RestConnector.getInstance()
    .callGoDotComPost(tokenList, "/token", TokenDO.class).getAccess_token());
  Cookie cookie = new Cookie(AuthConstants.ACCESS_TOKEN, tokens.toString());
  cookie.setValue(tokens.toString());
  request.setCookies(cookie);
  AuthFilter filter = new AuthFilter();
  filter.doFilter(request, response, chain);
 }
}
