package com.wdpr.ee.authz.model;

/**
 * Authorization Object with configuration
 */
public class AuthDO
{
    /**
     *
     */
    int id;
    /**
     *
     */
    String authType;
    /**
     *
     */
    String urlPattern;
    /**
     *
     */
    boolean authToken;
    /**
     *
     */
    String[] scopesRequired;

    /**
     * @return id
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * @param idParam
     */
    public void setId(int idParam)
    {
        this.id = idParam;
    }

    /**
     * @return authType
     */
    public String getAuthType()
    {
        return this.authType;
    }

    /**
     * @param authTypeParam
     */
    public void setAuthType(String authTypeParam)
    {
        this.authType = authTypeParam;
    }

    /**
     * @return urlPattern
     */
    public String getUrlPattern()
    {
        return this.urlPattern;
    }

    /**
     * @param urlPatternParam
     */
    public void setUrlPattern(String urlPatternParam)
    {
        this.urlPattern = urlPatternParam;
    }

    /**
     * @return authToken
     */
    public boolean isAuthToken()
    {
        return this.authToken;
    }

    /**
     * @return authToken
     */
    public boolean isAuthTokenRequired()
    {
        return this.authToken;
    }

    /**
     * @param authTokenParam
     */
    public void setAuthToken(boolean authTokenParam)
    {
        this.authToken = authTokenParam;
    }

    /**
     * @return scopesRequired
     */
    public String[] getScopesRequired()
    {
        return this.scopesRequired;
    }

    /**
     * @return scopesRequired
     */
    public String[] getScopesAllowed()
    {
        return this.scopesRequired;
    }

    /**
     * @param scopesRequiredParam
     */
    public void setScopesRequired(String[] scopesRequiredParam)
    {
        this.scopesRequired = scopesRequiredParam;
    }
}
