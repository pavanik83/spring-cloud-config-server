package com.wdpr.ee.authz.model;

import java.util.Arrays;

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
    Scope[] scopes;

    /**
     * @return the scopesRequired
     */
    public Scope[] getScopes()
    {
        return this.scopes;
    }

    /**
     * @param scopes the scopesRequired to set
     */
    public void setScopesRequired(Scope[] scopes)
    {
        this.scopes = scopes;
    }

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
     * Static class containing method, scopesRequired, scopesAllowed
     *
     */
    public static class Scope
    {
        /**
         * @see Object#toString()
         */
        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append("Scope [method=").append(this.method).append(", scopesRequired=")
                    .append(Arrays.toString(this.scopesRequired)).append("]");
            return builder.toString();
        }

        /**
         * Method(s) to be secured. Use * for all
         */
        String method;
        /**
         * Scopes required for access to URL/method
         */
        String[] scopesRequired;

        /**
         * @return the method
         */
        public String getMethod()
        {
            return this.method;
        }

        /**
         * @param method the method to set
         */
        public void setMethod(String method)
        {
            this.method = method;
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

    /**
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("AuthDO [id=").append(this.id).append(", authType=").append(this.authType)
                .append(", urlPattern=").append(this.urlPattern).append(", authToken=")
                .append(this.authToken).append(", scopes=").append(Arrays.toString(this.scopes))
                .append("]");
        return builder.toString();
    }
}
