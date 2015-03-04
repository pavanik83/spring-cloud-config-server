package com.wdpr.ee.authz.util;

import com.wdpr.ee.authz.model.AuthDO;
import com.wdpr.ee.authz.model.Scope;
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
* FileName - JSONConfigLoader.java
* Desc: used to Load JSON scope config  parameters (path-allowed scope, type, auth required or not)
* (c) Disney. All rights reserved.
*
* $Author:  $nixon
* $Revision:  $
* $Change:  $
* $Date: $
********************************************************************************************************/
public class JSONConfigLoader {
    private static final Logger logger = LogManager.getLogger(JSONConfigLoader.class);
    private final String JSON_FILE_NAME = "scope.json";
    File jsonFile ;
    Map<String, AuthDO> scopeMap = new HashMap<>();
    ObjectMapper mapper = new ObjectMapper();

    private static JSONConfigLoader instance ;

    private JSONConfigLoader() {
        jsonFile = new File(getClass().getClassLoader().getResource(JSON_FILE_NAME).getFile());
        initializeMap();
    }


    public static JSONConfigLoader getInstance() {
        if (instance == null) {
            instance = new JSONConfigLoader();

        }
        return instance;
    }

    private void initializeMap() {

        try {
            Scope scope = mapper.readValue(this.jsonFile, Scope.class);

            for (AuthDO patt : scope.getAuthorization()) {
                if (patt != null) {
                    scopeMap.put(patt.getUrlPattern(), patt);
                }

            }

        } catch (JsonGenerationException ex) {
            logger.error(ex);
        } catch (JsonMappingException ex) {
            logger.error(ex);
        } catch (IOException ex) {
            logger.error(ex);
        }

    }

    public Map<String, AuthDO> loadScopeData() {

        // File jsonFile = new File(jsonFilePath);
        return scopeMap;

    }

}
