#  <u>WDPR Auth Filter User's Guide</u>


## Table of Contents

* [WDPR Auth Filter Quick Start Guide](#quickstart)
- [How Auth Filter works](#how)
-  [Configuring the Filter](#config) 


## <a name="quickstart"></a>WDPR Auth Filter Quick Start Guide

Using the Authz Filters requires completing  these steps:

1. Add the Apache HttpClient dependency to your web project pom.xml file. This dependency should replace any existing Apache HTTP dependency; Be sure to test any existing HTTP clients in your web app for compatibility with this version.  
	
	       <dependency>
			<groupId>com.wdpr.ee</groupId>
			<artifactId>wdpr-authz</artifactId>
			<version>2.0.0</version>
			<exclusions>
                <!-- Old version 4.3 -->
			    <exclusion>
			        <artifactId>httpclient</artifactId>
			        <groupId>org.apache.httpcomponents</groupId>
			    </exclusion>
			</exclusions>
		</dependency> 


2. Add the WDPR logging and WDPR AuthZ filters to your project's web.xml file.

		<filter>
			<filter-name>HttpServletFilter</filter-name>
			<filter-class>com.wdpr.ee.loggingapi.filter.HttpLoggingFilter</filter-class>
		</filter>
		<filter-mapping>
			<filter-name>HttpServletFilter</filter-name>
			<url-pattern>/*</url-pattern>
		</filter-mapping>
		<filter>
			<filter-name>OAuthFilter</filter-name>
		<filter-class>com.wdpr.ee.authz.AuthFilter</filter-class>
		</filter>
		<filter-mapping>
			<filter-name>OAuthFilter</filter-name>
			<url-pattern>/*</url-pattern>
		</filter-mapping>

3. Add the following  files below to your web project class path. Example scope.json and auth-config.proprerty files can be retrieved from [WDPR AuthZ Filter git repo](https://github.disney.com/WDPR-RA-UI/Security-Filter/tree/shanghai/DemoApplication/src/main/resources). See section [Configuring the Filter ](#config)for an explanation of the expected entries for each file.

	* scope.json 
	* WdprLogAppender.properties
	* auth-config.properties
	
	
##<a name="how"></a>How Auth Filter works
	
	
The following diagram shows the flow of execution for the auth filter api:

![WDPR-Auth-Filter-Sequence-Diagram](./WDPR-Auth-Filter-Sequence-Diagram.pdf)

## <a name="config"></a>Configuring the Filter

This section details how to set the configuration of the filter, using the ***3 configuration files that must be found on the root class path in your war.***

### scope.json 

This file contains the scopes (authorizations)   required/allowed by the filter for any endpoints. The result  of a filter check that fails is to return a '401' without allowing  the request on to the endpoint. 

The following items are configurable:

* **urlPattern** - A scope is linked to an endpoint using all or part of the context portion of the  URL used to access the endpoint, .  This field contains a regex-compliant entry to match the  URL context to be protected.  An entry of ["*"] will allows any scope coming from the AuthZ server to pass, including an empty string ("") scope  (so only a token is required)***A ''no match' assumes there is no protection for the incoming endpoint context***
* **authToken** - a value of 'true' requires that the filter find a town (in the token header accept_token or "BEARER")
* **scopes** - An array of scopes for the related URL context
*  **scopesRequired** - One or more of the scope strings listed here must match exactly for the filter to allow the request to proceed.
*   **method** - The HTTP method (GET, POST, PUT, DELETE, PATCH) that the request must match for the scopes to apply. An "*" indicates the entry applies to all HTTP methods.

The following example indicates that any endpoint context that contains the string 'OAuthFilterTest' is required to one of the listed scopes ("read" and write") where the HTTP method is GET. Note that a context of any of the following would pass: OAuthFilterTest2, OAuthFilterTestMyFavorite, OAuthFilterTest, OAuthFilterTest2,anything else>.

		{
      		"authType": "pattern",
      		"urlPattern": "/OAuthFilterTest.*",
      		"id": 2,
      		"authToken":true,
      		"scopes": [
         	 {"method":"GET",
           	"scopesRequired": ["read"  "write"]
  		  }
      
This example indicates that any endpoint context that contains the strings 'OAuthFilterTest/photoOld' or  'OAuthFilterTest/photoOld' for any HTTP method, with any (or no) scopes defined, would pass. 

		{
      		"authType": "pattern",
      		"urlPattern": "/OAuthFilterTest/photos[Old|New]",
      		"id": 2,
      		"authToken":true,
      		"scopes": [
         	 {"method":"*",
           	"scopesRequired": [""]
    	 }
      
### auth-config.properties


This file provides the filter with the connection information for the relevant AuthZ server. The fields are as follows:

* **service.auth.protocol** - The protocol being used to reach the AuthZ server. It is almost always 
https' since it is good security to use an SSL tunnel when communicating with an AuthZ server, but it can be 'http'.
* **service.auth.host** - The host name or IP of the AuthZ server
* **service.auth.port** - The port of the AuthZ server
* **service.context.path** - The context the AuthZ server supports. There are usually only two, 
validate' and 'token', with validate the only one relevant to the filter check
* **service.auth.timeout** - the amount of time (in milliseconds) the filter will wait for a return from the AuthZ server 

The following example would connect to the AuthZ server at stg.authorization.go.com:443 with a timeout of 5 seconds:

		service.auth.protocol= https
		service.auth.host = stg.authorization.go.com
		service.auth.port =443
		service.auth.contextPath =/validate/
		service.scope.contextPath =/token
		service.auth.timeout =5000
		
### WdprLogAppender.properties

This file is for setting the logging entry properties that the filter will make (every request is logged with the time the request took as well whether the request was VALID or INVALID. ). Please see the [WDPR logging API site](https://github.disney.com/WDPR-RA/wdpr-loggingapi) for information on configuring this file / WDPR logging policy.

