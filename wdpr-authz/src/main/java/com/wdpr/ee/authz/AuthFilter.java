package com.wdpr.ee.authz;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.wdpr.ee.authz.model.AuthDO;
import com.wdpr.ee.authz.model.AuthDO.Scope;
import com.wdpr.ee.authz.scopevalidation.ClientIdValidator;
import com.wdpr.ee.authz.util.AuthConstants;
import com.wdpr.ee.authz.util.JSONConfigLoader;

/***************************************************************************************************
 * FileName - AuthFilter.java Desc: Security filter servlet class to validate
 * header parameters , Client app can directly plug-in filter-mapping in their
 * web.xml (c) Disney. All rights reserved.
 *
 * $Author: $nixon $Revision: $ $Change: $ $Date: $
 ********************************************************************************************************/
@WebFilter("/AuthFilter")
public class AuthFilter implements Filter {
	private static final Logger LOG = LogManager.getLogger(AuthFilter.class);
	private ServletContext context;
	/**
	 * Singleton instance of Rest Connector
	 */
	RestConnector connector = RestConnector.getInstance();
	/**
	 * Map<String, AuthDO> containing scope data
	 */
	Map<String, AuthDO> scopeMap = JSONConfigLoader.getInstance()
			.loadScopeData();

	/**
	 * Authorization Filter class
	 */
	public AuthFilter() {

	}

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) {
		this.context = fConfig.getServletContext();
		this.context.log("AuthFilter initialized");
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		boolean authRequired = false;
		boolean scopeRequired = false;
		boolean scopeValid = false;
		Map<String, String> cookieMap = new ConcurrentHashMap<>();
		Map<String, String> tokenList = new HashMap<>();

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		AuthDO scopeItem = loadScopeItem(req.getContextPath());
		if (scopeItem != null) {
			authRequired = scopeItem.isAuthTokenRequired();
			scopeRequired = scopeItem.getScopes().length > 0;
		}
		LOG.debug("#### Request headers = " + req.getHeaderNames());
		tokenList = loadHeaders(req);
		LOG.debug("#### Request headers = " + req.getHeaderNames());
		String method = req.getMethod();
		LOG.debug("#### Request Method = " + method);
		String token = req.getHeader(AuthConstants.ACCESS_TOKEN);
		if (token == null) {
			token = req.getHeader(AuthConstants.AUTHORIZATION);
			if (token != null && token.indexOf(AuthConstants.BEARER) > -1) {
				// Authorization value: 'BEARER <access token>'
				token = token
						.substring(token.indexOf(AuthConstants.BEARER) + 7);
				tokenList.put(AuthConstants.ACCESS_TOKEN, token);
			}
		}
		if (token == null && authRequired == true) {
			loadCookieData(req, cookieMap);// TODO: TBD if the request params
											// through cookie (UI apps)
			if (cookieMap.get(AuthConstants.ACCESS_TOKEN) != null) {
				tokenList.put(AuthConstants.ACCESS_TOKEN,
						cookieMap.get(AuthConstants.ACCESS_TOKEN));
			}
		}

		if (tokenList.size() == 0 && (authRequired || scopeRequired)) {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		try {
			String json = null;
			if (authRequired) {
				// HTTP TOKEN authentication: External OAuth call to URL
				// /validate
				json = this.connector.callGoDotComValidateToken(tokenList);
			}

			/*
			 * Assumptions made that API(URI) & its required scope will be
			 * defined in scope.json. If the incoming URI has entry in
			 * scope.json the filter will get uses scope and check uses has all
			 * required scope.
			 */
			if (json != null && scopeRequired) {
				// TODO Validate returned scopes from /validate against required
				// scopes in configuration
				scopeValid = validateScope(tokenList, method, scopeItem, json);
			}

			if ((json != null && (!scopeRequired || scopeValid))
					|| (scopeItem == null)) {
				LOG.info("Success- Auth/Scope : scopeRequired=" + scopeRequired);// TODO:TBR
				chain.doFilter(request, response);
			} else {
				res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		} finally {
			// Avoid compiler warning
		}
	}

	/**
	 * @param req
	 * @return Map of header keys and values
	 */
	public Map<String, String> loadHeaders(HttpServletRequest req) {
		Map<String, String> tokenList = new HashMap<>();
		Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames != null && headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			String value = req.getHeader(key);
			tokenList.put(key, value);
		}
		return tokenList;

	}

	/**
	 * Loads the scopes defined in the scope.json document that match/apply to the incoming request URL context
	 * @param reqCtx The context  of the URL for the incoming request
	 * @return A collection of scopes for the incoming URL to be validated against the token for the incoming reuqest
	 */
	private AuthDO loadScopeItem(String reqCtx) {
		AuthDO scopeItem = null;
		StringBuilder msg = new StringBuilder();
		for (String scopeCtx : this.scopeMap.keySet()) {
			msg.delete(0, msg.length());
			if (Pattern.matches(scopeCtx,reqCtx)) {
				scopeItem = this.scopeMap.get(scopeCtx);
				msg.append("#### Matched the incoming request context ");
				msg.append(reqCtx);
				msg.append(" with the configured scope context ");
				msg.append(scopeCtx);
				LOG.debug(msg.toString());
				return scopeItem;
			} else {
				msg.append("#### Unseccessful match of configured scope context");
				msg.append(scopeCtx);
				msg.append(" with the incoming request context ");
				msg.append(reqCtx);				
				LOG.debug(msg.toString());				
			}
		}
		LOG.debug("#### Context  from request = " + context);
		LOG.debug("#### Scope Items (AthDO object)   from context path = "
				+ scopeItem);
		return scopeItem;
	}

	/**
	 * @param tokenList
	 *            The authentication tokens contained in the response
	 * @param method
	 *            The method (GET, POST, PUT, DELETE) called
	 * @param configScopes
	 *            Scope being validated
	 * @return true if the required scope has been met
	 * @throws IOException
	 */
	@SuppressWarnings("boxing")
	private boolean validateScope(Map<String, String> tokenList, String method,
			AuthDO configScopes, String json) throws IOException {
		boolean isScopeValid = false;

		List<String> authZScopes = null;
		ClientIdValidator validator = new ClientIdValidator();
		try {
			authZScopes = validator.validateScopes(json);
			StringBuilder msg = new StringBuilder();

			for (String authZScope : authZScopes) {
				if (isScopeValid == true) {
					break;
				}
				for (Scope configScope : configScopes.getScopes()) {
					String configuredMethod = method.toLowerCase();
					String authZmethod = configScope.getMethod().toLowerCase();
					if (!(method.equalsIgnoreCase(configScope.getMethod()) || configScope
							.getMethod().equals('*'))) {
						msg.append("Could not match authZ method of:");
						msg.append(authZmethod);
						msg.append(" with confitgured method of ");
						msg.append(configuredMethod);
						LOG.warn(msg.toString());
						break;
					}

					for (String allowedScope : configScope.getScopesAllowed()) {
						if (allowedScope.equals("*")) {
							msg.append("Configured scope did not requrie AuthZ scope (configured scope = *) . Token is valid");
							LOG.debug(msg.toString());
							isScopeValid = true;
							break;
						}
						if (authZScope.contains(allowedScope)) {
							msg.append("AuthZ allowed scope of:");
							msg.append(allowedScope);
							msg.append((" found in configured scopes of:"));
							msg.append(authZScope);
							LOG.debug(msg.toString());
							isScopeValid = true;
							break;
						}
					}
				}
			}

		} catch (Exception ex) {
			LOG.error(ex);
		}
		return isScopeValid;
	}

	/**
	 * @param request
	 * @param cookieMap
	 */
	public void loadCookieData(HttpServletRequest request,
			Map<String, String> cookieMap) {
		Cookie cookie = null;
		Cookie[] cookies = null;
		// Get an array of Cookies associated with this domain
		cookies = request.getCookies();

		if (cookies != null) {

			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];
				cookieMap.put(cookie.getName(), cookie.getValue());
			}
		} else {
			LOG.warn("No cookies found for " + request);
			LOG.warn("No cookies found for " + request.getRequestURL());
		}
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// we can close resources here
	}
}

/**
 * allows adding additional header entries to a request This is used when you
 * have an HttpServletReuest, not needed for now
 *
 */
class HeaderMapRequestWrapper extends HttpServletRequestWrapper {
	/**
	 * construct a wrapper for this request
	 *
	 * @param request
	 */
	public HeaderMapRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	private Map<String, String> headerMap = new HashMap<>();

	/**
	 * add a header with given name and value
	 *
	 * @param name
	 * @param value
	 */
	public void addHeader(String name, String value) {
		this.headerMap.put(name, value);
	}

	/**
	 * replace the normal get header
	 */
	@Override
	public String getHeader(String name) {
		String headerValue = super.getHeader(name);
		if (this.headerMap.containsKey(name)) {
			headerValue = this.headerMap.get(name);
		}
		return headerValue;
	}

	/**
	 * get the Header names
	 */
	@Override
	public Enumeration<String> getHeaderNames() {
		List<String> names = Collections.list(super.getHeaderNames());
		for (String name : this.headerMap.keySet()) {
			names.add(name);
		}
		return Collections.enumeration(names);
	}

	/**
	 * @see javax.servlet.http.HttpServletRequestWrapper#getHeaders(String)
	 */
	@Override
	public Enumeration<String> getHeaders(String name) {
		List<String> values = Collections.list(super.getHeaders(name));
		if (this.headerMap.containsKey(name)) {
			values.add(this.headerMap.get(name));
		}
		return Collections.enumeration(values);
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return "HeaderMapRequestWrapper [headerMap=" + this.headerMap + "]";
	}
}
