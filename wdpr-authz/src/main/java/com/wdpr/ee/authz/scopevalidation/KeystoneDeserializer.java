/**
 *
 */
package com.wdpr.ee.authz.scopevalidation;

import java.io.IOException;
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
public class KeystoneDeserializer
{
    private static final Logger LOG = LogManager.getLogger(KeystoneDeserializer.class);

    /**
     * @param json
     * @return Array of allowed Roles
     */
    public List<String> abilities (String json)
    {
        List<String> abilities = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JsonOrgModule());
        JSONObject jso;
        try
        {
            jso = mapper.readValue(json, JSONObject.class);
        // Scope Roles - would have to parse out single String
        //String scope = (String)jso.get("scope");
        //LOG.info(scope);
        JSONObject kresp = (JSONObject)jso.get("keystone_response");
        //LOG.info("keystone_response=" + kresp);
        JSONObject ass = (JSONObject)kresp.get("GetKeystoneAssertionBySessionResult");
        //LOG.info("ass=" + ass);
        JSONArray roles = ass.getJSONArray("Roles");
        //LOG.info("roles=" + roles);
        JSONObject role = (JSONObject)roles.get(0);
        //LOG.info("role=" + role);
        //LOG.info("roleNames=" + JSONObject.getNames(role));
        JSONArray role0 = role.getJSONArray("Role");
        //LOG.info("role0=" + role0);
        // Anonymous array, can't get by name, need to use mapper parser
        JSONArray role01 = mapper.readValue(role0.toString(), JSONArray.class);
        //LOG.info("role01=" + role01.get(0));
        JSONObject fabs = (JSONObject)((JSONObject)role01.get(0)).get("FunctionalAbilities");
        //LOG.info("fabs=" + fabs);
        JSONArray fab = fabs.getJSONArray("FunctionalAbility");
        //LOG.info("fab=" + fab);
        for (int i=0; i< fab.length(); i++)
        {
            String name = (String)((JSONObject)fab.get(i)).get("Name");
            //LOG.info("name=" + name);
            abilities.add(name);
        }
        }
        catch (IOException | JSONException ex)
        {
            LOG.error(ex);
        }

        return abilities;
    }
}
