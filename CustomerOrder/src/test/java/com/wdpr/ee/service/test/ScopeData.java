package com.wdpr.ee.service.test;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ScopeData
{
    /** */
    List<Pattern> authorization = new ArrayList<>();

    /**
     * @return authorization
     */
    public List<Pattern> getAuthorization()
    {
        return this.authorization;
    }

    /**
     * @param authorization
     */
    public void setAuthorization(List<Pattern> authorization)
    {
        this.authorization = authorization;
    }
}

/**
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
class Pattern
{
    /** */
    String type;
    /** */
    String url_pattern;
    /** */
    int id;
    /** */
    String[] required_scopes;
    /** */
    boolean token_auth;

    /**
     * @return token_auth
     */
    public boolean isToken_auth()
    {
        return this.token_auth;
    }

    /**
     * @param token_auth
     */
    public void setToken_auth(boolean token_auth)
    {
        this.token_auth = token_auth;
    }

    /**
     * @return type
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * @param type
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * @return url_pattern
     */
    public String getUrl_pattern()
    {
        return this.url_pattern;
    }

    /**
     * @param url_pattern
     */
    public void setUrl_pattern(String url_pattern)
    {
        this.url_pattern = url_pattern;
    }

    /**
     * @return id
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * @param id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return required_scopes
     */
    public String[] getRequired_scopes()
    {
        return this.required_scopes;
    }

    /**
     * @param required_scopes
     */
    public void setRequired_scopes(String[] required_scopes)
    {
        this.required_scopes = required_scopes;
    }
}
