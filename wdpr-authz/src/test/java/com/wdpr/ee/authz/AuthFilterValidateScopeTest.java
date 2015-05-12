package com.wdpr.ee.authz;

import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import com.wdpr.ee.authz.model.AuthDO;
import com.wdpr.ee.authz.util.JSONConfigLoader;

/**
 * @author sanwa001
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AuthFilter.class})
@PowerMockIgnore({"javax.management.*"})

public class AuthFilterValidateScopeTest {

	Map<String, String> tokenList = new HashMap<>();
	Map<String, AuthDO> scopeMap = JSONConfigLoader.getInstance()
			.loadScopeData();
    Pattern[] scopePatterns = new Pattern[scopeMap.size()];
    private static final Logger LOG = LogManager.getLogger(AuthFilter.class);
	@Before
	public void setUp() throws Exception {
	    this.tokenList.put("access_token", "uz3588_tywiTt0a9l9MROA");
        this.tokenList.put("client_id", "WDPRO-NGE.PEPCOM-STAGE");
        this.tokenList.put("username", "api");
        this.tokenList.put("assertion_type", "public");
        this.tokenList.put("grant_type", "assertion");
    
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
    public void testvalidateScopeMethodMatched() throws Exception
    {
    	
    	AuthFilter authFilter	= PowerMockito.mock(AuthFilter.class);	
    	AuthFilter spy	=	spy(authFilter);
    	//AuthFilter authFilter	=	new AuthFilter();
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setContextPath("/customer-service");
    	AuthDO scopeItem = loadScopeItem(request.getContextPath());
    	String json="{\"scope\":\"disneyid-compliance-b2b-read-information disneyid-compliance-b2b-read-permissions disneyid-compliance-b2b-update-permissions disneyid-compliance-guest-read-information disneyid-compliance-guest-read-permissions disneyid-compliance-guest-update-permissions disneyid-profile-b2b-create disneyid-profile-guest-disable disneyid-profile-guest-read disneyid-profile-guest-update dtss-grx-public edgesvcs:4fb268cbe4b048c4a74ad9b4 edgesvcs:4fb3d8c9e4b0300ee8a2ed23 edgesvcs:4fc51383e4b081539a5ab350 edgesvcs:4fd368d7e4b0939536d134c7 edgesvcs:4fd380bee4b033db40328c07 edgesvcs:4fd38e61e4b033db40328c08 edgesvcs:4fd38eace4b0939536d134c8 edgesvcs:4fd607bce4b0939536d134ca edgesvcs:4fd61aa4e4b0939536d134cb edgesvcs:4fda9d9ee4b091063045de90 edgesvcs:4fdb598fe4b091063045dea6 edgesvcs:4fdb59a2e4b091063045dea7 edgesvcs:4fdb59dfe4b091063045dea8 edgesvcs:4fdb5a5fe4b091063045dea9 edgesvcs:4fdb5a94e4b091063045deaa edgesvcs:4fdb5aaee4b038e3e5e38a31 edgesvcs:4fdb5acbe4b038e3e5e38a32 edgesvcs:4fe4c646e4b079849be7b84d edgesvcs:4fef1341e4b0da234f941731 edgesvcs:4fef13c4e4b0cd8349110f21 edgesvcs:4fef1569e4b0b6a92c2d0290 edgesvcs:4fef1580e4b0cd8349110f22 edgesvcs:4fef15a7e4b0360bc71dd7c4 edgesvcs:4fef1715e4b0258b346d4bc3 edgesvcs:4fef18bae4b00c3a09445257 edgesvcs:4fef1c10e4b0360bc71dd7c5 edgesvcs:4fef297be4b080283d5c033e edgesvcs:4fef29a5e4b0258b346d4bc4 edgesvcs:4fef29b1e4b0360bc71dd7c6 edgesvcs:4fef2c78e4b0258b346d4bc6 edgesvcs:4fef5b1be4b0360bc71dd7c7 edgesvcs:4ff60eaee4b0da234f941746 edgesvcs:4ff60edae4b080283d5c034f edgesvcs:4ff60ef4e4b0cd8349110f2f edgesvcs:4ff60f08e4b0b6a92c2d02a3 edgesvcs:4ff60f22e4b0b6a92c2d02a4 edgesvcs:4ff60f41e4b0e80b01ea52d1 edgesvcs:4ff60f81e4b0cd8349110f30 edgesvcs:4ff60fb3e4b0b6a92c2d02a5 edgesvcs:4ff60ff5e4b0258b346d4bdd edgesvcs:4ff6102be4b0da234f941748 edgesvcs:4ff61396e4b0b6a92c2d02a7 edgesvcs:4ff6dd6de4b0cd8349110f31 edgesvcs:4ffc6820e4b00c3a0944526c edgesvcs:5009a70ce4b0d0406d1a3caa edgesvcs:5009a71fe4b0043715577bd6 edgesvcs:5009a733e4b00a2cae5003db edgesvcs:500ef978e4b051caead958a1 edgesvcs:500f002fe4b0d337bfe27f22 edgesvcs:500f00d1e4b051caead958ab edgesvcs:500f00e6e4b0d337bfe27f23 edgesvcs:500f0adfe4b051caead958ad edgesvcs:500f0bc2e4b0e7d8b59801f8 edgesvcs:500f13e4e4b051caead958b0 edgesvcs:500f1405e4b0d337bfe27f28 edgesvcs:50115bd4e4b051caead958bb edgesvcs:50115c20e4b051caead958bd edgesvcs:50115c39e4b051caead958be edgesvcs:50115c43e4b0200501d878e2 edgesvcs:50115c5ee4b051caead958bf edgesvcs:50115c74e4b051caead958c0 edgesvcs:50115cd1e4b0d337bfe27f33 edgesvcs:50115cf7e4b0200501d878e4 edgesvcs:50115d12e4b0d337bfe27f34 edgesvcs:50115de8e4b0d337bfe27f35 edgesvcs:50115dfae4b0200501d878e5 edgesvcs:50194737e4b0e7d8b5980258 wdpro-api-public-crud wdpro-authentication-general-crud wdpro-availability-general-crud wdpro-booking-general-crud wdpro-bulk-general-crud wdpro-cart-general-crud wdpro-cruise-booking-cart-keepalive-crud wdpro-facility-general-r wdpro-finder-general-r wdpro-health-monitor-general-r wdpro-lists-general-crud wdpro-lodging-general-crud wdpro-media-general-r wdpro-payment-general-crud wdpro-personalmagic-general-crud wdpro-pricing-general-r wdpro-product-general-r wdpro-profile-dob-u wdpro-profile-public-cr wdpro-profile-vertical-crud wdpro-recommendation-general-crud wdpro-recommendation-public-r wdpro-reservation-agent-crud wdpro-reservation-general-crud wdpro-reservation-itinerary-r wdpro-review-general-r\"}";
    	when(spy,"validateScope", this.tokenList, "GET", scopeItem,json).thenReturn(true);
    
    }
	@Test
    public void testvalidateScopeMethodNotMatched() throws Exception
    {
    	AuthFilter authFilter	= PowerMockito.mock(AuthFilter.class);	
    	AuthFilter spy	=	spy(authFilter);
    	//AuthFilter authFilter	=	new AuthFilter();
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setContextPath("/test-service");
    	AuthDO scopeItem = loadScopeItem(request.getContextPath());
    	String json="{\"scope\":\"disneyid-compliance-b2b-read-information disneyid-compliance-b2b-read-permissions disneyid-compliance-b2b-update-permissions disneyid-compliance-guest-read-information disneyid-compliance-guest-read-permissions disneyid-compliance-guest-update-permissions disneyid-profile-b2b-create disneyid-profile-guest-disable disneyid-profile-guest-read disneyid-profile-guest-update dtss-grx-public edgesvcs:4fb268cbe4b048c4a74ad9b4 edgesvcs:4fb3d8c9e4b0300ee8a2ed23 edgesvcs:4fc51383e4b081539a5ab350 edgesvcs:4fd368d7e4b0939536d134c7 edgesvcs:4fd380bee4b033db40328c07 edgesvcs:4fd38e61e4b033db40328c08 edgesvcs:4fd38eace4b0939536d134c8 edgesvcs:4fd607bce4b0939536d134ca edgesvcs:4fd61aa4e4b0939536d134cb edgesvcs:4fda9d9ee4b091063045de90 edgesvcs:4fdb598fe4b091063045dea6 edgesvcs:4fdb59a2e4b091063045dea7 edgesvcs:4fdb59dfe4b091063045dea8 edgesvcs:4fdb5a5fe4b091063045dea9 edgesvcs:4fdb5a94e4b091063045deaa edgesvcs:4fdb5aaee4b038e3e5e38a31 edgesvcs:4fdb5acbe4b038e3e5e38a32 edgesvcs:4fe4c646e4b079849be7b84d edgesvcs:4fef1341e4b0da234f941731 edgesvcs:4fef13c4e4b0cd8349110f21 edgesvcs:4fef1569e4b0b6a92c2d0290 edgesvcs:4fef1580e4b0cd8349110f22 edgesvcs:4fef15a7e4b0360bc71dd7c4 edgesvcs:4fef1715e4b0258b346d4bc3 edgesvcs:4fef18bae4b00c3a09445257 edgesvcs:4fef1c10e4b0360bc71dd7c5 edgesvcs:4fef297be4b080283d5c033e edgesvcs:4fef29a5e4b0258b346d4bc4 edgesvcs:4fef29b1e4b0360bc71dd7c6 edgesvcs:4fef2c78e4b0258b346d4bc6 edgesvcs:4fef5b1be4b0360bc71dd7c7 edgesvcs:4ff60eaee4b0da234f941746 edgesvcs:4ff60edae4b080283d5c034f edgesvcs:4ff60ef4e4b0cd8349110f2f edgesvcs:4ff60f08e4b0b6a92c2d02a3 edgesvcs:4ff60f22e4b0b6a92c2d02a4 edgesvcs:4ff60f41e4b0e80b01ea52d1 edgesvcs:4ff60f81e4b0cd8349110f30 edgesvcs:4ff60fb3e4b0b6a92c2d02a5 edgesvcs:4ff60ff5e4b0258b346d4bdd edgesvcs:4ff6102be4b0da234f941748 edgesvcs:4ff61396e4b0b6a92c2d02a7 edgesvcs:4ff6dd6de4b0cd8349110f31 edgesvcs:4ffc6820e4b00c3a0944526c edgesvcs:5009a70ce4b0d0406d1a3caa edgesvcs:5009a71fe4b0043715577bd6 edgesvcs:5009a733e4b00a2cae5003db edgesvcs:500ef978e4b051caead958a1 edgesvcs:500f002fe4b0d337bfe27f22 edgesvcs:500f00d1e4b051caead958ab edgesvcs:500f00e6e4b0d337bfe27f23 edgesvcs:500f0adfe4b051caead958ad edgesvcs:500f0bc2e4b0e7d8b59801f8 edgesvcs:500f13e4e4b051caead958b0 edgesvcs:500f1405e4b0d337bfe27f28 edgesvcs:50115bd4e4b051caead958bb edgesvcs:50115c20e4b051caead958bd edgesvcs:50115c39e4b051caead958be edgesvcs:50115c43e4b0200501d878e2 edgesvcs:50115c5ee4b051caead958bf edgesvcs:50115c74e4b051caead958c0 edgesvcs:50115cd1e4b0d337bfe27f33 edgesvcs:50115cf7e4b0200501d878e4 edgesvcs:50115d12e4b0d337bfe27f34 edgesvcs:50115de8e4b0d337bfe27f35 edgesvcs:50115dfae4b0200501d878e5 edgesvcs:50194737e4b0e7d8b5980258 wdpro-api-public-crud wdpro-authentication-general-crud wdpro-availability-general-crud wdpro-booking-general-crud wdpro-bulk-general-crud wdpro-cart-general-crud wdpro-cruise-booking-cart-keepalive-crud wdpro-facility-general-r wdpro-finder-general-r wdpro-health-monitor-general-r wdpro-lists-general-crud wdpro-lodging-general-crud wdpro-media-general-r wdpro-payment-general-crud wdpro-personalmagic-general-crud wdpro-pricing-general-r wdpro-product-general-r wdpro-profile-dob-u wdpro-profile-public-cr wdpro-profile-vertical-crud wdpro-recommendation-general-crud wdpro-recommendation-public-r wdpro-reservation-agent-crud wdpro-reservation-general-crud wdpro-reservation-itinerary-r wdpro-review-general-r\"}";
    	when(spy,"validateScope", this.tokenList, "GET", scopeItem,json).thenReturn(true);
    
    }
	@Test
    public void testvalidateStarScope() throws Exception
    {
    	AuthFilter authFilter	= PowerMockito.mock(AuthFilter.class);	
    	AuthFilter spy	=	spy(authFilter);
    	//AuthFilter authFilter	=	new AuthFilter();
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setContextPath("/star-service");
    	AuthDO scopeItem = loadScopeItem(request.getContextPath());
    	String json="{\"scope\":\"disneyid-compliance-b2b-read-information disneyid-compliance-b2b-read-permissions disneyid-compliance-b2b-update-permissions disneyid-compliance-guest-read-information disneyid-compliance-guest-read-permissions disneyid-compliance-guest-update-permissions disneyid-profile-b2b-create disneyid-profile-guest-disable disneyid-profile-guest-read disneyid-profile-guest-update dtss-grx-public edgesvcs:4fb268cbe4b048c4a74ad9b4 edgesvcs:4fb3d8c9e4b0300ee8a2ed23 edgesvcs:4fc51383e4b081539a5ab350 edgesvcs:4fd368d7e4b0939536d134c7 edgesvcs:4fd380bee4b033db40328c07 edgesvcs:4fd38e61e4b033db40328c08 edgesvcs:4fd38eace4b0939536d134c8 edgesvcs:4fd607bce4b0939536d134ca edgesvcs:4fd61aa4e4b0939536d134cb edgesvcs:4fda9d9ee4b091063045de90 edgesvcs:4fdb598fe4b091063045dea6 edgesvcs:4fdb59a2e4b091063045dea7 edgesvcs:4fdb59dfe4b091063045dea8 edgesvcs:4fdb5a5fe4b091063045dea9 edgesvcs:4fdb5a94e4b091063045deaa edgesvcs:4fdb5aaee4b038e3e5e38a31 edgesvcs:4fdb5acbe4b038e3e5e38a32 edgesvcs:4fe4c646e4b079849be7b84d edgesvcs:4fef1341e4b0da234f941731 edgesvcs:4fef13c4e4b0cd8349110f21 edgesvcs:4fef1569e4b0b6a92c2d0290 edgesvcs:4fef1580e4b0cd8349110f22 edgesvcs:4fef15a7e4b0360bc71dd7c4 edgesvcs:4fef1715e4b0258b346d4bc3 edgesvcs:4fef18bae4b00c3a09445257 edgesvcs:4fef1c10e4b0360bc71dd7c5 edgesvcs:4fef297be4b080283d5c033e edgesvcs:4fef29a5e4b0258b346d4bc4 edgesvcs:4fef29b1e4b0360bc71dd7c6 edgesvcs:4fef2c78e4b0258b346d4bc6 edgesvcs:4fef5b1be4b0360bc71dd7c7 edgesvcs:4ff60eaee4b0da234f941746 edgesvcs:4ff60edae4b080283d5c034f edgesvcs:4ff60ef4e4b0cd8349110f2f edgesvcs:4ff60f08e4b0b6a92c2d02a3 edgesvcs:4ff60f22e4b0b6a92c2d02a4 edgesvcs:4ff60f41e4b0e80b01ea52d1 edgesvcs:4ff60f81e4b0cd8349110f30 edgesvcs:4ff60fb3e4b0b6a92c2d02a5 edgesvcs:4ff60ff5e4b0258b346d4bdd edgesvcs:4ff6102be4b0da234f941748 edgesvcs:4ff61396e4b0b6a92c2d02a7 edgesvcs:4ff6dd6de4b0cd8349110f31 edgesvcs:4ffc6820e4b00c3a0944526c edgesvcs:5009a70ce4b0d0406d1a3caa edgesvcs:5009a71fe4b0043715577bd6 edgesvcs:5009a733e4b00a2cae5003db edgesvcs:500ef978e4b051caead958a1 edgesvcs:500f002fe4b0d337bfe27f22 edgesvcs:500f00d1e4b051caead958ab edgesvcs:500f00e6e4b0d337bfe27f23 edgesvcs:500f0adfe4b051caead958ad edgesvcs:500f0bc2e4b0e7d8b59801f8 edgesvcs:500f13e4e4b051caead958b0 edgesvcs:500f1405e4b0d337bfe27f28 edgesvcs:50115bd4e4b051caead958bb edgesvcs:50115c20e4b051caead958bd edgesvcs:50115c39e4b051caead958be edgesvcs:50115c43e4b0200501d878e2 edgesvcs:50115c5ee4b051caead958bf edgesvcs:50115c74e4b051caead958c0 edgesvcs:50115cd1e4b0d337bfe27f33 edgesvcs:50115cf7e4b0200501d878e4 edgesvcs:50115d12e4b0d337bfe27f34 edgesvcs:50115de8e4b0d337bfe27f35 edgesvcs:50115dfae4b0200501d878e5 edgesvcs:50194737e4b0e7d8b5980258 wdpro-api-public-crud wdpro-authentication-general-crud wdpro-availability-general-crud wdpro-booking-general-crud wdpro-bulk-general-crud wdpro-cart-general-crud wdpro-cruise-booking-cart-keepalive-crud wdpro-facility-general-r wdpro-finder-general-r wdpro-health-monitor-general-r wdpro-lists-general-crud wdpro-lodging-general-crud wdpro-media-general-r wdpro-payment-general-crud wdpro-personalmagic-general-crud wdpro-pricing-general-r wdpro-product-general-r wdpro-profile-dob-u wdpro-profile-public-cr wdpro-profile-vertical-crud wdpro-recommendation-general-crud wdpro-recommendation-public-r wdpro-reservation-agent-crud wdpro-reservation-general-crud wdpro-reservation-itinerary-r wdpro-review-general-r\"}";
    	when(spy,"validateScope", this.tokenList, "GET", scopeItem,json).thenReturn(true);
    
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
