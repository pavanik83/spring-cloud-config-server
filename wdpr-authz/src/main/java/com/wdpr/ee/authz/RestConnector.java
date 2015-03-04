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
* FileName - RestConnector.java
* Desc: HttpClient class to invoke Rest service (auth.go.com) , Support Get/POST
* (c) Disney. All rights reserved.
*
* $Author:  $nixon
* $Revision:  $
* $Change:  $
* $Date: $
********************************************************************************************************/

public class RestConnector {

    private static final Logger logger =  LogManager.getLogger(RestConnector.class);

    public static RestConnector restConnector;
    public static AuthConfig config = AuthConfig.getInstance();

    String PROTOCOL,HOST,AUTH_PATH,SCOPE_PATH;
    int PORT,TIME_OUT;


    ObjectMapper mapper = new ObjectMapper();
    static RequestConfig defaultRequestConfig;

    private RestConnector() {
        defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(TIME_OUT).setConnectTimeout(TIME_OUT)
                .setConnectionRequestTimeout(TIME_OUT)
                .setStaleConnectionCheckEnabled(true).build();
        PROTOCOL = config.getPropertyVal(AuthConstants.PROTOCOL);
        PORT = Integer.parseInt(config.getPropertyVal(AuthConstants.PORT).trim());
        TIME_OUT = Integer.parseInt(config.getPropertyVal(AuthConstants.TIME_OUT).trim());
        HOST = config.getPropertyVal(AuthConstants.HOST);
        AUTH_PATH = config.getPropertyVal(AuthConstants.AUTH_CTX_PATH);
        SCOPE_PATH = config.getPropertyVal(AuthConstants.SCOPE_CTX_PATH);

    }

    public static RestConnector getInstance() {
        if (restConnector == null) {
            restConnector = new RestConnector();
        }
        return restConnector;
    }



    public   boolean callGoDotComValidateToken(Map<String, String> tokenList) throws  IOException {

        HttpResponse response = callGoDotComGet( tokenList, AUTH_PATH);

        int stausCode = response.getStatusLine().getStatusCode();

        if(stausCode == HttpServletResponse.SC_OK){
            return  true;
        }
        return false;

    }

    public  TokenDO callGoDotComValidateScope(Map<String, String> tokenList) throws  IOException {

        TokenDO tokenObj = (TokenDO) callGoDotComPost( tokenList, SCOPE_PATH, TokenDO.class);
        return tokenObj;

    }




    public   HttpResponse callGoDotComGet(Map<String, String> tokenList, String ctxPath) throws  IOException {

        HttpGet getRequest = new HttpGet();
        HttpResponse response = null;
        CloseableHttpClient  httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig) .build();
        try {

            URIBuilder builder = new URIBuilder();
            if (ctxPath == AUTH_PATH ){
                ctxPath +=tokenList.get(AuthConstants.ACCESS_TOKEN);

            }

            builder.setScheme(PROTOCOL)
                    .setHost(HOST)
                    .setPort(PORT)
                    .setPath(ctxPath);


            getRequest = new HttpGet(builder.build());

            response = httpClient.execute(getRequest);
            logger.info("Response SC:"+response.getStatusLine().getStatusCode()+ ", from (GET)"+getRequest.getURI().toString());
        } catch (URISyntaxException | IOException ex) {
            logger.error(ex);

        } finally{
            httpClient.close();
        }
        return response ;

    }

    public   <T> T  callGoDotComPost(Map<String, String> tokenList, String ctxPath, Class<T> objectType) throws  IOException {


        HttpPost postRequest = new HttpPost();
        CloseableHttpClient  httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig) .build();
        T tokenResp = null;
        try {

            URIBuilder builder = new URIBuilder();
            builder.setScheme(config.getPropertyVal(AuthConstants.PROTOCOL))
                    .setHost(HOST)
                    .setPort(PORT)
                    .setPath(ctxPath);

            if (ctxPath == SCOPE_PATH ){

                if (tokenList.get(AuthConstants.CLIENT_ID)!= null){
                    builder.setParameter(AuthConstants.CLIENT_ID, tokenList.get(AuthConstants.CLIENT_ID));
                }
                if (tokenList.get(AuthConstants.ASSERTION_TYPE)!= null){
                    builder.setParameter(AuthConstants.ASSERTION_TYPE, tokenList.get(AuthConstants.ASSERTION_TYPE));
                }
                if (tokenList.get(AuthConstants.GRANT_TYPE)!= null){
                    builder.setParameter(AuthConstants.GRANT_TYPE, tokenList.get(AuthConstants.GRANT_TYPE));
                }
                if (tokenList.get(AuthConstants.USER_NAME)!= null){
                    builder.setParameter(AuthConstants.USER_NAME, tokenList.get(AuthConstants.USER_NAME));
                }
            }

            postRequest = new HttpPost(builder.build());

            HttpResponse response = httpClient.execute(postRequest);
            int stausCode = response.getStatusLine().getStatusCode();

            if(stausCode == HttpServletResponse.SC_OK){
                InputStream iStream = response.getEntity().getContent();
                tokenResp =  mapper.readValue(iStream, objectType);

            }
            logger.info("Response SC:"+stausCode+ ", from (POST)"+postRequest.getURI().toString());

        } catch (URISyntaxException | IOException ex) {

            logger.error(ex);

        } finally{
            httpClient.close();
        }
        return tokenResp ;

    }

}
