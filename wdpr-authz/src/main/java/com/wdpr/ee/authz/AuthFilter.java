package com.wdpr.ee.authz;

import com.wdpr.ee.authz.model.AuthDO;
import com.wdpr.ee.authz.model.TokenDO;
import com.wdpr.ee.authz.util.AuthConstants;
import com.wdpr.ee.authz.util.JSONConfigLoader;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/***************************************************************************************************
 * FileName - AuthFilter.java Desc: Security filter servlet class to validate
 * header parameters , Client app can directly plug-in filter-mapping in their
 * web.xml (c) Disney. All rights reserved.
 *
 * $Author: $nixon $Revision: $ $Change: $ $Date: $
 ********************************************************************************************************/
@WebFilter("/AuthFilter")
public class AuthFilter implements Filter
{
    private static final Logger LOG = LogManager.getLogger(AuthFilter.class);
    private ServletContext context;
    /**
     * Singleton instance of Resr Connector
     */
    RestConnector connector = RestConnector.getInstance();
    /**
     * Map<String, AuthDO> containing scope data
     */
    Map<String, AuthDO> scopeMap = JSONConfigLoader.getInstance().loadScopeData();

    /**
     * Authorization Filter class
     */
    public AuthFilter()
    {

    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig)
    {
        this.context = fConfig.getServletContext();
        this.context.log("AuthFilter initialized");
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {

        boolean authRequired = false, authSuccess = false, scopeRequired = false, scopeValid = false;
        Map<String, String> cookieMap = new ConcurrentHashMap<>();
        Map<String, String> tokenList = new HashMap<>();

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        AuthDO scopeItem = loadScopeItem(req.getContextPath());
        if (scopeItem != null)
        {
            authRequired = scopeItem.isAuthTokenRequired();
            scopeRequired = scopeItem.getScopesAllowed().length > 0;
        }

        tokenList = loadHeaders(req);
        LOG.info(req.getHeaderNames());
        String token = req.getHeader(AuthConstants.ACCESS_TOKEN);
        if (token == null)
        {
            token = req.getHeader(AuthConstants.AUTHORIZATION);
            if (token != null && token.indexOf(AuthConstants.BEARER)>-1)
            {
                // Authorization value: 'BEARER <access token>'
                token = token.substring(token.indexOf(AuthConstants.BEARER)+7);
                tokenList.put(AuthConstants.ACCESS_TOKEN, token);
            }
        }
        if (token == null && authRequired == true)
        {
            loadCookieData(req, cookieMap);// TODO: TBD if the request params
                                           // through cookie (UI apps)
            if (cookieMap.get(AuthConstants.ACCESS_TOKEN) != null)
            {
                tokenList
                        .put(AuthConstants.ACCESS_TOKEN, cookieMap.get(AuthConstants.ACCESS_TOKEN));
            }
        }

        if (tokenList.size() == 0 && (authRequired || scopeRequired))
        {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try
        {
            if (authRequired)
            {
                // HTTP TOKEN authentication: External OAuth call to URL /validate
                authSuccess = this.connector.callGoDotComValidateToken(tokenList);
            }

            /*
             * Assumptions made that API(URI) & its required scope will be
             * defined in scope.json. If the incoming URI has entry in
             * scope.json the filter will get uses scope and check uses has all
             * required scope.
             */
            if (scopeRequired)
            {
                // TODO Validate returned scopes from /validate against required scopes in configuration
                scopeValid = validateScope(tokenList, scopeItem, res);
            }

            if ((authSuccess && (!scopeRequired || scopeValid)) || (scopeItem == null))
            {
                LOG.info("Success- Auth/Scope : scopeRequired=" + scopeRequired);// TODO:TBR
                chain.doFilter(request, response);
            }
            else
            {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        finally
        {
            // Avoid compiler warning
        }
    }

    /**
     * @param req
     * @return Map of header keys and values
     */
    public Map<String, String> loadHeaders(HttpServletRequest req)
    {
        Map<String, String> tokenList = new HashMap<>();
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements())
        {
            String key = headerNames.nextElement();
            String value = req.getHeader(key);
            tokenList.put(key, value);
        }
        return tokenList;

    }

    /**
     * @param ctxPath
     * @return
     */
    private AuthDO loadScopeItem(String ctxPath)
    {
        AuthDO scopeItem = null;
        for (String key : this.scopeMap.keySet())
        {
            if (Pattern.matches(key, ctxPath))
            {
                scopeItem = this.scopeMap.get(key);
                return scopeItem;
            }
        }
        return scopeItem;
    }

    /**
     * @param tokenList
     * @param scopeItem
     * @param res
     * @return
     * @throws IOException
     */
    private boolean validateScope(Map<String, String> tokenList, AuthDO scopeItem,
            HttpServletResponse res) throws IOException
    {
        boolean isScopeValid = false;
        //TODO This is only needed if token has not been validated
        TokenDO respObj = this.connector.callGoDotComValidateScope(tokenList);
        if (respObj != null && respObj.getScope() != null)
        {
            for (String scope : scopeItem.getScopesRequired())
            {
                if (respObj.getScope().contains(scope))
                {
                    isScopeValid = true;
                }
                else
                {
                    return false;
                }

            }
        }
        return isScopeValid;
    }

    /**
     * @param request
     * @param cookieMap
     */
    public void loadCookieData(HttpServletRequest request, Map<String, String> cookieMap)
    {
        Cookie cookie = null;
        Cookie[] cookies = null;
        // Get an array of Cookies associated with this domain
        cookies = request.getCookies();

        if (cookies != null)
        {

            for (int i = 0; i < cookies.length; i++)
            {
                cookie = cookies[i];
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        else
        {
            LOG.info("No cookies found");
        }
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy()
    {
        // we can close resources here
    }
}

/**
 * allows adding additional header entries to a request This is used when you
 * have an HttpServletReuest, not needed for now
 *
 */
class HeaderMapRequestWrapper extends HttpServletRequestWrapper
{
    /**
     * construct a wrapper for this request
     *
     * @param request
     */
    public HeaderMapRequestWrapper(HttpServletRequest request)
    {
        super(request);
    }

    private Map<String, String> headerMap = new HashMap<>();

    /**
     * add a header with given name and value
     *
     * @param name
     * @param value
     */
    public void addHeader(String name, String value)
    {
        this.headerMap.put(name, value);
    }

    /**
     * replace the normal get header
     */
    @Override
    public String getHeader(String name)
    {
        String headerValue = super.getHeader(name);
        if (this.headerMap.containsKey(name))
        {
            headerValue = this.headerMap.get(name);
        }
        return headerValue;
    }

    /**
     * get the Header names
     */
    @Override
    public Enumeration<String> getHeaderNames()
    {
        List<String> names = Collections.list(super.getHeaderNames());
        for (String name : this.headerMap.keySet())
        {
            names.add(name);
        }
        return Collections.enumeration(names);
    }

    /**
     * @see javax.servlet.http.HttpServletRequestWrapper#getHeaders(String)
     */
    @Override
    public Enumeration<String> getHeaders(String name)
    {
        List<String> values = Collections.list(super.getHeaders(name));
        if (this.headerMap.containsKey(name))
        {
            values.add(this.headerMap.get(name));
        }
        return Collections.enumeration(values);
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return "HeaderMapRequestWrapper [headerMap=" + this.headerMap + "]";
    }
}
