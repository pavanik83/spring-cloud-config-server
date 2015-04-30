/**
 *
 */
package com.wdpr.ee.authz.scopevalidation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
		try {
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
			LOG.info("Configured scopes = " + abilities);			
			
		} catch (IOException | JSONException ex) {
			LOG.error(ex);
		}

		return abilities;
	}
}
