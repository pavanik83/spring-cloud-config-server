package com.wdpr.ee.service.customer.customerorder.exception;

import java.io.Serializable;

public class ErrorResponse implements Serializable
{

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

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getErrorType()
    {
        return errorType;
    }

    public void setErrorType(String errorType)
    {
        this.errorType = errorType;
    }

    public String getErrorDescription()
    {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription)
    {
        this.errorDescription = errorDescription;
    }
}
