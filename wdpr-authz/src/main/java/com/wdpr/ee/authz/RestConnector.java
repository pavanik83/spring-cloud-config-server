package com.wdpr.ee.authz;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.cache.CacheResponseStatus;
import org.apache.http.client.cache.HttpCacheContext;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.wdpr.ee.authz.model.TokenDO;
import com.wdpr.ee.authz.util.AuthConfig;
import com.wdpr.ee.authz.util.AuthConstants;

/***************************************************************************************************
 * FileName - RestConnector.java Desc: HttpClient class to invoke Rest service
 * (auth.go.com) , Support Get/POST (c) Disney. All rights reserved.
 *
 * $Author: $nixon $Revision: $ $Change: $ $Date: $
 ********************************************************************************************************/

public class RestConnector
{

 private static final Logger LOG = LogManager.getLogger(RestConnector.class);

 /**
  * @see com.wdpr.ee.authz.RestConnector
  */
 public static RestConnector restConnector;
 /**
  * @see com.wdpr.ee.authz.util.AuthConfig
  */
 public static AuthConfig config = AuthConfig.getInstance();

 /**
  * REST protocol
  */
 String PROTOCOL;
 /**
  * REST host
  */
 String HOST;
 /**
  * REST auth_path
  */
 String AUTH_PATH;
 /**
  * REST scope_path
  */
 String SCOPE_PATH;
 /**
  * REST port
  */
 int PORT;
 /**
  * REST timeout
  */
 int TIME_OUT;

 /**
  * Maps from JSON response to Java object AuthDO
  */
 ObjectMapper mapper = new ObjectMapper();
 /**
  * Configures timeouts and stale connection check
  */
 static RequestConfig defaultRequestConfig;
 /**
  * Configures max cache entries, size
  */
 static CacheConfig cacheConfig;
 static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
 /**
  * Adds cache config and request config to Apache http client
  */
 // static CloseableHttpClient httpClient;
 CloseableHttpClient httpClient = HttpClients.custom()
   .setConnectionManager(connManager).build();

 @SuppressWarnings("deprecation")
 private RestConnector()
 {
  // Set some shared constants
  initSharedConstatnts();
  // Set the HttpClient cache
  initCache();
  httpClient = CachingHttpClients.custom().setCacheConfig(cacheConfig)
    .setDefaultRequestConfig(defaultRequestConfig).build();
  // Set the HttpCleint connection pool
  initPoolManager();

  // Set teh AuthZ and configuration (scope.json) parameters for this
  // filter
  this.AUTH_PATH = config.getPropertyVal(AuthConstants.AUTH_CTX_PATH);
  this.SCOPE_PATH = config.getPropertyVal(AuthConstants.SCOPE_CTX_PATH);
  StringBuilder settings = new StringBuilder(
    "####The setting for the RestConnection in the AuthZ Filter are:");
  settings.append("AUTH_PATH = ");
  settings.append(this.AUTH_PATH + "; ");
  settings.append("SCOPE_PATH = ");
  settings.append(this.SCOPE_PATH + "; ");
  settings.append("HOST = ");
  settings.append(this.HOST + "; ");
  // LOG.debug(settings.toString());
 }

 /**
  * Set the constants used to configure the HttpClient connections, cache, and
  * scope validation
  */
 private void initSharedConstatnts()
 {

  this.PROTOCOL = config.getPropertyVal(AuthConstants.PROTOCOL);
  this.PORT = Integer
    .parseInt(config.getPropertyVal(AuthConstants.PORT).trim());
  this.TIME_OUT = Integer.parseInt(config
    .getPropertyVal(AuthConstants.TIME_OUT).trim());
  this.HOST = config.getPropertyVal(AuthConstants.HOST);
  // Set teh AuthZ and configuration (scope.json) parameters for this
  // filter
  this.AUTH_PATH = config.getPropertyVal(AuthConstants.AUTH_CTX_PATH);
  this.SCOPE_PATH = config.getPropertyVal(AuthConstants.SCOPE_CTX_PATH);

 }

 /**
  * Set the connection pool parameters for the HttpClient used to access the
  * AuthZ server
  */
 private void initPoolManager()
 {
  connManager.setMaxTotal(10);
  connManager.setDefaultMaxPerRoute(10);
  HttpHost host = new HttpHost(this.HOST, this.PORT);
  connManager.setMaxPerRoute(new HttpRoute(host), 10);
 }

 /**
  * Set teh Http Client caching params for the HttpClient used to access the
  * AuthZ server
  */
 @SuppressWarnings("deprecation")
 private void initCache()
 {
  defaultRequestConfig = RequestConfig.custom().setSocketTimeout(this.TIME_OUT)
    .setConnectTimeout(this.TIME_OUT)
    .setConnectionRequestTimeout(this.TIME_OUT)
    .setStaleConnectionCheckEnabled(true).build();

  cacheConfig = CacheConfig.custom().setMaxCacheEntries(1000)
    .setMaxObjectSize(8192).build();
 }

 /**
  * @return Singleton instance of REST connector
  */
 public static RestConnector getInstance()
 {
  if (restConnector == null)
  {
   restConnector = new RestConnector();
  }
  return restConnector;
 }

 /**
  * @param tokenList
  * @return http response valid
  * @throws IOException
  */
 public String callGoDotComValidateToken(Map<String, String> tokenList)
   throws IOException
 {
  // We need the response to validate scope authorizations
  // LOG.debug("####Token list in callGoDotComValidateToken() = " +
  // tokenList.toString());
  return callGoDotComGet(tokenList, this.AUTH_PATH);
 }

 /**
  * @param tokenList
  * @return token object
  * @throws IOException
  */
 public TokenDO callGoDotComValidateScope(Map<String, String> tokenList)
   throws IOException
 {
  // LOG.debug("####Token list in callGoDotComValidateScope() = " +
  // tokenList.toString());

  TokenDO tokenObj = callGoDotComPost(tokenList, this.SCOPE_PATH, TokenDO.class);
  return tokenObj;
 }

 /**
  * @param tokenList
  * @param ctxPath
  * @return http response
  * @throws IOException
  */
 public String callGoDotComGet(Map<String, String> tokenList, String ctxPath)
   throws IOException
 {
  String json = null;
  String accessToken = null;
  CloseableHttpResponse response = null;
  try
  {
   URIBuilder builder = new URIBuilder();
   if (ctxPath.equals(this.AUTH_PATH))
   {
    // LOG.debug(tokenList);
    // Some confusion between header value access_token and
    // authorization
    accessToken = tokenList.get(AuthConstants.AUTHORIZATION);
    if (accessToken != null && accessToken.indexOf(AuthConstants.BEARER) > -1)
    {
     // Authorization value: 'BEARER <access token>'
     accessToken = accessToken.substring(accessToken
       .indexOf(AuthConstants.BEARER) + 7);
     tokenList.put(AuthConstants.AUTHORIZATION, accessToken);
    }
    if (accessToken != null)
    {
     ctxPath += tokenList.get(AuthConstants.AUTHORIZATION);
    }
    /*
     * else { LOG.warn(AuthConstants.ACCESS_TOKEN + " is not set!"); }
     */
   }

   builder.setScheme(this.PROTOCOL).setHost(this.HOST).setPort(this.PORT)
     .setPath(ctxPath);

   HttpCacheContext context = HttpCacheContext.create();
   HttpGet getRequest = new HttpGet(builder.build());
   // HttpResponse response = httpClient.execute(getRequest);
   double start = System.nanoTime();
   response = httpClient.execute(getRequest, context);
   double end = System.nanoTime();
   double elapsed = end - start;

   LOG.debug("#### Time for calling the AUTHZ server is: "
     + Double.toString(elapsed / 1000000) + " milliseconds");

   int statusCode = response.getStatusLine().getStatusCode();
   StringBuilder authZcallMsg = new StringBuilder();

   if (statusCode == HttpServletResponse.SC_OK)
   {
    authZcallMsg.append("Validation SUCCESSFUL for token ");
    authZcallMsg.append(accessToken);
    json = EntityUtils.toString(response.getEntity());
   } else
   {
    authZcallMsg.append("Validation FAILED for token ");
    authZcallMsg.append(accessToken);
   }
   CacheResponseStatus responseStatus = context.getCacheResponseStatus();
   switch (responseStatus)
   {
   case CACHE_HIT:
    LOG.debug("Cache Hit: A response was generated from the cache with "
      + "no requests sent upstream");
    break;
   case CACHE_MODULE_RESPONSE:
    LOG.debug("Cache Response: The response was generated directly by the "
      + "caching module");
    break;
   case CACHE_MISS:
    LOG.debug("Cache Miss: The response came from an upstream server");
    break;
   case VALIDATED:
    LOG.debug("Cache Validated: The response was generated from the cache "
      + "after validating the entry with the origin server");
    break;
   default:
    LOG.warn("No response cache status: " + responseStatus);
    break;
   }
   authZcallMsg.append(" Response SC:" + statusCode + ", from (GET)"
     + getRequest.getURI().toString() + " response=" + json);
   // LOG.debug(authZcallMsg.toString());
  } 
  catch (URISyntaxException ex)
  {
   LOG.error("****Check if auth-config.properties has entry as per the standard****",ex);
  }
  catch (IOException ex)
  {
   LOG.error("****Generic IO exception has occured****",ex);
  }
  finally
  {
   HttpClientUtils.closeQuietly(response);
  }
  return json;
 }

 /**
  * @param tokenList
  * @param ctxPath
  * @param objectType
  * @return token response
  * @throws IOException
  */
 @SuppressWarnings("resource")
 public <T> T callGoDotComPost(Map<String, String> tokenList, String ctxPath,
   Class<T> objectType) throws IOException
 {
  HttpResponse response = null;
  HttpPost postRequest = new HttpPost();
  T tokenResp = null;
  try
  {
   URIBuilder builder = new URIBuilder();
   builder.setScheme(config.getPropertyVal(AuthConstants.PROTOCOL))
     .setHost(this.HOST).setPort(this.PORT).setPath(ctxPath);
   if (ctxPath.equals(this.SCOPE_PATH))
   {
    if (tokenList.get(AuthConstants.CLIENT_ID) != null)
    {
     builder.setParameter(AuthConstants.CLIENT_ID,
       tokenList.get(AuthConstants.CLIENT_ID));
    }
    if (tokenList.get(AuthConstants.ASSERTION_TYPE) != null)
    {
     builder.setParameter(AuthConstants.ASSERTION_TYPE,
       tokenList.get(AuthConstants.ASSERTION_TYPE));
    }
    if (tokenList.get(AuthConstants.GRANT_TYPE) != null)
    {
     builder.setParameter(AuthConstants.GRANT_TYPE,
       tokenList.get(AuthConstants.GRANT_TYPE));
    }
    if (tokenList.get(AuthConstants.USER_NAME) != null)
    {
     builder.setParameter(AuthConstants.USER_NAME,
       tokenList.get(AuthConstants.USER_NAME));
    }
    if (tokenList.get(AuthConstants.SCOPE) != null)
    {
     builder.setParameter(AuthConstants.SCOPE,
       tokenList.get(AuthConstants.SCOPE));
    }
    if (tokenList.get(AuthConstants.CLIENT_SECRET) != null)
    {
     builder.setParameter(AuthConstants.CLIENT_SECRET,
       tokenList.get(AuthConstants.CLIENT_SECRET));
    }
   }

   postRequest = new HttpPost(builder.build());

   response = httpClient.execute(postRequest);
   int statusCode = response.getStatusLine().getStatusCode();

   if (statusCode == HttpServletResponse.SC_OK)
   {
    InputStream iStream = response.getEntity().getContent();
    if (objectType != null)
    {
     // If return type is String, return the json response,
     // otherwise parse into tokens
     if (objectType.getName().equals("java.lang.String"))
     {
      tokenResp = (T) EntityUtils.toString(response.getEntity());
     } else
     {
      tokenResp = this.mapper.readValue(iStream, objectType);
     }
    }
    iStream.close();
   }
   LOG.info("Response SC:" + statusCode + ", from (POST) "
     + postRequest.getURI().toString());
  } catch (URISyntaxException | IOException ex)
  {
   LOG.error(ex);
  } finally
  {
   HttpClientUtils.closeQuietly(response);
  }
  return tokenResp;
 }
}
