#  <u>WDPR Auth Filter User's Guide</u>

- [WDPR Auth Filter Quick Start Guide](#wdpr-Auth Filter-quick-start-guide)


# WDPR Auth Filter Quick Start Guide

Using the Authz Filters requires the below steps:

1.) Add the below dependency in the web project pom.xml file so as to add the filter code in the project dependency.
	
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


2.) Add the below filter entry in project web.xml

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

3.) Add the below (.properties,.json) file in web project class path same can be retrived from the git Location(https://github.disney.com/WDPR-RA-UI/Security-Filter/tree/shanghai/DemoApplication/src/main/resources)

	scope.json
	WdprLogAppender.properties
	auth-config.properties