package com.wdpr.ee.service.customer.customerorder.util;

import java.util.HashMap;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class CommonUtil
{

    private static final Logger logger = LogManager.getLogger(CommonUtil.class);

    public static void loadHeader(HttpGet getRequest, HashMap<String, String> hashMap)
    {
        for (String keys : hashMap.keySet())
        {
            logger.info("Received: key " + keys + ":" + hashMap.get(keys).toString());

            getRequest.setHeader(keys, hashMap.get(keys));

        }

        logger.info("CorrelationId in TC:  " + ThreadContext.get("Correlation-Id"));

        // Nixon: ATTN

        // Below code should be uncommneted when no aspect X-CorrelationId is
        // available

        // if (hashMap.get("X-CorrelationId")== null){
        //
        // logger.info("CorrelationId in TC (ma- null):  "+ThreadContext.get("Correlation-Id"));
        //
        HttpGet httpget
        // httpget.setHeader("X-CorrelationId",
        // ThreadContext.get("Correlation-Id"));
        //
        //
        //
        // }

        // Aspect check
        ;

        Header[] headers = getRequest.getAllHeaders();
        logger.info("AllHeaders>>.............................");
        for (Header header : headers)
        {
            logger.info("AllHeaders>>" + header.getName() + ":" + header.getValue());

        }
        logger.info("headers x-corr_id........................");
        Header[] crr_ids = getRequest.getHeaders("X-CorrelationId");
        if (crr_ids != null)
        {
            for (Header crr_id : crr_ids)
            {
                logger.info("crr_id>>" + crr_id.getName() + ":" + crr_id.getValue());

            }
            logger.info(" header size -" + crr_ids.length);
            logger.info(" Aspect -X-CorrelationId "
                    + getRequest.getHeaders("X-CorrelationId").toString());
        }

        if (crr_ids != null)
        {
            logger.info(" header size -" + crr_ids.length);
            logger.info(" Aspect -X-CorrelationId "
                    + getRequest.getHeaders("X-CorrelationId").toString());

        }
        else
        {
            logger.error(" Aspect -X-CorrelationId null");
        }

    }

}
