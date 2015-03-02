package com.wdpr.ee.authz;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.wdpr.ee.authz.RestConnector;
import com.wdpr.ee.authz.util.AuthConfig;


public class SecurityCheckMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		AuthConfig config= AuthConfig.getInstance();
		RestConnector connector = RestConnector.getInstance();
		for (String key: config.getPropkeys() )
						
		System.out.println("key"+key+":"+ config.getPropertyMap().get(key));
		
		try{
			int i=0;
			while(true){
				Map<String, String> tockenList = new  HashMap<>();
				String token=generateSessionKey() ;
				tockenList.put("access_token",token);
				System.out.println(++i +" " +token);
				if(connector.callGoDotComValidateToken(tockenList)){
					System.out.println("Suthenticated");
				}
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	
	
	public static String generateSessionKey(){
		String alphabet = 
		        new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); //9
		int n = alphabet.length(); //10
		int length = "Un6LHVsOtTphTEANANz0UQ".length();
		String result = new String(); 
		Random r = new Random(); //11

		for (int i=0; i<length; i++) //12
		    result = result + alphabet.charAt(r.nextInt(n)); //13
//		System.out.println(result);
			return result;
		}

	
}
