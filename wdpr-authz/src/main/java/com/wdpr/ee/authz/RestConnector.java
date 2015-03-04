package com.wdpr.ee.authz;

import com.wdpr.ee.authz.model.TokenDO;
import com.wdpr.ee.authz.util.AuthConfig;
import com.wdpr.ee.authz.util.AuthConstants;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

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
     *
     */
    ObjectMapper mapper = new ObjectMapper();
    /**
     *
     */
    static RequestConfig defaultRequestConfig;

    private RestConnector()
    {
        defaultRequestConfig = RequestConfig.custom().setSocketTimeout(this.TIME_OUT)
                .setConnectTimeout(this.TIME_OUT).setConnectionRequestTimeout(this.TIME_OUT)
                .setStaleConnectionCheckEnabled(true).build();
        this.PROTOCOL = config.getPropertyVal(AuthConstants.PROTOCOL);
        this.PORT = Integer.parseInt(config.getPropertyVal(AuthConstants.PORT).trim());
        this.TIME_OUT = Integer.parseInt(config.getPropertyVal(AuthConstants.TIME_OUT).trim());
        this.HOST = config.getPropertyVal(AuthConstants.HOST);
        this.AUTH_PATH = config.getPropertyVal(AuthConstants.AUTH_CTX_PATH);
        this.SCOPE_PATH = config.getPropertyVal(AuthConstants.SCOPE_CTX_PATH);
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
     * @return  http response valid
     * @throws IOException
     */
    public boolean callGoDotComValidateToken(Map<String, String> tokenList) throws IOException
    {
        HttpResponse response = callGoDotComGet(tokenList, this.AUTH_PATH);
        int stausCode = response.getStatusLine().getStatusCode();
        if (stausCode == HttpServletResponse.SC_OK)
        {
            return true;
        }
        return false;
    }

    /**
     * @param tokenList
     * @return token object
     * @throws IOException
     */
    public TokenDO callGoDotComValidateScope(Map<String, String> tokenList) throws IOException
    {
        TokenDO tokenObj = callGoDotComPost(tokenList, this.SCOPE_PATH, TokenDO.class);
        return tokenObj;
    }

    /**
     * @param tokenList
     * @param ctxPath
     * @return http response
     * @throws IOException
     */
    public HttpResponse callGoDotComGet(Map<String, String> tokenList, String ctxPath)
            throws IOException
    {
        HttpGet getRequest = new HttpGet();
        HttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig).build();
        try
        {
            URIBuilder builder = new URIBuilder();
            if (ctxPath == this.AUTH_PATH)
            {
                ctxPath += tokenList.get(AuthConstants.ACCESS_TOKEN);
            }

            builder.setScheme(this.PROTOCOL).setHost(this.HOST).setPort(this.PORT).setPath(ctxPath);
            getRequest = new HttpGet(builder.build());
            response = httpClient.execute(getRequest);
            LOG.info("Response SC:" + response.getStatusLine().getStatusCode() + ", from (GET)"
                    + getRequest.getURI().toString());
        }
        catch (URISyntaxException | IOException ex)
        {
            LOG.error(ex);
        }
        finally
        {
            if (httpClient != null)
            {
                try
                {
                    httpClient.close();
                }
                catch (Exception ex)
                {
                    LOG.error(ex);
                }
            }
        }
        return response;
    }

    /**
     * @param tokenList
     * @param ctxPath
     * @param objectType
     * @return token response
     * @throws IOException
     */
    public <T> T callGoDotComPost(Map<String, String> tokenList, String ctxPath, Class<T> objectType)
            throws IOException
    {
        HttpPost postRequest = new HttpPost();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig).build();
        T tokenResp = null;
        try
        {
            URIBuilder builder = new URIBuilder();
            builder.setScheme(config.getPropertyVal(AuthConstants.PROTOCOL)).setHost(this.HOST)
                    .setPort(this.PORT).setPath(ctxPath);
            if (ctxPath == this.SCOPE_PATH)
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
            }

            postRequest = new HttpPost(builder.build());

            @SuppressWarnings("resource")
            HttpResponse response = httpClient.execute(postRequest);
            int stausCode = response.getStatusLine().getStatusCode();

            if (stausCode == HttpServletResponse.SC_OK)
            {
                InputStream iStream = response.getEntity().getContent();
                tokenResp = this.mapper.readValue(iStream, objectType);
                iStream.close();
            }
            LOG.info("Response SC:" + stausCode + ", from (POST)"
                    + postRequest.getURI().toString());
        }
        catch (URISyntaxException | IOException ex)
        {
            LOG.error(ex);
        }
        finally
        {
            if (httpClient != null)
            {
                try
                {
                    httpClient.close();
                }
                catch (Exception ex)
                {
                    LOG.error(ex);
                }
            }
        }
        return tokenResp;
    }
}
