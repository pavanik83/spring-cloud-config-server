package com.wdpr.ee.authz.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Authorization scope
 */
public class ScopeRequired
{
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ScopeRequired [authorization=").append(this.authorization).append("]");
        return builder.toString();
    }

    /**
     * authorization
     */
    List<AuthDO> authorization = new ArrayList<>();

    /**
     * @return authorization
     */
    public List<AuthDO> getAuthorization()
    {
        return this.authorization;
    }

    /**
     * @param authorizationIn
     */
    public void setAuthorization(List<AuthDO> authorizationIn)
    {
        this.authorization = authorizationIn;
    }
}
