Security-Filter
=========
	Cusomer order: POC rest service for client implementation using Keystone as credentials supplier
	wdpr-authz : Auth Filtering Component

[OAuthFilterTestService2: POC for client id / client secret version of wdpr-authz filter](https://github.disney.com/WDPR-ReferenceArchitecture/OauthHTTPFilterTest)
	
###Introduction

[Security : Auth filter Architecture](https://github.disney.com/WDPR-RA-UI/Security-Filter/blob/master/Auth-Filter.png)

[Demo: UI snaps with header & params](https://github.disney.com/WDPR-RA-UI/Security-Filter/blob/master/Snaps.png)

wdpr-authz is the maven ServletFilter project, supporting an HTTP Filter-based option for  OAuth style protection for web services. There are two current test services supporting two different client authentication/authorization methods:
	
-  The CustomerOrder service, which uses the Keystone security service to authenticate a client and provide the clients credentials which include 'scopes', used by an AuthZ server to authorize endpoint access.
-  The OAuthFilterTestService2, which uses client id / client secret to perform authentication and provide 'scopes' (authorization levels) as stored on the AuthZ server.


Several properties are configurable:

1.  The target AuthZ server (via HTTP URL )
2.  Timeout duration for the filter's AuthZ server call 
3.  Scopes to be matched in any returned scopes from the AuthZ server, using regex patterns against the context of the incoming URL request 	

###REST service invocation URI: 

Example customer creation which requires an existing valid token:

    http://localhost:8080/customer-service/customer-services/customer-create?firstName=NixonDion&lastName=Forward&gender=M&firstAddressLine=9414+Easy+Subdivision&city=Fort+Dix&state=Mississippi&zip=39956-1447

###Client Dependency/Configuration
(pom.xml)

		<dependency>
			<groupId>com.wdpr.ee</groupId>
			<artifactId>wdpr-authz</artifactId>
			<version>1.0.1-SNAPSHOT</version>	
		</dependency> 

Client (web.xml)
#     
    <web-app>
    ..
       <filter>
        <filter-name>AuthFilter</filter-name>
        <filter-class>com.wdpr.ee.authz.AuthFilter</filter-class>
       </filter>
       <filter-mapping>
        <filter-name>AuthFilter</filter-name>
        <url-pattern>/*</url-pattern>
       </filter-mapping>
     </web-app #

Note: Rest service(customer-service) Client should pass necessary headers as part of request for new token 
eg: access_token, client_id etc.

Sample logs from Client:

    KPL-VM-NARAA011 2015-02-11 04:35:17,977 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':4e548cf6-9de8-42af-9b9c-ab2b79e556a0 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.loggingapi.filter.HttpLoggingFilter - X-CorrelationId Received: 5fac9a52-d477-4698-8758-c48e97b28be7
    KPL-VM-NARAA011 2015-02-11 04:35:17,977 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':5fac9a52-d477-4698-8758-c48e97b28be7 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.authz.AuthFilter - AuthFilter.............IN
    KPL-VM-NARAA011 2015-02-11 04:35:17,977 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':5fac9a52-d477-4698-8758-c48e97b28be7 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.authz.AuthFilter - scopeMap 2
    KPL-VM-NARAA011 2015-02-11 04:35:17,977 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':5fac9a52-d477-4698-8758-c48e97b28be7 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.authz.AuthFilter - key:/CustomerOrder*,path:/CustomerOrder
    KPL-VM-NARAA011 2015-02-11 04:35:17,977 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':5fac9a52-d477-4698-8758-c48e97b28be7 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.authz.AuthFilter - isMatch:true
    KPL-VM-NARAA011 2015-02-11 04:35:17,977 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':5fac9a52-d477-4698-8758-c48e97b28be7 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.authz.AuthFilter - authRequired:true, scopeRequired :true
    KPL-VM-NARAA011 2015-02-11 04:35:18,570 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':5fac9a52-d477-4698-8758-c48e97b28be7 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.authz.RestConnector - URI   >>>https://stg.authorization.go.com:443/validate/_CHyXsQ1sPUDDkmBMI-NqQ
    KPL-VM-NARAA011 2015-02-11 04:35:19,306 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':5fac9a52-d477-4698-8758-c48e97b28be7 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.authz.AuthFilter - authSuccess:true
