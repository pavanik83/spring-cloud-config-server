/**
 *
 */
package com.wdpr.ee.authz.scopevalidation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;

/**
 * Returns Roles allowed, after token validation, from Keystone response
 */
public class ClientIdValidator {
	private static final Logger LOG = LogManager
			.getLogger(ClientIdValidator.class);

	/**
	 * @param json
	 * @return Array of allowed Roles
	 */
	public List<String> validateScopes(String json) {
		List<String> abilities = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JsonOrgModule());
		JSONObject jso;
		try 
		{
			//*******Decoding the json attribute so as to remove the encoded character, this is mainly required for KeyStone Authorization**/
			json = decodeJson(json);
			jso = mapper.readValue(json, JSONObject.class);

			Object  scopeObj =   jso.get("scope");
			if (scopeObj instanceof JSONArray) {
				JSONArray scopes = (JSONArray) scopeObj;
				 for (int i=0; i< scopes.length(); i++)
			        {
			            String name = scopes.get(i).toString();
			            abilities.add(name);
			        }
			} else if (scopeObj instanceof String)
			{
				 abilities.add(scopeObj.toString());
			}
			//LOG.info("Configured scopes = " + abilities);			
			
		} catch (IOException | JSONException ex) {
			//LOG.error(ex);
		}

		return abilities;
	}
    /**
     * 
     * @param json
     * @return
     * @throws UnsupportedEncodingException
     */
	private String decodeJson(String json) throws UnsupportedEncodingException 
	{
		json	=	URLDecoder.decode(json, "UTF-8");
		return json;
	}
}
