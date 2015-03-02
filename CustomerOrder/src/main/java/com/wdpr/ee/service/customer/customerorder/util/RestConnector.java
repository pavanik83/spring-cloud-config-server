package com.wdpr.ee.service.customer.customerorder.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wdpr.ee.loggingapi.annotation.WdprMethodLog;
import com.wdpr.ee.service.customer.customerorder.model.CustomerOrder;
import com.wdpr.ee.service.customer.customerorder.model.ItemVO;

public class RestConnector {
	
	private static final Logger logging =  LogManager.getLogger(RestConnector.class);
	
	private static final int PORT = 8081;
	public static final String HOST = "localhost";
	public static final String PATH1 = "/pricing-services/pricing-service/item-price";
	
	//@WdprMethodLog
	public  static void priceItemNumber(CustomerOrder customerOrder, HashMap<String, String> hashMap) throws SQLException, ClassNotFoundException, IOException {

		
		logging.info(" ....................... enter priceItemNumber().................");
		HttpGet getRequest = null;
		CloseableHttpClient  httpClient =HttpClientBuilder.create().build();
		//HttpClient httpClient = new DefaultHttpClient();
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http")
				.setHost(HOST)
				.setPort(PORT)
				.setPath(PATH1)
				.setParameter("itemnumber",customerOrder.getItemNumber());
		
		 
		try {
			getRequest = new HttpGet(builder.build());
			
			
			CommonUtil.loadHeader(getRequest,hashMap);
			
			//sample : //http://localhost:8081/pricing-services/pricing-service/item-price?itemnumber=10
			logging.info(">>> uri" + getRequest.getURI());

			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));
			String output;
			StringBuffer sb= new StringBuffer();
			while ((output = br.readLine()) != null) {				
				sb.append(output);
				
			}
			
			logging.info(" SB---"  +sb.toString());
			
			ItemVO item = (ItemVO) JSONDeserializer.toObject(sb.toString(), ItemVO.class);
			
			if (item != null ){
				customerOrder.setItemPricePerUnit(item.getPricePerUnit());
				customerOrder.setItemName(item.getItemName());
				customerOrder.setItemDescription(item.getDescription());
			}
						
			httpClient.close();
			//httpClient.getConnectionManager().closeExpiredConnections();
			
		} catch (URISyntaxException ex) {
			logging.error(ex);
			throw new RuntimeException("Failed : HTTP error code : "
					+ ex);
			
		} catch (ClientProtocolException ex) {
			logging.error(ex);
			throw new RuntimeException("Failed : HTTP error code : "
					+ ex);



		} catch (IOException ex) {
			logging.error(ex);
			throw new RuntimeException("Failed : HTTP error code : "
					+ ex);

		}
		
		
	
		
		
		logging.info(" ....................... enter priceItemNumber().................");
	
		
	}

}
