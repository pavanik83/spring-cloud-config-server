package com.wdpr.ee.authz;

import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import com.wdpr.ee.authz.model.AuthDO;
import com.wdpr.ee.authz.model.TokenDO;
import com.wdpr.ee.authz.util.AuthConstants;
import com.wdpr.ee.authz.util.JSONConfigLoader;
import com.wdpr.ee.authz.util.ScopeKeyClass;

/**
 * @author sanwa001
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ AuthFilter.class })
@PowerMockIgnore({ "javax.management.*" })
public class AuthFilterValidateScopeTest
{

    Map<String, String> tokenList = new HashMap<>();
    Map<ScopeKeyClass, AuthDO> scopeMap = JSONConfigLoader.getInstance().loadScopeData();
    Pattern[] scopePatterns = new Pattern[scopeMap.size()];
    private static final Logger LOG = LogManager.getLogger(AuthFilter.class);
    Map<String, String> grantTokens = new HashMap<>();

    @Before
    public void setUp() throws Exception
    {
        this.tokenList.put("access_token", "uz3588_tywiTt0a9l9MROA");
        this.tokenList.put("client_id", "WDPRO-NGE.PEPCOM-STAGE");
        this.tokenList.put("username", "api");
        this.tokenList.put("assertion_type", "public");
        this.tokenList.put("grant_type", "assertion");
        
        this.grantTokens.put("grant_type", "client_credentials");
        this.grantTokens.put("client_id", "WDPRO-NGE.PEPCOM-STAGE");
        this.grantTokens.put("client_secret", "E2050034-0C95-11E1-872D-1BB84724019B");
        this.grantTokens.put("scope", "RETURN_ALL_CLIENT_SCOPES");
        
    }

    @After
    public void tearDown() throws Exception
    {
    }
    
    @Test
    public void testvalidateScopeNewService() throws Exception
    {

        AuthFilter authFilter = PowerMockito.mock(AuthFilter.class);
        AuthFilter spy = spy(authFilter);
        // AuthFilter authFilter = new AuthFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/new-service");
        request.setMethod("GET");
        AuthDO scopeItem = null;
        scopeItem = retrieveScopeItemFromURIandMethod(scopeItem, request);
        String json =
                "{\"scope\":\"disneyid-compliance-b2b-read-information disneyid-compliance-b2b-read-permissions disneyid-compliance-b2b-update-permissions disneyid-compliance-guest-read-information disneyid-compliance-guest-read-permissions disneyid-compliance-guest-update-permissions disneyid-profile-b2b-create disneyid-profile-guest-disable disneyid-profile-guest-read disneyid-profile-guest-update dtss-grx-public edgesvcs:4fb268cbe4b048c4a74ad9b4 edgesvcs:4fb3d8c9e4b0300ee8a2ed23 edgesvcs:4fc51383e4b081539a5ab350 edgesvcs:4fd368d7e4b0939536d134c7 edgesvcs:4fd380bee4b033db40328c07 edgesvcs:4fd38e61e4b033db40328c08 edgesvcs:4fd38eace4b0939536d134c8 edgesvcs:4fd607bce4b0939536d134ca edgesvcs:4fd61aa4e4b0939536d134cb edgesvcs:4fda9d9ee4b091063045de90 edgesvcs:4fdb598fe4b091063045dea6 edgesvcs:4fdb59a2e4b091063045dea7 edgesvcs:4fdb59dfe4b091063045dea8 edgesvcs:4fdb5a5fe4b091063045dea9 edgesvcs:4fdb5a94e4b091063045deaa edgesvcs:4fdb5aaee4b038e3e5e38a31 edgesvcs:4fdb5acbe4b038e3e5e38a32 edgesvcs:4fe4c646e4b079849be7b84d edgesvcs:4fef1341e4b0da234f941731 edgesvcs:4fef13c4e4b0cd8349110f21 edgesvcs:4fef1569e4b0b6a92c2d0290 edgesvcs:4fef1580e4b0cd8349110f22 edgesvcs:4fef15a7e4b0360bc71dd7c4 edgesvcs:4fef1715e4b0258b346d4bc3 edgesvcs:4fef18bae4b00c3a09445257 edgesvcs:4fef1c10e4b0360bc71dd7c5 edgesvcs:4fef297be4b080283d5c033e edgesvcs:4fef29a5e4b0258b346d4bc4 edgesvcs:4fef29b1e4b0360bc71dd7c6 edgesvcs:4fef2c78e4b0258b346d4bc6 edgesvcs:4fef5b1be4b0360bc71dd7c7 edgesvcs:4ff60eaee4b0da234f941746 edgesvcs:4ff60edae4b080283d5c034f edgesvcs:4ff60ef4e4b0cd8349110f2f edgesvcs:4ff60f08e4b0b6a92c2d02a3 edgesvcs:4ff60f22e4b0b6a92c2d02a4 edgesvcs:4ff60f41e4b0e80b01ea52d1 edgesvcs:4ff60f81e4b0cd8349110f30 edgesvcs:4ff60fb3e4b0b6a92c2d02a5 edgesvcs:4ff60ff5e4b0258b346d4bdd edgesvcs:4ff6102be4b0da234f941748 edgesvcs:4ff61396e4b0b6a92c2d02a7 edgesvcs:4ff6dd6de4b0cd8349110f31 edgesvcs:4ffc6820e4b00c3a0944526c edgesvcs:5009a70ce4b0d0406d1a3caa edgesvcs:5009a71fe4b0043715577bd6 edgesvcs:5009a733e4b00a2cae5003db edgesvcs:500ef978e4b051caead958a1 edgesvcs:500f002fe4b0d337bfe27f22 edgesvcs:500f00d1e4b051caead958ab edgesvcs:500f00e6e4b0d337bfe27f23 edgesvcs:500f0adfe4b051caead958ad edgesvcs:500f0bc2e4b0e7d8b59801f8 edgesvcs:500f13e4e4b051caead958b0 edgesvcs:500f1405e4b0d337bfe27f28 edgesvcs:50115bd4e4b051caead958bb edgesvcs:50115c20e4b051caead958bd edgesvcs:50115c39e4b051caead958be edgesvcs:50115c43e4b0200501d878e2 edgesvcs:50115c5ee4b051caead958bf edgesvcs:50115c74e4b051caead958c0 edgesvcs:50115cd1e4b0d337bfe27f33 edgesvcs:50115cf7e4b0200501d878e4 edgesvcs:50115d12e4b0d337bfe27f34 edgesvcs:50115de8e4b0d337bfe27f35 edgesvcs:50115dfae4b0200501d878e5 edgesvcs:50194737e4b0e7d8b5980258 wdpro-api-public-crud wdpro-authentication-general-crud wdpro-availability-general-crud wdpro-booking-general-crud wdpro-bulk-general-crud wdpro-cart-general-crud wdpro-cruise-booking-cart-keepalive-crud wdpro-facility-general-r wdpro-finder-general-r wdpro-health-monitor-general-r wdpro-lists-general-crud wdpro-lodging-general-crud wdpro-media-general-r wdpro-payment-general-crud wdpro-personalmagic-general-crud wdpro-pricing-general-r wdpro-product-general-r wdpro-profile-dob-u wdpro-profile-public-cr wdpro-profile-vertical-crud wdpro-recommendation-general-crud wdpro-recommendation-public-r wdpro-reservation-agent-crud wdpro-reservation-general-crud wdpro-reservation-itinerary-r wdpro-review-general-r\"}";
        when(spy, "validateScope", this.tokenList, "GET", scopeItem, json).thenReturn(true);

    }
    @Test
    public void testvalidateScopeMethodMatched() throws Exception
    {

        AuthFilter authFilter = PowerMockito.mock(AuthFilter.class);
        AuthFilter spy = spy(authFilter);
        // AuthFilter authFilter = new AuthFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/customer-service");
        request.setMethod("GET");
        AuthDO scopeItem = null;
        scopeItem = retrieveScopeItemFromURIandMethod(scopeItem, request);
        String json =
                "{\"scope\":\"disneyid-compliance-b2b-read-information disneyid-compliance-b2b-read-permissions disneyid-compliance-b2b-update-permissions disneyid-compliance-guest-read-information disneyid-compliance-guest-read-permissions disneyid-compliance-guest-update-permissions disneyid-profile-b2b-create disneyid-profile-guest-disable disneyid-profile-guest-read disneyid-profile-guest-update dtss-grx-public edgesvcs:4fb268cbe4b048c4a74ad9b4 edgesvcs:4fb3d8c9e4b0300ee8a2ed23 edgesvcs:4fc51383e4b081539a5ab350 edgesvcs:4fd368d7e4b0939536d134c7 edgesvcs:4fd380bee4b033db40328c07 edgesvcs:4fd38e61e4b033db40328c08 edgesvcs:4fd38eace4b0939536d134c8 edgesvcs:4fd607bce4b0939536d134ca edgesvcs:4fd61aa4e4b0939536d134cb edgesvcs:4fda9d9ee4b091063045de90 edgesvcs:4fdb598fe4b091063045dea6 edgesvcs:4fdb59a2e4b091063045dea7 edgesvcs:4fdb59dfe4b091063045dea8 edgesvcs:4fdb5a5fe4b091063045dea9 edgesvcs:4fdb5a94e4b091063045deaa edgesvcs:4fdb5aaee4b038e3e5e38a31 edgesvcs:4fdb5acbe4b038e3e5e38a32 edgesvcs:4fe4c646e4b079849be7b84d edgesvcs:4fef1341e4b0da234f941731 edgesvcs:4fef13c4e4b0cd8349110f21 edgesvcs:4fef1569e4b0b6a92c2d0290 edgesvcs:4fef1580e4b0cd8349110f22 edgesvcs:4fef15a7e4b0360bc71dd7c4 edgesvcs:4fef1715e4b0258b346d4bc3 edgesvcs:4fef18bae4b00c3a09445257 edgesvcs:4fef1c10e4b0360bc71dd7c5 edgesvcs:4fef297be4b080283d5c033e edgesvcs:4fef29a5e4b0258b346d4bc4 edgesvcs:4fef29b1e4b0360bc71dd7c6 edgesvcs:4fef2c78e4b0258b346d4bc6 edgesvcs:4fef5b1be4b0360bc71dd7c7 edgesvcs:4ff60eaee4b0da234f941746 edgesvcs:4ff60edae4b080283d5c034f edgesvcs:4ff60ef4e4b0cd8349110f2f edgesvcs:4ff60f08e4b0b6a92c2d02a3 edgesvcs:4ff60f22e4b0b6a92c2d02a4 edgesvcs:4ff60f41e4b0e80b01ea52d1 edgesvcs:4ff60f81e4b0cd8349110f30 edgesvcs:4ff60fb3e4b0b6a92c2d02a5 edgesvcs:4ff60ff5e4b0258b346d4bdd edgesvcs:4ff6102be4b0da234f941748 edgesvcs:4ff61396e4b0b6a92c2d02a7 edgesvcs:4ff6dd6de4b0cd8349110f31 edgesvcs:4ffc6820e4b00c3a0944526c edgesvcs:5009a70ce4b0d0406d1a3caa edgesvcs:5009a71fe4b0043715577bd6 edgesvcs:5009a733e4b00a2cae5003db edgesvcs:500ef978e4b051caead958a1 edgesvcs:500f002fe4b0d337bfe27f22 edgesvcs:500f00d1e4b051caead958ab edgesvcs:500f00e6e4b0d337bfe27f23 edgesvcs:500f0adfe4b051caead958ad edgesvcs:500f0bc2e4b0e7d8b59801f8 edgesvcs:500f13e4e4b051caead958b0 edgesvcs:500f1405e4b0d337bfe27f28 edgesvcs:50115bd4e4b051caead958bb edgesvcs:50115c20e4b051caead958bd edgesvcs:50115c39e4b051caead958be edgesvcs:50115c43e4b0200501d878e2 edgesvcs:50115c5ee4b051caead958bf edgesvcs:50115c74e4b051caead958c0 edgesvcs:50115cd1e4b0d337bfe27f33 edgesvcs:50115cf7e4b0200501d878e4 edgesvcs:50115d12e4b0d337bfe27f34 edgesvcs:50115de8e4b0d337bfe27f35 edgesvcs:50115dfae4b0200501d878e5 edgesvcs:50194737e4b0e7d8b5980258 wdpro-api-public-crud wdpro-authentication-general-crud wdpro-availability-general-crud wdpro-booking-general-crud wdpro-bulk-general-crud wdpro-cart-general-crud wdpro-cruise-booking-cart-keepalive-crud wdpro-facility-general-r wdpro-finder-general-r wdpro-health-monitor-general-r wdpro-lists-general-crud wdpro-lodging-general-crud wdpro-media-general-r wdpro-payment-general-crud wdpro-personalmagic-general-crud wdpro-pricing-general-r wdpro-product-general-r wdpro-profile-dob-u wdpro-profile-public-cr wdpro-profile-vertical-crud wdpro-recommendation-general-crud wdpro-recommendation-public-r wdpro-reservation-agent-crud wdpro-reservation-general-crud wdpro-reservation-itinerary-r wdpro-review-general-r\"}";
        when(spy, "validateScope", this.tokenList, "GET", scopeItem, json).thenReturn(true);

    }

    @Test
    public void testvalidateScopeMethodNotMatched() throws Exception
    {
        AuthFilter authFilter = PowerMockito.mock(AuthFilter.class);
        AuthFilter spy = spy(authFilter);
        // AuthFilter authFilter = new AuthFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test-service");
        request.setMethod("POST");
        AuthDO scopeItem = null;
        scopeItem = retrieveScopeItemFromURIandMethod(scopeItem, request);
        String json =
                "{\"scope\":\"disneyid-compliance-b2b-read-information disneyid-compliance-b2b-read-permissions disneyid-compliance-b2b-update-permissions disneyid-compliance-guest-read-information disneyid-compliance-guest-read-permissions disneyid-compliance-guest-update-permissions disneyid-profile-b2b-create disneyid-profile-guest-disable disneyid-profile-guest-read disneyid-profile-guest-update dtss-grx-public edgesvcs:4fb268cbe4b048c4a74ad9b4 edgesvcs:4fb3d8c9e4b0300ee8a2ed23 edgesvcs:4fc51383e4b081539a5ab350 edgesvcs:4fd368d7e4b0939536d134c7 edgesvcs:4fd380bee4b033db40328c07 edgesvcs:4fd38e61e4b033db40328c08 edgesvcs:4fd38eace4b0939536d134c8 edgesvcs:4fd607bce4b0939536d134ca edgesvcs:4fd61aa4e4b0939536d134cb edgesvcs:4fda9d9ee4b091063045de90 edgesvcs:4fdb598fe4b091063045dea6 edgesvcs:4fdb59a2e4b091063045dea7 edgesvcs:4fdb59dfe4b091063045dea8 edgesvcs:4fdb5a5fe4b091063045dea9 edgesvcs:4fdb5a94e4b091063045deaa edgesvcs:4fdb5aaee4b038e3e5e38a31 edgesvcs:4fdb5acbe4b038e3e5e38a32 edgesvcs:4fe4c646e4b079849be7b84d edgesvcs:4fef1341e4b0da234f941731 edgesvcs:4fef13c4e4b0cd8349110f21 edgesvcs:4fef1569e4b0b6a92c2d0290 edgesvcs:4fef1580e4b0cd8349110f22 edgesvcs:4fef15a7e4b0360bc71dd7c4 edgesvcs:4fef1715e4b0258b346d4bc3 edgesvcs:4fef18bae4b00c3a09445257 edgesvcs:4fef1c10e4b0360bc71dd7c5 edgesvcs:4fef297be4b080283d5c033e edgesvcs:4fef29a5e4b0258b346d4bc4 edgesvcs:4fef29b1e4b0360bc71dd7c6 edgesvcs:4fef2c78e4b0258b346d4bc6 edgesvcs:4fef5b1be4b0360bc71dd7c7 edgesvcs:4ff60eaee4b0da234f941746 edgesvcs:4ff60edae4b080283d5c034f edgesvcs:4ff60ef4e4b0cd8349110f2f edgesvcs:4ff60f08e4b0b6a92c2d02a3 edgesvcs:4ff60f22e4b0b6a92c2d02a4 edgesvcs:4ff60f41e4b0e80b01ea52d1 edgesvcs:4ff60f81e4b0cd8349110f30 edgesvcs:4ff60fb3e4b0b6a92c2d02a5 edgesvcs:4ff60ff5e4b0258b346d4bdd edgesvcs:4ff6102be4b0da234f941748 edgesvcs:4ff61396e4b0b6a92c2d02a7 edgesvcs:4ff6dd6de4b0cd8349110f31 edgesvcs:4ffc6820e4b00c3a0944526c edgesvcs:5009a70ce4b0d0406d1a3caa edgesvcs:5009a71fe4b0043715577bd6 edgesvcs:5009a733e4b00a2cae5003db edgesvcs:500ef978e4b051caead958a1 edgesvcs:500f002fe4b0d337bfe27f22 edgesvcs:500f00d1e4b051caead958ab edgesvcs:500f00e6e4b0d337bfe27f23 edgesvcs:500f0adfe4b051caead958ad edgesvcs:500f0bc2e4b0e7d8b59801f8 edgesvcs:500f13e4e4b051caead958b0 edgesvcs:500f1405e4b0d337bfe27f28 edgesvcs:50115bd4e4b051caead958bb edgesvcs:50115c20e4b051caead958bd edgesvcs:50115c39e4b051caead958be edgesvcs:50115c43e4b0200501d878e2 edgesvcs:50115c5ee4b051caead958bf edgesvcs:50115c74e4b051caead958c0 edgesvcs:50115cd1e4b0d337bfe27f33 edgesvcs:50115cf7e4b0200501d878e4 edgesvcs:50115d12e4b0d337bfe27f34 edgesvcs:50115de8e4b0d337bfe27f35 edgesvcs:50115dfae4b0200501d878e5 edgesvcs:50194737e4b0e7d8b5980258 wdpro-api-public-crud wdpro-authentication-general-crud wdpro-availability-general-crud wdpro-booking-general-crud wdpro-bulk-general-crud wdpro-cart-general-crud wdpro-cruise-booking-cart-keepalive-crud wdpro-facility-general-r wdpro-finder-general-r wdpro-health-monitor-general-r wdpro-lists-general-crud wdpro-lodging-general-crud wdpro-media-general-r wdpro-payment-general-crud wdpro-personalmagic-general-crud wdpro-pricing-general-r wdpro-product-general-r wdpro-profile-dob-u wdpro-profile-public-cr wdpro-profile-vertical-crud wdpro-recommendation-general-crud wdpro-recommendation-public-r wdpro-reservation-agent-crud wdpro-reservation-general-crud wdpro-reservation-itinerary-r wdpro-review-general-r\"}";
        when(spy, "validateScope", this.tokenList, "GET", scopeItem, json).thenReturn(true);

    }

    @Test
    public void testvalidateStarScope() throws Exception
    {
        AuthFilter authFilter = PowerMockito.mock(AuthFilter.class);
        AuthFilter spy = spy(authFilter);
        // AuthFilter authFilter = new AuthFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/star-service");
        request.setMethod("GET");
        AuthDO scopeItem = null;
        scopeItem = retrieveScopeItemFromURIandMethod(scopeItem, request);
        String json =
                "{\"scope\":\"disneyid-compliance-b2b-read-information disneyid-compliance-b2b-read-permissions disneyid-compliance-b2b-update-permissions disneyid-compliance-guest-read-information disneyid-compliance-guest-read-permissions disneyid-compliance-guest-update-permissions disneyid-profile-b2b-create disneyid-profile-guest-disable disneyid-profile-guest-read disneyid-profile-guest-update dtss-grx-public edgesvcs:4fb268cbe4b048c4a74ad9b4 edgesvcs:4fb3d8c9e4b0300ee8a2ed23 edgesvcs:4fc51383e4b081539a5ab350 edgesvcs:4fd368d7e4b0939536d134c7 edgesvcs:4fd380bee4b033db40328c07 edgesvcs:4fd38e61e4b033db40328c08 edgesvcs:4fd38eace4b0939536d134c8 edgesvcs:4fd607bce4b0939536d134ca edgesvcs:4fd61aa4e4b0939536d134cb edgesvcs:4fda9d9ee4b091063045de90 edgesvcs:4fdb598fe4b091063045dea6 edgesvcs:4fdb59a2e4b091063045dea7 edgesvcs:4fdb59dfe4b091063045dea8 edgesvcs:4fdb5a5fe4b091063045dea9 edgesvcs:4fdb5a94e4b091063045deaa edgesvcs:4fdb5aaee4b038e3e5e38a31 edgesvcs:4fdb5acbe4b038e3e5e38a32 edgesvcs:4fe4c646e4b079849be7b84d edgesvcs:4fef1341e4b0da234f941731 edgesvcs:4fef13c4e4b0cd8349110f21 edgesvcs:4fef1569e4b0b6a92c2d0290 edgesvcs:4fef1580e4b0cd8349110f22 edgesvcs:4fef15a7e4b0360bc71dd7c4 edgesvcs:4fef1715e4b0258b346d4bc3 edgesvcs:4fef18bae4b00c3a09445257 edgesvcs:4fef1c10e4b0360bc71dd7c5 edgesvcs:4fef297be4b080283d5c033e edgesvcs:4fef29a5e4b0258b346d4bc4 edgesvcs:4fef29b1e4b0360bc71dd7c6 edgesvcs:4fef2c78e4b0258b346d4bc6 edgesvcs:4fef5b1be4b0360bc71dd7c7 edgesvcs:4ff60eaee4b0da234f941746 edgesvcs:4ff60edae4b080283d5c034f edgesvcs:4ff60ef4e4b0cd8349110f2f edgesvcs:4ff60f08e4b0b6a92c2d02a3 edgesvcs:4ff60f22e4b0b6a92c2d02a4 edgesvcs:4ff60f41e4b0e80b01ea52d1 edgesvcs:4ff60f81e4b0cd8349110f30 edgesvcs:4ff60fb3e4b0b6a92c2d02a5 edgesvcs:4ff60ff5e4b0258b346d4bdd edgesvcs:4ff6102be4b0da234f941748 edgesvcs:4ff61396e4b0b6a92c2d02a7 edgesvcs:4ff6dd6de4b0cd8349110f31 edgesvcs:4ffc6820e4b00c3a0944526c edgesvcs:5009a70ce4b0d0406d1a3caa edgesvcs:5009a71fe4b0043715577bd6 edgesvcs:5009a733e4b00a2cae5003db edgesvcs:500ef978e4b051caead958a1 edgesvcs:500f002fe4b0d337bfe27f22 edgesvcs:500f00d1e4b051caead958ab edgesvcs:500f00e6e4b0d337bfe27f23 edgesvcs:500f0adfe4b051caead958ad edgesvcs:500f0bc2e4b0e7d8b59801f8 edgesvcs:500f13e4e4b051caead958b0 edgesvcs:500f1405e4b0d337bfe27f28 edgesvcs:50115bd4e4b051caead958bb edgesvcs:50115c20e4b051caead958bd edgesvcs:50115c39e4b051caead958be edgesvcs:50115c43e4b0200501d878e2 edgesvcs:50115c5ee4b051caead958bf edgesvcs:50115c74e4b051caead958c0 edgesvcs:50115cd1e4b0d337bfe27f33 edgesvcs:50115cf7e4b0200501d878e4 edgesvcs:50115d12e4b0d337bfe27f34 edgesvcs:50115de8e4b0d337bfe27f35 edgesvcs:50115dfae4b0200501d878e5 edgesvcs:50194737e4b0e7d8b5980258 wdpro-api-public-crud wdpro-authentication-general-crud wdpro-availability-general-crud wdpro-booking-general-crud wdpro-bulk-general-crud wdpro-cart-general-crud wdpro-cruise-booking-cart-keepalive-crud wdpro-facility-general-r wdpro-finder-general-r wdpro-health-monitor-general-r wdpro-lists-general-crud wdpro-lodging-general-crud wdpro-media-general-r wdpro-payment-general-crud wdpro-personalmagic-general-crud wdpro-pricing-general-r wdpro-product-general-r wdpro-profile-dob-u wdpro-profile-public-cr wdpro-profile-vertical-crud wdpro-recommendation-general-crud wdpro-recommendation-public-r wdpro-reservation-agent-crud wdpro-reservation-general-crud wdpro-reservation-itinerary-r wdpro-review-general-r\"}";
        when(spy, "validateScope", this.tokenList, "GET", scopeItem, json).thenReturn(true);

    }

    @Test
    public void testvalidateScopeKeystone() throws Exception
    {

        AuthFilter authFilter = PowerMockito.mock(AuthFilter.class);
        AuthFilter spy = spy(authFilter);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/keystone-service");
        request.setMethod("GET");
        AuthDO scopeItem = null;
        scopeItem = retrieveScopeItemFromURIandMethod(scopeItem, request);
        String json =
                "{\"scope\":\"wdprt-dme-cast DRC+Agent%3ADMESearchResultsFilterButtons DRC+Agent%3ADMEManageInboundFlightAirline DRC+Agent%3ADMECreateAddInboundTransferButton DRC+Agent%3ADMEManageGuestNameonReservation DRC+Agent%3ADMEManageOutboundFlightAirline DRC+Agent%3ADMEManageInboundInternationalFlightIndicator DRC+Agent%3ADMECancelManageDMETransferButton DRC+Agent%3ADMECreateNoOutboundFlightInfo DRC+Agent%3ADMEAdvSearchIncludeCancelsCheckBox DRC+Agent%3ADMECreateOutboundTransferTypeFlight%2FOther DRC+Agent%3ADMEAdvSearchByRoom%23 DRC+Agent%3ADMEManageAddInboundTransferButton DRC+Agent%3ADMESearchByGuestName DRC+Agent%3ADMEUpdateGuestChildIndicator DRC+Agent%3ADMEManageOutboundInternationalFlightIndicator DRC+Agent%3ADMECreateAddOutboundTransferButton DRC+Agent%3ADMEUpdateGuestAge DRC+Agent%3ADMEManageInboundFlightNumber DRC+Agent%3ADMECommonUIOptOutTransferForSingleGuestsOnReservation DRC+Agent%3ADMECreateInboundDrop-offLocation DRC+Agent%3ADMECommonUIAddSingleGuestOnReservationToTransfer DRC+Agent%3ADMEManageOutboundTransferTypeFlight%2FOther DRC+Agent%3ADMEClearSearchFilter DRC+Agent%3ADMEAdvancedSearchNoRestrictions DRC+Agent%3ADMEClearSearchButton DRC+Agent%3ADMECommonUIOptOutTransferForAllGuestsOnReservation DRC+Agent%3ADMEAdjustMaxSearchResultsReturned DRC+Agent%3ADMEAdvSearchByResortDropdown DRC+Agent%3ADMESearchToolTipforConfirmationID DRC+Agent%3ADMECollateralAddressCancel DRC+Agent%3ADMRemoveFilterCriteriaBreadcrumb DRC+Agent%3ADMECreateInboundFlightNumber DRC+Agent%3ADMEAddCollateralAddressSave DRC+Agent%3ADMEManageValidFlightIncidaor DRC+Agent%3ADMERecapPanelSortDropdown DRC+Agent%3ADMECollateralSave DRC+Agent%3ADMECreateAdd%2FViewDMETransferHistoryComment DRC+Agent%3ADMEAdvSearchByAgencyName DRC+Agent%3ADMECreateOutoundTransferNoRestrictions DRC+Agent%3ADMECreateContinueCreateDMETransferButton DRC+Agent%3ADMEUpdateGuestAdultIndicator DRC+Agent%3ADMECommonUITravelPlanStatusDropdown DRC+Agent%3ADMECreateTransfersNoRestrictions DRC+Agent%3ADMECancelSearch DRC+Agent%3ADMEUpdateGuestReservationStatus DRC+Agent%3ADMECommonUIAddAllGuestsOnReservationToTransfer DRC+Agent%3ADMEAdvSearchbyGroupName DRC+Agent%3ADMESaveManageGuest DRC+Agent%3ADMESearchByDateRange DRC+Agent%3ADMECommonUIAddGuestToTransfer DRC+Agent%3ADMECommonUICancelUpdateReservation DRC+Agent%3ADMENewGuestSpecialRequestDropdownManual DRC+Agent%3ADMECommonUIRemoveGuestFromTransfer DRC+Agent%3ADMECreateInboundFlightAirline DRC+Agent%3ADMECreateNoArrivingFlightInfo DRC+Agent%3ADMECreateInboundInternationalFlightIndicator DRC+Agent%3ADMEApplyManageDMETransferButton DRC+Agent%3ADMECreateOutboundTransferOptOut DRC+Agent%3ADMEExpandReservationSearchResult DRC+Agent%3ADMEViewHistoryNoRestrictions DRC+Agent%3ADMEHistoryMenu DRC+Agent%3ADMECommonUIExpand%2FCollapseAll DRC+Agent%3ADMESpecialRequestCancelButton DRC+Agent%3ADMECreateOutboundPick-upLocation DRC+Agent%3ADMECreateOutboundFlightDate DRC+Agent%3ADMECreateOutboundInternationalFlightIndicator DRC+Agent%3ADMECommonUIRecapPanelPartyTab DRC+Agent%3ADMECreateInboundTransferTypeFlight%2FOther DRC+Agent%3ADMECommonUIRecapPanelCollateralTab DRC+Agent%3ADMECollateralCancel DRC+Agent%3ADMEManageNumberOfAssignedGuests DRC+Agent%3ADMESearchbyGroupName DRC+Agent%3ADMEBasicSearchNoRestrictions DRC+Agent%3ADMEUpdateGuestSpecialRequestDropdown DRC+Agent%3ADMEGuestSpecialRequestDropdownLaunch DRC+Agent%3ADMECommonUISpecialRequest DRC+Agent%3ADMEAccessFromAllExternalSystems DRC+Agent%3ADMECreateInboundTransferOptOut DRC+Agent%3ADMEMnageInboundTransferTypeFlight%2FOther DRC+Agent%3ADMESearchByConfirmationID DRC+Agent%3ADMESearchbyGroupCode DRC+Agent%3ADMEReservationMenu DRC+Agent%3ADMEIncludeCancelsFilterCheckBox DRC+Agent%3ADMEAdvancedSearchHyperlink DRC+Agent%3ADMEOpenReservationFromSearch DRC+Agent%3ADMEEditGuestOnReservationButton DRC+Agent%3ADMEAdvSearchbyTeamName DRC+Agent%3ADMESearchButton DRC+Agent%3ADMEManageOutboundFlightDate DRC+Agent%3ADMEAllSearchNoRestrictions DRC+Agent%3ADMEManageRemoveTransferFromGuestReservation DRC+Agent%3ADMECommonUIContinueUpdateReservation DRC+Agent%3ADMECloseHistory DRC+Agent%3ADMEAddHistoryComment DRC+Agent%3ADMECommonUIRemoveTansferAllGuestsAllTravelPlans DRC+Agent%3ADMEAdvSearchByGroupCode DRC+Agent%3ADMEManageOutboundFlightNumber DRC+Agent%3ADMESearchByAllDateRanges DRC+Agent%3ADMEManageAddOutboundTransferButton DRC+Agent%3ADMEFilterSearch DRC+Agent%3ADMECreateCancelCreateDMETransferButton DRC+Agent%3ADMECreateInboundFlightDate DRC+Agent%3ADMEBackToExternalSystem DRC+Agent%3ADMEManageInboundDMETransferButton DRC+Agent%3ADMEAddCollateralAddress DRC+Agent%3ADMECommonUIEditGuestOnReservationButton DRC+Agent%3ADMESearchResultsNoRestrictions DRC+Agent%3ADMECancelManageGuest DRC+Agent%3ADMECommonUIRecapTab DRC+Agent%3ADMECreateInboundTransfersNoRestrictions DRC+Agent%3ADMEManageScheduledFlightDepartureTime DRC+Agent%3ADMESpecialRequestDoneButton DRC+Agent%3ADMEBasicSearchHyperlink DRC+Agent%3ADMECommonUIRemoveAllGuestsOnResFromTransfer DRC+Agent%3ADMECreateValidFlightIndicator DRC+Agent%3ADMEAdvSearchByAgencyIATA%23OrCLIA%23 DRC+Agent%3ADMECreateOutboundFlightNumber DRC+Agent%3ADMEViewTransferTableInfo DRC+Agent%3ADMEManageScheduledFlightArrivalTime DRC+Agent%3ADMECreateOutboundFlightAirline DRC+Agent%3ADMECommonUIRemoveSingleGuestOnReservationFromTransfer DRC+Agent%3ADMEManageInboundFlightDate Add+History+Comment%3ADMECancelManageDMETransferButton Add+History+Comment%3ADMECollateralSave Add+History+Comment%3ADMECreateAdd%2FViewDMETransferHistoryComment Add+History+Comment%3ADMECreateContinueCreateDMETransferButton Add+History+Comment%3ADMEApplyManageDMETransferButton Add+History+Comment%3ADMEHistoryMenu Add+History+Comment%3ADMECollateralCancel Add+History+Comment%3ADMEManageTransferHistoryComment Add+History+Comment%3ADMECloseHistory Add+History+Comment%3ADMEAddHistoryComment Add+History+Comment%3ADMEAddCollateralHistoryComment Add+History+Comment%3ADMECreateCancelCreateDMETransferButton Add+History+Comment%3ADMEViewTransferTableInfo\"}";
        when(spy, "validateScope", this.tokenList, "GET", scopeItem, json).thenReturn(true);

    }
    
    @Test
    public void testvalidateScopeWrongMethodName() throws Exception
    {

        AuthFilter authFilter = PowerMockito.mock(AuthFilter.class);
        AuthFilter spy = spy(authFilter);
        // AuthFilter authFilter = new AuthFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/MethodCheck/abc/xyz/hhh");
        request.setMethod("TEST");
        AuthDO scopeItem = null;
        scopeItem = retrieveScopeItemFromURIandMethod(scopeItem, request);
        String json =
                "{\"scope\":\"disneyid-compliance-b2b-read-information disneyid-compliance-b2b-read-permissions disneyid-compliance-b2b-update-permissions disneyid-compliance-guest-read-information disneyid-compliance-guest-read-permissions disneyid-compliance-guest-update-permissions disneyid-profile-b2b-create disneyid-profile-guest-disable disneyid-profile-guest-read disneyid-profile-guest-update dtss-grx-public edgesvcs:4fb268cbe4b048c4a74ad9b4 edgesvcs:4fb3d8c9e4b0300ee8a2ed23 edgesvcs:4fc51383e4b081539a5ab350 edgesvcs:4fd368d7e4b0939536d134c7 edgesvcs:4fd380bee4b033db40328c07 edgesvcs:4fd38e61e4b033db40328c08 edgesvcs:4fd38eace4b0939536d134c8 edgesvcs:4fd607bce4b0939536d134ca edgesvcs:4fd61aa4e4b0939536d134cb edgesvcs:4fda9d9ee4b091063045de90 edgesvcs:4fdb598fe4b091063045dea6 edgesvcs:4fdb59a2e4b091063045dea7 edgesvcs:4fdb59dfe4b091063045dea8 edgesvcs:4fdb5a5fe4b091063045dea9 edgesvcs:4fdb5a94e4b091063045deaa edgesvcs:4fdb5aaee4b038e3e5e38a31 edgesvcs:4fdb5acbe4b038e3e5e38a32 edgesvcs:4fe4c646e4b079849be7b84d edgesvcs:4fef1341e4b0da234f941731 edgesvcs:4fef13c4e4b0cd8349110f21 edgesvcs:4fef1569e4b0b6a92c2d0290 edgesvcs:4fef1580e4b0cd8349110f22 edgesvcs:4fef15a7e4b0360bc71dd7c4 edgesvcs:4fef1715e4b0258b346d4bc3 edgesvcs:4fef18bae4b00c3a09445257 edgesvcs:4fef1c10e4b0360bc71dd7c5 edgesvcs:4fef297be4b080283d5c033e edgesvcs:4fef29a5e4b0258b346d4bc4 edgesvcs:4fef29b1e4b0360bc71dd7c6 edgesvcs:4fef2c78e4b0258b346d4bc6 edgesvcs:4fef5b1be4b0360bc71dd7c7 edgesvcs:4ff60eaee4b0da234f941746 edgesvcs:4ff60edae4b080283d5c034f edgesvcs:4ff60ef4e4b0cd8349110f2f edgesvcs:4ff60f08e4b0b6a92c2d02a3 edgesvcs:4ff60f22e4b0b6a92c2d02a4 edgesvcs:4ff60f41e4b0e80b01ea52d1 edgesvcs:4ff60f81e4b0cd8349110f30 edgesvcs:4ff60fb3e4b0b6a92c2d02a5 edgesvcs:4ff60ff5e4b0258b346d4bdd edgesvcs:4ff6102be4b0da234f941748 edgesvcs:4ff61396e4b0b6a92c2d02a7 edgesvcs:4ff6dd6de4b0cd8349110f31 edgesvcs:4ffc6820e4b00c3a0944526c edgesvcs:5009a70ce4b0d0406d1a3caa edgesvcs:5009a71fe4b0043715577bd6 edgesvcs:5009a733e4b00a2cae5003db edgesvcs:500ef978e4b051caead958a1 edgesvcs:500f002fe4b0d337bfe27f22 edgesvcs:500f00d1e4b051caead958ab edgesvcs:500f00e6e4b0d337bfe27f23 edgesvcs:500f0adfe4b051caead958ad edgesvcs:500f0bc2e4b0e7d8b59801f8 edgesvcs:500f13e4e4b051caead958b0 edgesvcs:500f1405e4b0d337bfe27f28 edgesvcs:50115bd4e4b051caead958bb edgesvcs:50115c20e4b051caead958bd edgesvcs:50115c39e4b051caead958be edgesvcs:50115c43e4b0200501d878e2 edgesvcs:50115c5ee4b051caead958bf edgesvcs:50115c74e4b051caead958c0 edgesvcs:50115cd1e4b0d337bfe27f33 edgesvcs:50115cf7e4b0200501d878e4 edgesvcs:50115d12e4b0d337bfe27f34 edgesvcs:50115de8e4b0d337bfe27f35 edgesvcs:50115dfae4b0200501d878e5 edgesvcs:50194737e4b0e7d8b5980258 wdpro-api-public-crud wdpro-authentication-general-crud wdpro-availability-general-crud wdpro-booking-general-crud wdpro-bulk-general-crud wdpro-cart-general-crud wdpro-cruise-booking-cart-keepalive-crud wdpro-facility-general-r wdpro-finder-general-r wdpro-health-monitor-general-r wdpro-lists-general-crud wdpro-lodging-general-crud wdpro-media-general-r wdpro-payment-general-crud wdpro-personalmagic-general-crud wdpro-pricing-general-r wdpro-product-general-r wdpro-profile-dob-u wdpro-profile-public-cr wdpro-profile-vertical-crud wdpro-recommendation-general-crud wdpro-recommendation-public-r wdpro-reservation-agent-crud wdpro-reservation-general-crud wdpro-reservation-itinerary-r wdpro-review-general-r\"}";
        when(spy, "validateScope", this.tokenList, "GET", scopeItem, json).thenReturn(true);

    }
    private void loadScopePatterns()
    {

        StringBuilder msg = new StringBuilder();
        int count = 0;
        /*
         * for (String scope : this.scopeMap.keySet()) { scopePatterns[count] =
         * Pattern.compile(scope);
         * 
         * msg.append("#### Loaded required scope "); msg.append(scope);
         * LOG.debug(msg.toString()); count++; }
         */
        for (ScopeKeyClass scopeKeyClass : this.scopeMap.keySet())
        {
            scopePatterns[count] = Pattern.compile(scopeKeyClass.getUrl());

            msg.append("#### Loaded required scope ");
            msg.append(scopeKeyClass.getUrl());
            LOG.debug(msg.toString());
            count++;
        }
        return;
    }

    /**
     * 
     * @param reqCtx
     * @param method
     * @return
     */
    private AuthDO loadScopeItem(String reqCtx, String method)
    {
        loadScopePatterns();
        AuthDO scopeItem = null;
        StringBuilder msg = new StringBuilder();
        ScopeKeyClass scopeKeyClass = new ScopeKeyClass();
        scopeKeyClass.setUrl(reqCtx);
        scopeKeyClass.setMethod(method);
        for (int i = 0; i < scopePatterns.length; i++)
        {
            msg.delete(0, msg.length());
            String scopeKey = scopePatterns[i].pattern();
            if (scopePatterns[i].matcher(reqCtx).matches())
            {
                scopeItem = this.scopeMap.get(scopeKeyClass);
                msg.append("#### Matched the incoming request context ");
                msg.append(reqCtx);
                msg.append(" with the configured scope context ");
                msg.append(scopeKey);
                LOG.debug(msg.toString());
                return scopeItem;
            }
            else
            {
                msg.append("#### Unsuccessful match of configured scope context");
                msg.append(scopeKey);
                msg.append(" with the incoming request context ");
                msg.append(reqCtx);
                LOG.debug(msg.toString());
            }
        }
        return scopeItem;
    }
    /**
     * 
     * @param scopeItem
     * @param req
     * @return
     */
    private AuthDO retrieveScopeItemFromURIandMethod(AuthDO scopeItem, HttpServletRequest req)
    {
        loadScopePatterns();
        for (int i = 0; i < scopePatterns.length; i++)
        {
            
            if(scopePatterns[i].matcher(req.getRequestURI()).matches())
            {
                ScopeKeyClass scopeKeyClass = new ScopeKeyClass();
                scopeKeyClass.setUrl(scopePatterns[i].pattern());
                scopeKeyClass.setMethod("*");
                scopeItem = this.scopeMap.get(scopeKeyClass);
                if(scopeItem==null)
                {
                    scopeItem = loadScopeItem(scopePatterns[i].pattern(), req.getMethod());
                }
                break;
            }
        }
        return scopeItem;
    }
}
