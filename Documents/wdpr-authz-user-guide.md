#  <u>WDPR Auth Filter User's Guide</u>


## Table of Contents

* [WDPR Auth Filter Quick Start Guide](#quickstart)
- [How Auth Filter works](#how)
-  [Configuring the Filter](#config) 
-  [Running the Demo Application Using Auth Filter](#demo)


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


	* **scope.json**
	* **WdprLogAppender.properties**
	* **auth-config.properties**
	

##<a name="how"></a>How Auth Filter works
	
	
The following diagram shows the flow of execution for the auth filter api:

![WDPR-Auth-Filter-Sequence-Diagram](./WDPR-Auth-Filter-Sequence-Diagram.png)

## <a name="config"></a>Configuring the Filter

This section details how to set the configuration of the filter, using the ***3 configuration files that must be found on the root class path in your war.***

### scope.json 

This file contains the scopes (authorizations)   required/allowed by the filter for any endpoints. The result  of a filter check that fails is to return a '401' without allowing  the request on to the endpoint. 

The following items are configurable:

* **urlPattern** - A scope is linked to an endpoint using all or part of the context portion of the  URL used to access the endpoint, .  This field contains a regex-compliant entry to match the  URI to be protected.  An entry of ["*"] will allows any scope coming from the AuthZ server to pass, including an empty string ("") scope  (so only a token is required)***A ''no match' assumes there is no protection for the incoming endpoint context***
* **authToken** - a value of `true` indicate that proper authentication is required if set to `false` authentication is not mandatory and the auth filter will chain the request to next page. 
* **scopes** - An array of scopes for the related URL context. Note that scopes are generally text strings that represent a role or authorization level. Scopes in the scopes.json file must match exactly to at least one scope associated with the incoming AuthZ token, else a request will be terminated with a 401. For example, a scope based on a Keystone role (the user authenticated with Keystone before getting the AuthZ token) will contain the role AND an authorization, separated by a colon, e.g., "DRC Agent : cancelTicketsUI".
*  **scopesRequired** - One or more of the scope strings listed here must match exactly for the filter to allow the request to proceed.
*   **method** - The HTTP method (GET, POST, PUT, DELETE, PATCH) that the request must match for the scopes to apply. An "*" indicates the entry applies to all HTTP methods, if both * and HTTP method are used  * will take the precedence.

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



##<a name="demo"></a> Running the Demo Application Using Auth Filter

#### Rest Service example invocation: 

	http://]localhost:8080/DemoApplication/CallableServlet
	
####Steps For Running The Demo:

	1. Download the Demo Application project and build  the war file from the git repo(https://github.disney.com/WDPR-RA-UI/Security-Filter/tree/shanghai/DemoApplication).
	2. Change the scope.json file to contain at least one scope associated with the requesting client.
	3. Change the auth-config.properties entries to point to the AuthZ server you wish the filter to use.
	4. Build the Demo Application war file and deploy it to a Tomcat server
	5. Open DHC  (Developer Tool Extension from Chrome)  in Google Chrome and do the following:
	6. Obtain a fresh token from the AuthZ server (this can be done by calling the AuthZ server using curl or DHC with the url  https://<stg.authorization.go.com>your authz host ip or DNS>:443/token?grant_type=client_credentials&client_id=<some authz client id>&client_secret=<some AuthZ secret>)
	7. Add the URL localhost:8080/DemoApplication/ in Request text in DHC and chose the method as GET.
	8. Add the Headers "access_token" and value as the new valid Token value (from a call to your AuthZ server),Hit the Send button.
	9. In case of valid entry the response status will be 200 and you will get a return value from the Demo Application  of "You have reached the Callable Service Test endpoint! @ Fri May 15 15:23:01 EDT 2015". If the token is expired or a required scope (as configured in scopes. json )  is not found  in the passed-token, the response status will be 401 with no returned body..

#### Sample logs from Client:


	WW-AM04020514 Wdpr.ee.loggingapi.appender.SystemProperties Java version 1.7.0_65 - processors: 8, architecture: x86_64-64 - Mac OS X 10.8.3 unknown, architecture: x86_64-64
	WW-AM04020514 2015-05-14 15:22:59,872 [localhost-startStop-1] ['App-Name':DemoAuthZApp 'Correlation-Id':6c2e8ae7-0ff6-4a77-8d87-fb8880ce62ef 'Session-Id': 'Thread-Group':main 'Thread-Id':17 'Version':"1.0"  'X-Message-Id': 'X-User-Id':] DEBUG com.wdpr.ee.authz.AuthFilter - #### Loaded required scope /DemoApplication.*
	WW-AM04020514 2015-05-14 15:22:59,881 [localhost-startStop-1] ['App-Name':DemoAuthZApp 'Correlation-Id':6c2e8ae7-0ff6-4a77-8d87-fb8880ce62ef 'Session-Id': 'Thread-Group':main 'Thread-Id':17 'Version':"1.0"  'X-Message-Id': 'X-User-Id':] DEBUG com.wdpr.ee.authz.AuthFilter - #### Loaded required scope /DemoApplication.*
	WW-AM04020514 2015-05-14 15:23:17,651 [tomcat-http--3] ['App-Name':DemoAuthZApp 'Correlation-Id':c1ae741e-639e-4b54-a51c-900a190e6958 'Session-Id':D1F36E72A2A4E828F59AF215FCA06C2A 'Thread-Group':main 'Thread-Id':25 'Version':"1.0"  'X-Message-Id': 'X-User-Id':] DEBUG com.wdpr.ee.authz.AuthFilter - #### Matched the incoming request context /DemoApplication with the configured scope context /DemoApplication.*
	WW-AM04020514 2015-05-14 15:23:17,651 [tomcat-http--3] ['App-Name':DemoAuthZApp 'Correlation-Id':c1ae741e-639e-4b54-a51c-900a190e6958 'Session-Id':D1F36E72A2A4E828F59AF215FCA06C2A 'Thread-Group':main 'Thread-Id':25 'Version':"1.0"  'X-Message-Id': 'X-User-Id':] DEBUG com.wdpr.ee.authz.AuthFilter - #### scopeItem =  AuthDO [id=2, authType=pattern, urlPattern=/DemoApplication.*, authToken=true, scopes=[Scope [method=GET, scopesRequired=[wdpro-payment-general-crud]]]] context = /DemoApplication
	WW-AM04020514 2015-05-14 15:23:17,652 [tomcat-http--3] ['App-Name':DemoAuthZApp 'Correlation-Id':c1ae741e-639e-4b54-a51c-900a190e6958 'Session-Id':D1F36E72A2A4E828F59AF215FCA06C2A 'Thread-Group':main 'Thread-Id':25 'Version':"1.0"  'X-Message-Id': 'X-User-Id':] DEBUG com.wdpr.ee.authz.AuthFilter - #### Auth required = true, Scope required = true
	WW-AM04020514 2015-05-14 15:23:17,652 [tomcat-http--3] ['App-Name':DemoAuthZApp 'Correlation-Id':c1ae741e-639e-4b54-a51c-900a190e6958 'Session-Id':D1F36E72A2A4E828F59AF215FCA06C2A 'Thread-Group':main 'Thread-Id':25 'Version':"1.0"  'X-Message-Id': 'X-User-Id':] DEBUG com.wdpr.ee.authz.AuthFilter - #### Request headers = org.apache.tomcat.util.http.NamesEnumerator@65c69c85
	WW-AM04020514 2015-05-14 15:23:17,653 [tomcat-http--3] ['App-Name':DemoAuthZApp 'Correlation-Id':c1ae741e-639e-4b54-a51c-900a190e6958 'Session-Id':D1F36E72A2A4E828F59AF215FCA06C2A 'Thread-Group':main 'Thread-Id':25 'Version':"1.0"  'X-Message-Id': 'X-User-Id':] DEBUG com.wdpr.ee.authz.AuthFilter - #### Request method = GET
	WW-AM04020514 2015-05-14 15:23:17,653 [tomcat-http--3] ['App-Name':DemoAuthZApp 'Correlation-Id':c1ae741e-639e-4b54-a51c-900a190e6958 'Session-Id':D1F36E72A2A4E828F59AF215FCA06C2A 'Thread-Group':main 'Thread-Id':25 'Version':"1.0"  'X-Message-Id': 'X-User-Id':] DEBUG com.wdpr.ee.authz.AuthFilter - #### Token list = {cookie=JSESSIONID=2C4C42FAFBAC25F24392BCE3FA5AF478, connection=keep-alive, accept-language=en-US,en;q=0.8, host=localhost:8080, accept=*/*, user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36, accept-encoding=gzip, deflate, sdch, access_token=o8Hxsauxonm6ID-y72mySg}
	WW-AM04020514 2015-05-14 15:23:18,796 [tomcat-http--3] ['App-Name':DemoAuthZApp 'Correlation-Id':c1ae741e-639e-4b54-a51c-900a190e6958 'Session-Id':D1F36E72A2A4E828F59AF215FCA06C2A 'Thread-Group':main 'Thread-Id':25 'Version':"1.0"  'X-Message-Id': 'X-User-Id':] DEBUG com.wdpr.ee.authz.RestConnector - #### Time for calling the AUTHZ server is: 1093.309952 milliseconds
	WW-AM04020514 2015-05-14 15:23:18,808 [tomcat-http--3] ['App-Name':DemoAuthZApp 'Correlation-Id':c1ae741e-639e-4b54-a51c-900a190e6958 'Session-Id':D1F36E72A2A4E828F59AF215FCA06C2A 'Thread-Group':main 'Thread-Id':25 'Version':"1.0"  'X-Message-Id': 'X-User-Id':] DEBUG com.wdpr.ee.authz.RestConnector - Cache Miss: The response came from an upstream server
	WW-AM04020514 2015-05-14 15:23:20,241 [tomcat-http--3] ['App-Name':DemoAuthZApp 'Correlation-Id':c1ae741e-639e-4b54-a51c-900a190e6958 'Session-Id':D1F36E72A2A4E828F59AF215FCA06C2A 'Thread-Group':main 'Thread-Id':25 'Version':"1.0"  'X-Message-Id': 'X-User-Id':] INFO  com.wdpr.ee.authz.AuthFilter - Successful validation using token o8Hxsauxonm6ID-y72mySg- Auth/Scope : scopeRequired=true
	WW-AM04020514 2015-05-14 15:23:20,241 [tomcat-http--3] ['App-Name':DemoAuthZApp 'Correlation-Id':c1ae741e-639e-4b54-a51c-900a190e6958 'Session-Id':D1F36E72A2A4E828F59AF215FCA06C2A 'Thread-Group':main 'Thread-Id':25 'Version':"1.0"  'X-Message-Id': 'X-User-Id':] DEBUG com.wdpr.ee.authz.AuthFilter - #### Time for authz filter exectuion with VALID token is 2590.468096 milliseconds
	WW-AM04020514 2015-05-14 15:23:20,248 [tomcat-http--3] ['App-Name':DemoAuthZApp 'Correlation-Id':c1ae741e-639e-4b54-a51c-900a190e6958 'Session-Id':D1F36E72A2A4E828F59AF215FCA06C2A 'Thread-Group':main 'Thread-Id':25 'Version':"1.0"  'X-Message-Id': 'X-User-Id':] PERF  com.wdpr.ee.loggingapi.filter.HttpLoggingFilter -  Boundary Time :2603748000 (ns), Taken for functionality DemoAuthZApp_/DemoApplication/CallableServlet

