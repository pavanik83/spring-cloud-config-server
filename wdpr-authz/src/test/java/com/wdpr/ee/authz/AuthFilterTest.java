/**
 *
 */
package com.wdpr.ee.authz;

import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;




public class AuthFilterTest {
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
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
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
        tokenList.put("grant_type", "assertion");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Tests the utility method for setting a correlation id by the client
     */



    /**
     * Creates an HTTP Filter and check to make sure the thread context
     * variables the filter is responsible for are set
     *
     * @throws IOException
     *             If the doFilter chokes
     * @throws ServletException
     *             If the doFilter chokes
     */
    @Test
    public void testDoFilter() throws IOException, ServletException {
        // Mock the HTTP Servlet objects used by the filter
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getHeader("TOKEN")).thenReturn("Un6LHVsOtTphTEANANz0UQ");

        AuthFilter filter = new AuthFilter();



        AuthFilter mockFilter = Mockito.mock(AuthFilter.class);
//        Mockito.when(mockFilter.loadHeaders(request)).thenReturn(tokenList);
        Mockito.doReturn(tokenList).when(mockFilter).loadHeaders(request);
        Mockito.when(request.getContextPath()).thenReturn("/CustomerOrder");
        filter.doFilter(request, response, chain);
        assertNotNull(response.getStatus());

    }

    @Test
    public void testDoFilterWithData() throws IOException, ServletException {
        // Mock the HTTP Servlet objects used by the filter
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getHeader("TOKEN")).thenReturn("Un6LHVsOtTphTEANANz0UQ");

        Enumeration<String> headerNames = Mockito.mock(Enumeration.class);
        AuthFilter filter = new AuthFilter();

//        Mockito.when(loadHeaders(request)).thenReturn(tokenList);

//        AuthFilter mockfilter = Mockito.mock(AuthFilter.class);


        AuthFilter spy = Mockito.spy(filter);
        Mockito.doReturn(tokenList).when(spy).loadHeaders(request);
//        Mockito.when(spy.loadHeaders(request)).thenReturn(tokenList);
//        Mockito.doReturn(tokenList).when(spy).loadHeaders(request);

//        Mockito.when(request.getHeaderNames()).thenReturn((Enumeration<String>)headerNames);
//        Mockito.when(headerNames.hasMoreElements()).thenReturn(false);
//        Mockito.when(filter.loadHeaders(request)).thenReturn(tokenList);
        //Mockito.doReturn(tokenList).when(filter.loadHeaders(request));



//        req.getHeaderNames()
//        Mockito.doThrow(new RuntimeException()).when(spy).loadHeaders(request);
//        Mockito.doReturn(tokenList).when(mockFilter.loadHeaders(request));
//        testLoadHeaders();
//        Mockito.doReturn(tokenList).when(mockFilter).loadHeaders(request);
//        Enumeration<String> headerNames = Mockito.mock(Enumeration.class);
//        request.getHeaderNames()
//        Map<String, String> tokenList = new HashMap<>();


//        Enumeration<String> headerNames = Mockito.mock(Enumeration<>.class);


//
//        Mockito.doReturn(tokenList).when(mockFilter.loadHeaders(request, tokenList));
//        tokenList.put("access_token", "uz3588_tywiTt0a9l9MROA");
//        tokenList.put("client_id", "WDPRO-NGE.PEPCOM-STAGE");
//        tokenList.put("username", "api");
//        tokenList.put("assertion_type", "public");
//        tokenList.put("grant_type", "assertion");
//        AuthFilter spy = Mockito.spy(filter);
//        Mockito.doThrow(new RuntimeException()).when(spy).loadHeaders(request, tokenList);

//        Mockito.when(tokenList.size()).thenReturn(tokenList.size());
//        Mockito.doReturn(1).when(spy).loadHeaders(request, tokenList);
//        Mockito.doNothing().when(loadHeaders(request, tokenList));
//        Mockito.doAnswer((Answer) tokenList).when(mockFilter).loadHeaders(request, tokenList);


        //Mockito.mock(mockFilter.loadHeaders(request, tokenList));

//        Mockito.when(request.getHeaderNames()).thenReturn(Enumeration<String>tokenList.keySet());

        //Map.Entry<String, String> entrySet = tokenList;


//        loadHeaders();
        Mockito.when(request.getContextPath()).thenReturn("/CustomerOrder");
        filter.doFilter(request, response, chain);
        assertNotNull(response.getStatus());

    }



    @Test
     public void testLoadHeaders() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        AuthFilter filter = new AuthFilter();
        filter.loadHeaders(request);
//        Map<String,String> map = Mockito.mock(Map.class);
        Map<String, String> tokenList =Mockito.mock(Map.class);
//        Mockito.when(map.entrySet()).thenReturn(tokenList.entrySet());
        tokenList.put("access_token", "uz3588_tywiTt0a9l9MROA");
        tokenList.put("client_id", "WDPRO-NGE.PEPCOM-STAGE");
        tokenList.put("username", "api");
        tokenList.put("assertion_type", "public");
        tokenList.put("grant_type", "assertion");
        assertNotNull(tokenList);

    }




}
