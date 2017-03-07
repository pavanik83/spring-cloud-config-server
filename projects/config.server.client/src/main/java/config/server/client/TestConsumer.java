package config.server.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestConsumer {
	
	@Value("${tollstart}")
	private String tollstart;
	
	@Value("${tollstop}")
	private String tollend;
	
	@Value("${lanecount}")
	private int lanecount;
	
	@Value("${rate}")
	private float rate;
	
	@Value("${datasource}")
	private String datasource;
	
	@RequestMapping(value="/propertiesVal", method=RequestMethod.GET)
	public String getPropertiesValues(){
		return "toolstart : "+tollstart + " tollend : "+ tollend+ " lanecount : "
					+lanecount + " rate : "+rate +" \n encypted data of datasource : "
					+ datasource;
	}

}
