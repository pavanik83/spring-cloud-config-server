package com.wdpr.ee.authz.util;

import com.wdpr.ee.authz.model.AuthDO;
import com.wdpr.ee.authz.model.ScopeRequired;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

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
    private final String JSON_FILE_NAME = "scope.json";
    /**
     * File containing JSON configuration
     */
    File jsonFile;
    /**
     * Map of scope key/value
     */
    Map<String, AuthDO> scopeMap = new HashMap<>();
    /**
     * Maps json value to scope
     */
    ObjectMapper mapper = new ObjectMapper();

    private static JSONConfigLoader instance;

    private JSONConfigLoader()
    {
        this.jsonFile = new File(getClass().getClassLoader().getResource(this.JSON_FILE_NAME).getFile());
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
                if (patt != null)
                {
                    this.scopeMap.put(patt.getUrlPattern(), patt);
                }
            }
        }
        catch (JsonGenerationException ex)
        {
            LOG.error(ex);
        }
        catch (JsonMappingException ex)
        {
            LOG.error(ex);
        }
        catch (IOException ex)
        {
            LOG.error(ex);
        }
    }

    /**
     * @return scopeMap
     */
    public Map<String, AuthDO> loadScopeData()
    {
        // File jsonFile = new File(jsonFilePath);
        return this.scopeMap;
    }
}
