package com.wdpr.ee.authz.model;

public class AuthDO {
	int id;
	String authType;
	String urlPattern;
	boolean authToken;
	String[] scopesRequired;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public boolean isAuthToken() {
		return authToken;
	}

	public boolean isAuthTokenRequired() {
		return authToken;
	}
	public void setAuthToken(boolean authToken) {
		this.authToken = authToken;
	}

	public String[] getScopesRequired() {
		return scopesRequired;
	}
	
	public String[] getScopesAllowed() {
		return scopesRequired;
	}

	public void setScopesRequired(String[] scopesRequired) {
		this.scopesRequired = scopesRequired;
	}
}