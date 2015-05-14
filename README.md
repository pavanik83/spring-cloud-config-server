WDPR AuthZ Filter
=========

This component is an HTTP 'filter', e.g., it intercepts calls into a web app container (e.g., Tomcat, WAS, etc) and performs  a function prior to passing on to the  request to the real endpoint.  In this case, t**he filter's function is to act as a security gate using the OAuth security protocol**. More information on OAuth can be found at the following links:

* [OAuth Defined](http://en.wikipedia.org/wiki/OAuth)
* [OAuth Community Site](http://oauth.net/)

## OAuth Sequence using the WDPR AuthZ Filter


The OAuth '2' 'protocol is the current standard for WDPR. This protocol involves the following steps:

1. A client requests access to a protected resource from the resource owner. Usually this involves calling an authenticating./authorization site (e.g., Keystone), but this function can also be handled directly by the OAuth server (called 'AuthZ' server), which can function as a basic password/secret validation service.
2.  The AuthZ server provides a token after the client is authenticated, either by checking it's own id/secret database for a match or by sending the credentials returned by the Resource owner when the client authenticated with the owner. If the latter case, AuthZ calls the Resource owner to verify the credentials are authentic.  Assuming things go well, AuthZ returns a token to the client that include any 'scopes' (authorizations ), either  defined by the Resource owner or held by AuthZ for the given client id. 
3.  Assuming t a token was granted, the client makes a request to an endpoint protected by the  AuthZ server and includes the token. In the case of web services, this is usually done via an HTTP header.
4.  When the client request reaches the endpoint server, the servlet container (Tomcat, WAS, etc.) will invoke any and all filters defined for that endpoint.  Each filter is called in succession, thus the term 'chained' filters.
5.  When a request reaches the AuthZ filter, it has two functions:
	*  Call the AuthZ server and validate that the token carried in the request is valid/still active
	*  Compare any 'scopes' (think permissions or roles) in the token with scope required by the** endpoint. 

Failure of step 5 for any reason results in the request being terminated and a '401' returned to the client. 

**A detailed discussion of how to use the AuthZ Filter is available at [AuthZ Usage Guide](https://github.disney.com/WDPR-RA-UI/Security-Filter/blob/shanghai/Documents/wdpr-authz-user-guide.md)**
 
##Example Implementations

The WDPR AuthZ filter repo contains two example implementations of using the filter:

1. [Customer order: POC rest service for client implementation using Keystone as credentials supplier](https://github.disney.com/WDPR-RA-UI/Security-Filter/tree/shanghai/CustomerOrder)
2. [DemoApplication: POC for client id / client secret version of wdpr-authz filter](https://github.disney.com/WDPR-ReferenceArchitecture/OauthHTTPFilterTest)
	




