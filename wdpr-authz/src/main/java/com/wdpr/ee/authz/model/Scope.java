package com.wdpr.ee.authz.model;

import java.util.ArrayList;
import java.util.List;

public class Scope {

    List<AuthDO> authorization = new ArrayList<>();

    public List<AuthDO> getAuthorization() {
        return authorization;
    }

    public void setAuthorization(List<AuthDO> authorization) {
        this.authorization = authorization;
    }



}
