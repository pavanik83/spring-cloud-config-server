package com.wdpr.ee.authz;

import java.io.IOException;
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
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    Map<String, AuthDO> scopeMap = JSONConfigLoader.getInstance().loadScopeData();
    /**
     * Array of PAtterns from Scope URL, used to match request URLs to vlaidate
     * scope
     */
    Pattern[] scopePatterns = new Pattern[scopeMap.size()];

    /**
     * Authorization Filter constructor. Load the required scopes from
     * scopes.json as regex patterns for matching with request URLs to validate
     * scopes
     */
    public AuthFilter() {
        loadScopePatterns();
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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
    ServletException {

        double start = System.nanoTime();
        boolean authRequired = false;
        boolean scopeRequired = false;
        boolean scopeValid = false;
        StringBuilder msg = new StringBuilder();
        Map<String, String> cookieMap = new ConcurrentHashMap<>();
        Map<String, String> tokenList = new HashMap<>();

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        AuthDO scopeItem = loadScopeItem(req.getRequestURI());  // this was using the context, but in order to protect individual endpoints in a single service
        msg.delete(0, msg.length());
        msg.append("#### scopeItem =  ").append(scopeItem).append(" context = ").append(req.getContextPath());

        LOG.debug(msg.toString());

        if (scopeItem != null) {
            authRequired = scopeItem.isAuthTokenRequired();
            scopeRequired = scopeItem.getScopes().length > 0;
        }

        msg.delete(0, msg.length());
        msg.append("#### Auth required = ").append(authRequired).append(", Scope required = ").append(scopeRequired);

        LOG.debug(msg.toString());

        tokenList = loadHeaders(req);

        msg.delete(0, msg.length());
        msg.append("#### Request headers = ").append(req.getHeaderNames());
        LOG.debug(msg.toString());
        String method = req.getMethod();
        msg.delete(0, msg.length());
        msg.append("#### Request method = ").append(method);

        LOG.debug(msg.toString());

        String token = req.getHeader(AuthConstants.AUTHORIZATION);
        if (token != null && token.indexOf(AuthConstants.BEARER) > -1) {
            // Authorization value: 'BEARER <access token>'
            token = token.substring(token.indexOf(AuthConstants.BEARER) + 7);
            tokenList.put(AuthConstants.AUTHORIZATION, token);
        }
        if (token == null && authRequired == true) {
            loadCookieData(req, cookieMap);
            if (cookieMap.get(AuthConstants.ACCESS_TOKEN) != null) {
                tokenList.put(AuthConstants.AUTHORIZATION, cookieMap.get(AuthConstants.ACCESS_TOKEN));
            }
        }
        msg.delete(0, msg.length());
        msg.append("#### Token list = ").append(tokenList);

        LOG.debug(msg.toString());

        if (tokenList.size() == 0 && (authRequired || scopeRequired)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String json = null;
        if (authRequired) {
            // External AuthZ call to validate token/fetch any defined scopes
            json = this.connector.callGoDotComValidateToken(tokenList);
        }

        // validate that token has required scope defined in scopes.json
        if (json != null && scopeRequired) {
            scopeValid = validateScope(tokenList, method, scopeItem, json);
        }

        // Capturing time spent in pass through the filter
        double end = System.nanoTime();
        double elapsed = end - start;

        if ((json != null && (!scopeRequired || scopeValid)) || (scopeItem == null)) {
            msg.delete(0, msg.length());
            msg.append("Successful validation using token ").append(token).append("- Auth/Scope : scopeRequired=").append(scopeRequired);

            LOG.info(msg.toString());

            msg.delete(0, msg.length());
            msg.append("#### Time for authz filter exectuion with VALID token is ").append(Double.toString(elapsed / 1000000)).append(" milliseconds");

            LOG.debug(msg.toString());

            chain.doFilter(request, response);
        } else {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            LOG.debug(("#### Time for authz filter execution with INVALID token is ")
                    + Double.toString(elapsed / 1000000) + " milliseconds");
        }

        return;
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
     * Loads the scopes defined in the scope.json document that match/apply to
     * the incoming request URL context
     * 
     * @param reqCtx
     *            The context of the URL for the incoming request
     * @return A collection of scopes for the incoming URL to be validated
     *         against the token for the incoming request
     */
    private void loadScopePatterns() {

        StringBuilder msg = new StringBuilder();
        int count = 0;
        for (String scope : this.scopeMap.keySet()) {
            scopePatterns[count] = Pattern.compile(scope);
            msg.delete(0, msg.length());
            msg.append("#### Loaded required scope ").append(scope);
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

        for (int i = 0; i < scopePatterns.length; i++) {
            msg.delete(0, msg.length());
            String scopeKey = scopePatterns[i].pattern();
            if (scopePatterns[i].matcher(reqCtx).matches()) {
                scopeItem = this.scopeMap.get(scopeKey);
                msg.append("#### Matched the incoming request context ").append(reqCtx).append(" with the configured scope context ").append(scopeKey);
                LOG.debug(msg.toString());
                return scopeItem;
            } else {
                msg.append("#### Unsuccessful match of configured scope context").append(scopeKey).append(" with the incoming request context ").append(reqCtx);
                LOG.debug(msg.toString());
            }
        }

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
    private boolean validateScope(Map<String, String> tokenList, String method, AuthDO configScopes, String json)
            throws IOException {

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
                    if (!(method.equalsIgnoreCase(configScope.getMethod()) || configScope.getMethod().equals('*'))) {
                        msg.append("Could not match authZ method of:").append(authZmethod).append(" with confitgured method of ").append(configuredMethod);
                        LOG.warn(msg.toString());
                        break;
                    }

                    for (String allowedScope : configScope.getScopesAllowed()) {
                        if (allowedScope.equals("*")) {
                            msg.append("Configured scope did not requrie AuthZ scope (configured scope = *) . Token is valid");
                            // LOG.debug(msg.toString());
                            isScopeValid = true;
                            break;
                        }
                        if (authZScope.contains(allowedScope)) {
                            msg.append("AuthZ allowed scope of:").append(allowedScope).append((" found in configured scopes of:")).append(authZScope);
                            // LOG.debug(msg.toString());
                            isScopeValid = true;
                            break;
                        }
                    }
                }
            }

        } catch (Exception ex) {
            LOG.error("****Check if the scope returned from scope.json are as per the standard for the filter****", ex);
        }

        return isScopeValid;
    }

    /**
     * @param request
     * @param cookieMap
     */
    public void loadCookieData(HttpServletRequest request, Map<String, String> cookieMap) {

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

