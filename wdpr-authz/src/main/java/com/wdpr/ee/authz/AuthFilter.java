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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;






import com.wdpr.ee.authz.model.AuthDO;
import com.wdpr.ee.authz.model.TokenDO;
import com.wdpr.ee.authz.util.AuthConstants;
import com.wdpr.ee.authz.util.JSONConfigLoader;

/***************************************************************************************************
* FileName - AuthFilter.java
* Desc:  Security filter servlet class to validate header parameters , Client app can directly plug-in 
*  filter-mapping in their web.xml  
* (c) Disney. All rights reserved.
*
* $Author:  $nixon
* $Revision:  $
* $Change:  $
* $Date: $
********************************************************************************************************/
@WebFilter("/AuthFilter")
public class AuthFilter implements Filter {
	private static final Logger logger = LogManager.getLogger(AuthFilter.class);
	private ServletContext context;
	RestConnector connector = RestConnector.getInstance();
	Map<String, AuthDO> scopeMap = JSONConfigLoader.getInstance().loadScopeData();

	public AuthFilter() {

	}

	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
		this.context.log("AuthFilter initialized");
	}
	


	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		
		boolean authRequired = false, authSuccess=false, scopeRequired = false, scopeValid = false;
		Map<String, String> cookieMap = new ConcurrentHashMap<>();
		Map<String, String> tokenList = new  HashMap<>();
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;	
		
		AuthDO scopeItem = loadScopeItem(req.getContextPath());
		if  (scopeItem != null ){
			authRequired = scopeItem.isAuthTokenRequired();
			scopeRequired = scopeItem.getScopesAllowed().length > 0;
		}

		if (req.getHeader(AuthConstants.ACCESS_TOKEN) == null && authRequired == true ) {
			loadCookieData(req,cookieMap);//TODO: TBD if the request params through cookie (UI apps)
			if(cookieMap.get(AuthConstants.ACCESS_TOKEN) != null){
				tokenList.put(AuthConstants.ACCESS_TOKEN, cookieMap.get(AuthConstants.ACCESS_TOKEN));	
			}
		}			
	
		tokenList = loadHeaders(req);

		if (tokenList.size() == 0 && (authRequired || scopeRequired)) {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return ;
		}
		 
		try {

			// Proceeding with  HTTP TOKEN authentication			
			if (authRequired){
				authSuccess = connector.callGoDotComValidateToken(tokenList);	
	
			}

			/*
			 * Assumptions made that  API(URI) & its required scope will be defined in scope.json. If the incoming 
			 * URI has entry in scope.json the filter will get uses scope and check uses has all required scope. 
			 */
			if (scopeRequired){
				scopeValid = validateScope(tokenList,scopeItem,res);						
			}
			
			if ((authSuccess &&(!scopeRequired || scopeValid) ) || (scopeItem == null) ){
				logger.info("Success- Auth/Scope : scopeRequired"+scopeRequired);//TODO:TBR
				chain.doFilter(request, response);
			}else{
				res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);	
				return ;
			}
		} finally {

		}
		
	}
	
	/**
	 * @param req
	 * @param tokenList
	 */
	public Map<String, String> loadHeaders(HttpServletRequest req){
		Map<String, String> tokenList = new  HashMap<>();;
		Enumeration<String> headerNames = req.getHeaderNames();		
		while (headerNames != null &&headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = req.getHeader(key);
			tokenList.put(key, value);
		}
		return tokenList;
		
	}
	/**
	 * @param ctxPath
	 * @return
	 */
	private AuthDO loadScopeItem(String ctxPath){
		AuthDO scopeItem = null; 
		for (String key : scopeMap.keySet()) {
			if (Pattern.matches(key, ctxPath)){
				scopeItem = scopeMap.get(key);			
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
	private boolean validateScope(Map<String, String> tokenList, AuthDO scopeItem, HttpServletResponse res) throws IOException {
		boolean isScopeValid = false;
		TokenDO respObj = connector.callGoDotComValidateScope(tokenList);
		if (respObj.getScope() != null) {
			for (String scope : scopeItem.getScopesRequired()) {
				if (respObj.getScope().contains(scope)) {
					isScopeValid = true;
				} else {
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
	public void loadCookieData(HttpServletRequest request, Map<String, String> cookieMap ){
		  Cookie cookie = null;
		  Cookie[] cookies = null;
	      // Get an array of Cookies associated with this domain
	      cookies = request.getCookies();
	
	      if( cookies != null ){
	        
	         for (int i = 0; i < cookies.length; i++){
	            cookie = cookies[i];
	            cookieMap.put(cookie.getName(), cookie.getValue());

	         }
	      }else{
	    	  logger.info("No cookies found");
	      }

	   
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
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

	private Map<String, String> headerMap = new HashMap<String, String>();

	/**
	 * add a header with given name and value
	 * 
	 * @param name
	 * @param value
	 */
	public void addHeader(String name, String value) {
		headerMap.put(name, value);
	}

	/**
	 * replace the normal get header
	 */
	@Override
	public String getHeader(String name) {
		String headerValue = super.getHeader(name);
		if (headerMap.containsKey(name)) {
			headerValue = headerMap.get(name);
		}
		return headerValue;
	}

	/**
	 * get the Header names
	 */
	@Override
	public Enumeration<String> getHeaderNames() {
		List<String> names = Collections.list(super.getHeaderNames());
		for (String name : headerMap.keySet()) {
			names.add(name);
		}
		return Collections.enumeration(names);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		List<String> values = Collections.list(super.getHeaders(name));
		if (headerMap.containsKey(name)) {
			values.add(headerMap.get(name));
		}
		return Collections.enumeration(values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HeaderMapRequestWrapper [headerMap=" + headerMap + "]";
	}
	
	

}
