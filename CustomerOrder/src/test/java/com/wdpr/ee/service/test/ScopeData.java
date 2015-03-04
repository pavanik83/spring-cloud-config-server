package com.wdpr.ee.service.test;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ScopeData {


    List<Pattern> authorization = new ArrayList<>();

    public List<Pattern> getAuthorization() {
        return authorization;
    }

    public void setAuthorization(List<Pattern> authorization) {
        this.authorization = authorization;
    }




}
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
class Pattern{
    String type;
    String url_pattern;
    int id ;
    String[] required_scopes;
    boolean token_auth;

    public boolean isToken_auth() {
        return token_auth;
    }
    public void setToken_auth(boolean token_auth) {
        this.token_auth = token_auth;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUrl_pattern() {
        return url_pattern;
    }
    public void setUrl_pattern(String url_pattern) {
        this.url_pattern = url_pattern;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String[] getRequired_scopes() {
        return required_scopes;
    }
    public void setRequired_scopes(String[] required_scopes) {
        this.required_scopes = required_scopes;
    }

}
