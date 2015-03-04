package com.wdpr.ee.service.customer.customerorder.util;

import java.util.Date;
import java.util.Random;

public class StringUtil
{

    private static Random random = new Random((new Date()).getTime());

    /**
     * generates an alphanumeric string based on specified length.
     * 
     * @param length
     *            # of characters to generate
     * @return random string
     */
    public static String generateRandomString(int length)
    {
        char[] values =
        { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'A', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E' };
        String out = "";
        for (int i = 0; i < length; i++)
        {
            int idx = random.nextInt(values.length);
            out += values[idx];
        }
        return out;
    }

    public static String generateRandomANString(int length)
    {
        char[] values =
        { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'A', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9' };
        String out = "";
        for (int i = 0; i < length; i++)
        {
            int idx = random.nextInt(values.length);
            out += values[idx];
        }
        return out;
    }

    public static int generateRandomInt(int length)
    {
        char[] values =
        { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        String out = "";
        for (int i = 0; i < length; i++)
        {
            int idx = random.nextInt(values.length);
            out += values[idx];
        }

        return Integer.parseInt(out);
    }
}