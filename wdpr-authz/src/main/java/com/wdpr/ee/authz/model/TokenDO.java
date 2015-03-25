package com.wdpr.ee.authz.model;

/**
 * Authorization token object
 */
public class TokenDO
{
    /**
     * access_token
     */
    String access_token;
    /**
     * token_type
     */
    String token_type;
    /**
     * scope
     */
    String scope;
    /**
     * expires_in
     */
    String expires_in;

    /**
     * @return access_token
     */
    public String getAccess_token()
    {
        return this.access_token;
    }

    /**
     * @param access_tokenParam
     */
    public void setAccess_token(String access_tokenParam)
    {
        this.access_token = access_tokenParam;
    }

    /**
     * @return Token Type
     */
    public String getToken_type()
    {
        return this.token_type;
    }

    /**
     * @param token_typeParam
     */
    public void setToken_type(String token_typeParam)
    {
        this.token_type = token_typeParam;
    }

    /**
     * @return scope
     */
    public String getScope()
    {
        return this.scope;
    }

    /**
     * @param scopeParam
     */
    public void setScope(String scopeParam)
    {
        this.scope = scopeParam;
    }

    /**
     * @return expires in
     */
    public String getExpires_in()
    {
        return this.expires_in;
    }

    /**
     * @param expires_inParam
     */
    public void setExpires_in(String expires_inParam)
    {
        this.expires_in = expires_inParam;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("TokenDO [access_token=").append(this.access_token).append(", token_type=")
                .append(this.token_type).append(", scope=").append(this.scope)
                .append(", expires_in=").append(this.expires_in).append("]");
        return builder.toString();
    }
}
