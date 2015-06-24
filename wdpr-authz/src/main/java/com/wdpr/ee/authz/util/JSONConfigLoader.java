package com.wdpr.ee.authz.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.wdpr.ee.authz.model.AuthDO;
import com.wdpr.ee.authz.model.AuthDO.Scope;
import com.wdpr.ee.authz.model.ScopeRequired;

/***************************************************************************************************
 * FileName - JSONConfigLoader.java Desc: used to Load JSON scope config
 * parameters (path-allowed scope, type, auth required or not) (c) Disney. All
 * rights reserved.
 *
 * $Author: $nixon $Revision: $ $Change: $ $Date: $
 ********************************************************************************************************/
public class JSONConfigLoader
{
    private static final Logger LOG = LogManager.getLogger(JSONConfigLoader.class);

    private static String scopeJsonFileName = "scope.json";
    private File jsonFile; // File containing JSON configuration
    Map<ScopeKeyClass, AuthDO> scopeMap = new HashMap<>();  // Map of scope key/value
    private ObjectMapper mapper = new ObjectMapper();  //  Maps json value to scope

    private static JSONConfigLoader instance;

    private JSONConfigLoader()
    {
        if (System.getProperty("AUTH_FILTER_SCOPE_FILE_NAME") != null) {
            scopeJsonFileName = System.getProperty("AUTH_FILTER_SCOPE_FILE_NAME");
        }

        this.jsonFile = new File(getClass().getClassLoader().getResource(scopeJsonFileName).getFile());

        initializeMap();
    }

    /**
     * @return singleton instance of JSON Config Loader
     */
    public static JSONConfigLoader getInstance()
    {
        if (instance == null)
        {
            instance = new JSONConfigLoader();
        }
        return instance;
    }

    private void initializeMap()
    {
        try
        {
            ScopeRequired scope = this.mapper.readValue(this.jsonFile, ScopeRequired.class);
            for (AuthDO patt : scope.getAuthorization())
            {
                String urlPattern   =   patt.getUrlPattern();
                ScopeKeyClass scopeKeyClass =   new ScopeKeyClass();
                scopeKeyClass.setUrl(urlPattern);
                Scope[] localScope   =   patt.getScopes();
                for (Scope scopes : localScope)
                {
                    scopeKeyClass.setMethod(scopes.getMethod());
                }
                if (patt != null)
                {
                    this.scopeMap.put(scopeKeyClass, patt);
                }
            }
        }
        catch (JsonGenerationException ex)
        {
            LOG.error("****Check the scope.json****", ex);
        }
        catch (JsonMappingException ex)
        {
            LOG.error("****Check the scope.json for the mapping element****", ex);
        }
        catch (IOException ex)
        {
            LOG.error("****Check the scope.json if that is properly written****", ex);
        }
    }
    /**
     * @return scopeMap
     */
    public Map<ScopeKeyClass, AuthDO> loadScopeData()
    {
        // File jsonFile = new File(jsonFilePath);
        return this.scopeMap;
    }
}
