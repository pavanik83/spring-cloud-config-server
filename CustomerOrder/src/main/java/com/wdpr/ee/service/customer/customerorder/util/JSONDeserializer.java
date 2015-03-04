package com.wdpr.ee.service.customer.customerorder.util;

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
 * FileName - JSONDeserializer.java Desc: class used for VA JSON Deserialization
 * using jakson ObjectMapper. (c) Disney. All rights reserved.
 *
 * $Author: cognizant $nixon $Revision: $ $Change: $ $Date: $
 ********************************************************************************************************/

public class JSONDeserializer
{

    private static final Logger LOGGER = LogManager.getLogger(JSONDeserializer.class);
    public static ObjectMapper mapper = new ObjectMapper();

    JSONDeserializer()
    {
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationConfig.Feature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        mapper.configure(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING, true);
        mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_SETTERS, true);
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }

    public static Object jsonToObject(String is, Object obj)
    {

        Object dataObj = null;
        try
        {
            LOGGER.info("enter jsonToObject ");
            dataObj = mapper.readValue(is, Object.class);

        }
        catch (JsonParseException ex)
        {
            LOGGER.error(ex);
            // throw new ConfigurationException("The input is not valid JSON",
            // ex);
        }
        catch (JsonMappingException ex)
        {
            LOGGER.error(ex);
            // throw new ConfigurationException("The input JSON doesn't match ",
            // ex);
        }
        catch (IOException ex)
        {
            LOGGER.error(ex);
            // throw new
            // ConfigurationException("IO error while reading the JSON",ex);
        }

        LOGGER.info("exit jsonToObject ");
        return dataObj;
    }

    public static <T> T toObject(String content, Class<T> objectType)
    {
        LOGGER.info("enter toObject ");
        T theObject = null;
        try
        {

            theObject = mapper.readValue(content, objectType);
        }
        catch (JsonParseException e)
        {
            LOGGER.error(e);

            // throw
            // ApplicationExceptionFactory.create(HttpErrorType.INTERNAL_SERVER_ERROR,
            // ErrorTypeEnum.SYSTEM_ERROR,
            // ProductErrorCode.JSON_RESPONSE_PARSING_ERROR ,
            // "JsonParseException " );

        }
        catch (JsonMappingException e)
        {

            LOGGER.error(e);
            // throw
            // ApplicationExceptionFactory.create(HttpErrorType.INTERNAL_SERVER_ERROR,
            // ErrorTypeEnum.SYSTEM_ERROR,
            // ProductErrorCode.JSON_RESPONSE_PARSING_ERROR ,
            // "JsonMappingException " );

        }
        catch (IOException e)
        {
            LOGGER.error(e);
            // throw
            // ApplicationExceptionFactory.create(HttpErrorType.INTERNAL_SERVER_ERROR,
            // ErrorTypeEnum.SYSTEM_ERROR,
            // ProductErrorCode.JSON_RESPONSE_PARSING_ERROR , "IOException " );
        }

        if (theObject == null)
        {
            LOGGER.error("Unable to load Object" + objectType.getCanonicalName());
            // throw
            // ApplicationExceptionFactory.create(HttpErrorType.INTERNAL_SERVER_ERROR,
            // ErrorTypeEnum.SYSTEM_ERROR,
            // ProductErrorCode.JSON_RESPONSE_PARSING_ERROR ,
            // "Unable to load Object " );
        }
        LOGGER.info("exit toObject ");
        return theObject;
    }
    //
    // public static String toJSONString(ProductsCollectionResource res) {
    // String jsonString = null;
    // try {
    //
    // jsonString = mapper.writeValueAsString(res);
    // } catch (JsonParseException e) {
    // LOGGER.error(e);
    //
    // throw
    // ApplicationExceptionFactory.create(HttpErrorType.INTERNAL_SERVER_ERROR,
    // ErrorTypeEnum.SYSTEM_ERROR,
    // ProductErrorCode.JSON_RESPONSE_PARSING_ERROR ,
    // "toJSONString() JsonParseException " );
    //
    // } catch (IOException e) {
    // LOGGER.error(e.getLocalizedMessage(), e.fillInStackTrace());
    // }
    //
    // if (jsonString == null) {
    // throw ApplicationExceptionFactory.create(HttpErrorType.UNKNOWN,
    // ErrorTypeEnum.INVALID_FORMAT,
    // ProductErrorCode.JSON_RESPONSE_PARSING_ERROR,
    // "Serialization Issue");
    // }
    // return jsonString;
    // }
    //
    //
    // public static String toJSONString(ProductsResourceCollectionVO
    // resCollection) {
    // String jsonString = null;
    // try {
    // long time = System.currentTimeMillis();//TODO: remove
    // jsonString = mapper.writeValueAsString(resCollection);
    // LOGGER.info("Time toJSONString ()"+(System.currentTimeMillis()-time));//TODO:
    // remove
    // } catch (IOException e) {
    // LOGGER.error(e.getLocalizedMessage(), e.fillInStackTrace());
    // }
    //
    // if (jsonString == null) {
    // throw
    // ApplicationExceptionFactory.create(HttpErrorType.INTERNAL_SERVER_ERROR,
    // ErrorTypeEnum.INVALID_FORMAT,
    // ProductErrorCode.JSON_RESPONSE_PARSING_ERROR,
    // "Serialization Issue");
    // }
    // return jsonString;
    // }
}
