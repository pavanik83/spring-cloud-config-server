nixon-poc
=========
	Cusomer order: POC rest service for client implementation
	wdpr-authz :POC for Auth Filtering




Introduction




[Security : Auth filter Architecture](https://github.disney.com/georn021/nixon-poc/blob/master/Auth-Filter.png)


[Demo: UI snaps with header & parms](https://github.disney.com/georn021/nixon-poc/blob/master/Snaps.png)

wdpr-authz is the maven Filter project which is being  plugged into CustomerOrder (Rest service ) . 

During service invocation Filter will be invoked and based on the Auth & scope status (ref: scope.json) appropriate go.authenitcation  service will be invoked 


Token Validation is being done against authorization.go.com Which is configured through auth-config.properties. The pluged client can configure appropriate Auth URL placed in /resources/auth-config.properties

REST service invokation URI: 

http://localhost:8080/CustomerOrder/customer-services/customer-create?firstName=NixonDion&lastName=Forward&gender=M&firstAddressLine=9414+Easy+Subdivision&city=Fort+Dix&state=Mississippi&zip=39956-1447

Client Dependency(pom.xml)

		<dependency>
			<groupId>com.wdpr.ee</groupId>
			<artifactId>wdpr-authz</artifactId>
			<version>1.0.0</version>	
			<scope>compile</scope>		
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

Note: Rest service(CustomerOrder) Client should pass necessory headers as part of request 
eg: access_token, client_id etc.


SAmple logs from Client 

    KPL-VM-NARAA011 2015-02-11 04:35:17,977 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':4e548cf6-9de8-42af-9b9c-ab2b79e556a0 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.loggingapi.filter.HttpLoggingFilter - X-CorrelationId Received: 5fac9a52-d477-4698-8758-c48e97b28be7
    KPL-VM-NARAA011 2015-02-11 04:35:17,977 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':5fac9a52-d477-4698-8758-c48e97b28be7 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.authz.AuthFilter - AuthFilter.............IN
    KPL-VM-NARAA011 2015-02-11 04:35:17,977 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':5fac9a52-d477-4698-8758-c48e97b28be7 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.authz.AuthFilter - scopeMap 2
    KPL-VM-NARAA011 2015-02-11 04:35:17,977 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':5fac9a52-d477-4698-8758-c48e97b28be7 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.authz.AuthFilter - key:/CustomerOrder*,path:/CustomerOrder
    KPL-VM-NARAA011 2015-02-11 04:35:17,977 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':5fac9a52-d477-4698-8758-c48e97b28be7 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.authz.AuthFilter - isMatch:true
    KPL-VM-NARAA011 2015-02-11 04:35:17,977 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':5fac9a52-d477-4698-8758-c48e97b28be7 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.authz.AuthFilter - authRequired:true, scopeRequired :true
    KPL-VM-NARAA011 2015-02-11 04:35:18,570 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':5fac9a52-d477-4698-8758-c48e97b28be7 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.authz.RestConnector - URI   >>>https://stg.authorization.go.com:443/validate/_CHyXsQ1sPUDDkmBMI-NqQ
    KPL-VM-NARAA011 2015-02-11 04:35:19,306 [http-8080-1] ['App-Name':CustomerService 'Correlation-Id':5fac9a52-d477-4698-8758-c48e97b28be7 'Session-Id':0C0217D2C4EF96BC6BEC1E3E3680B60F 'Thread-Group':main 'Thread-Id':1 'Version':"1.0"] INFO  com.wdpr.ee.authz.AuthFilter - authSuccess:true
