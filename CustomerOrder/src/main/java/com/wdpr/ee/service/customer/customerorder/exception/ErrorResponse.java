package com.wdpr.ee.service.customer.customerorder.exception;

import java.io.Serializable;

/**
 *
 */
public class ErrorResponse implements Serializable
{

    /**
     * @param statusCode
     * @param errorType
     * @param errorDescription
     */
    public ErrorResponse(int statusCode, String errorType, String errorDescription)
    {
        super();
        this.statusCode = statusCode;
        this.errorType = errorType;
        this.errorDescription = errorDescription;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int statusCode;
    private String errorType;
    private String errorDescription;

    /**
     * @return statusCode
     */
    public int getStatusCode()
    {
        return this.statusCode;
    }

    /**
     * @param statusCode
     */
    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    /**
     * @return errorType
     */
    public String getErrorType()
    {
        return this.errorType;
    }

    /**
     * @param errorType
     */
    public void setErrorType(String errorType)
    {
        this.errorType = errorType;
    }

    /**
     * @return errorDescription
     */
    public String getErrorDescription()
    {
        return this.errorDescription;
    }

    /**
     * @param errorDescription
     */
    public void setErrorDescription(String errorDescription)
    {
        this.errorDescription = errorDescription;
    }
}
