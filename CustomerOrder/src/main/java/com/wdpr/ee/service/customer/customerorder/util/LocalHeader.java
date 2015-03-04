package com.wdpr.ee.service.customer.customerorder.util;

import java.util.HashMap;

public class LocalHeader
{

    public static final ThreadLocal<HashMap<String, String>> userThreadLocal = new ThreadLocal<HashMap<String, String>>()
    {
        protected HashMap<String, String> initialValue()
        {
            return new HashMap<String, String>();
        }
    };

    public static void put(String tag, String value)
    {
        userThreadLocal.get().put(tag, value);
    }

    public static void unset()
    {
        userThreadLocal.remove();
    }

    public static void get(String tag)
    {
        userThreadLocal.get().get(tag);

    }

}
