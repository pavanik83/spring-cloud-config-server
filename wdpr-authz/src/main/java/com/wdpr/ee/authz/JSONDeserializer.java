package com.wdpr.ee.authz;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/***************************************************************************************************
 * FileName - JSONDeserializer.java Desc: class used for JSON De-serialization
 * using jakson ObjectMapper. (c) Disney. All rights reserved.
 *
 * $Author: cognizant $nixon $Revision: $ $Change: $ $Date: $
 ********************************************************************************************************/

public class JSONDeserializer
{

    private static final Logger logger = LogManager.getLogger(JSONDeserializer.class);
    /**
     * @see org.codehaus.jackson.map.ObjectMapper
     */
    public static ObjectMapper mapper = new ObjectMapper();

    /**
     * Configure the mapper with deserialization features
     */
    JSONDeserializer()
    {
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationConfig.Feature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        mapper.configure(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING, true);
        mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_SETTERS, true);
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }

    /**
     * @param is
     * @param obj
     * @return Object from json
     */
    public static Object jsonToObject(String is, Object obj)
    {

        Object dataObj = null;
        try
        {
            dataObj = mapper.readValue(is, Object.class);

        }
        catch (JsonParseException ex)
        {
            logger.error(ex);
            // throw new ConfigurationException("The input is not valid JSON",
            // ex);
        }
        catch (JsonMappingException ex)
        {
            logger.error(ex);
            // throw new ConfigurationException("The input JSON doesn't match ",
            // ex);
        }
        catch (IOException ex)
        {
            logger.error(ex);
            // throw new
            // ConfigurationException("IO error while reading the JSON",ex);
        }

        return dataObj;
    }

    /**
     * Does not throw any exceptions, it only logs them
     * @param content
     * @param objectType
     * @return the Object
     */
    public static <T> T toObject(String content, Class<T> objectType)
    {
        T theObject = null;
        try
        {

            theObject = mapper.readValue(content, objectType);
        }
        catch (JsonParseException e)
        {
            logger.error(e);

            // throw
            // ApplicationExceptionFactory.create(HttpErrorType.INTERNAL_SERVER_ERROR,
            // ErrorTypeEnum.SYSTEM_ERROR,
            // ProductErrorCode.JSON_RESPONSE_PARSING_ERROR ,
            // "JsonParseException " );

        }
        catch (JsonMappingException e)
        {

            logger.error(e);
            // throw
            // ApplicationExceptionFactory.create(HttpErrorType.INTERNAL_SERVER_ERROR,
            // ErrorTypeEnum.SYSTEM_ERROR,
            // ProductErrorCode.JSON_RESPONSE_PARSING_ERROR ,
            // "JsonMappingException " );

        }
        catch (IOException e)
        {
            logger.error(e);
            // throw
            // ApplicationExceptionFactory.create(HttpErrorType.INTERNAL_SERVER_ERROR,
            // ErrorTypeEnum.SYSTEM_ERROR,
            // ProductErrorCode.JSON_RESPONSE_PARSING_ERROR , "IOException " );
        }

        if (theObject == null)
        {
            logger.error("Unable to load Object" + objectType.getCanonicalName());
            // throw
            // ApplicationExceptionFactory.create(HttpErrorType.INTERNAL_SERVER_ERROR,
            // ErrorTypeEnum.SYSTEM_ERROR,
            // ProductErrorCode.JSON_RESPONSE_PARSING_ERROR ,
            // "Unable to load Object " );
        }

        return theObject;
    }

}
