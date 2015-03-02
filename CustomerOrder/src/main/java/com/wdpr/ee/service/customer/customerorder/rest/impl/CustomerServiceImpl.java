package com.wdpr.ee.service.customer.customerorder.rest.impl;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import com.wdpr.ee.loggingapi.annotation.WdprMethodLog;
import com.wdpr.ee.service.customer.customerorder.exception.ErrorResponse;
import com.wdpr.ee.service.customer.customerorder.exec.CustomerServiceExectuor;
import com.wdpr.ee.service.customer.customerorder.model.Customer;
import com.wdpr.ee.service.customer.customerorder.model.CustomerOrder;
import com.wdpr.ee.service.customer.customerorder.util.LocalHeader;

public class CustomerServiceImpl {
	private static final Logger logger = LogManager	.getLogger(CustomerServiceImpl.class);
	
	private CustomerServiceExectuor customerServiceExecutor = new CustomerServiceExectuor();

	public CustomerServiceExectuor getCustomerServiceExecutor() {
		return customerServiceExecutor;
	}

	public void setCustomerServiceExecutor(
			CustomerServiceExectuor customerServiceExecutor) {
		this.customerServiceExecutor = customerServiceExecutor;
	}

/*
 * //sample: http://localhost:8080/CustomerOrder/customer-services/customer-create?firstName=DDDion&lastName=Forward&gender=M&firstAddressLine=9414+Easy+Subdivision&city=Fort+Dix&state=Mississippi&zip=39956-1447&namePrefix=Mr
 */

	@GET
	@Path("/customer-create")
	@Produces({ MediaType.APPLICATION_JSON })
	//@WdprMethodLog
	public Response createCustomer(@Context UriInfo ui,
			@Context HttpHeaders header) {
		
		long serviceStartTime = System.nanoTime();

		
		logger.info("Create Customer service begins");
		Customer customer = null;
		
		if (ui != null) {

			MultivaluedMap<String, String> params = ui.getQueryParameters();
			try {
				customer = new Customer(params);

				customerServiceExecutor.createCustomer(customer);
			} catch (IllegalArgumentException e) {
				logger.error("Exception in validating request ", e);
				ErrorResponse error = new ErrorResponse(400,
						"Mandatory param missing",
						e.getMessage());

				return generateErrorResponse(error);
			} catch (Exception e) {
				logger.error("Unknown runtime Exception ", e);
				ErrorResponse error = new ErrorResponse(500,
						"Internal server error", "unknown error");

				return generateErrorResponse(error);
			}
		}
		
		
		
		long serviceEndTime = System.nanoTime();
		logger.info("Total_Taken_CC :- create Customer Time "+TimeUnit.MILLISECONDS.convert(serviceEndTime- serviceStartTime, TimeUnit.NANOSECONDS));
		logger.info("service end Log statement time ");
		long serviceEndTimeLog = System.nanoTime();
		logger.info("LOG_Total_Taken_CC :- create Customer Time "+(serviceEndTimeLog-serviceEndTime));

		return Response.ok(customer, MediaType.APPLICATION_JSON).build();
	}
/*
 * http://localhost:8080/CustomerOrder/customer-services/customer-order?customerId=015da784-deec-4e0c-a9f8-70aff9adaa7d&shippingAddressId=91b9136f-1c0e-41fb-93e4-894f588fc719&itemNumber=48&itemQuantity=67
 */
	@GET
	@Path("/customer-order")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response createCustomerOrder(@Context UriInfo ui,
			@Context HttpHeaders header) {
		long serviceStartTime = System.nanoTime();

		if (header != null && header.getRequestHeaders() != null) {
			for (String headername : header.getRequestHeaders().keySet()) {				
				if (headername.toUpperCase().startsWith("X-")) {

					LocalHeader.put(headername, header.getRequestHeader(headername).get(0));
				}
			}
		}
		
		
		CustomerOrder customerOrder = null;
		logger.info("Create customer order service begins");
		if (ui != null) {

			MultivaluedMap<String, String> params = ui.getQueryParameters();
			try {
				customerOrder = new CustomerOrder(params);

				customerServiceExecutor.createCustomerOrder(customerOrder);
			} catch (IllegalArgumentException e) {
				logger.error("Exception in validating request ", e);
				ErrorResponse error = new ErrorResponse(400,
						"Mandatory param missing",
						e.getMessage());

				return generateErrorResponse(error);
			} catch (Exception e) {
				logger.error("Unknown runtime Exception  ", e);
				ErrorResponse error = new ErrorResponse(500,
						"Internal server error", "unknown error");

				return generateErrorResponse(error);
			}
		}
		
		long serviceEndTime = System.nanoTime();
		logger.info("Total_Taken_OC : Order Customer service Time "
				+TimeUnit.MILLISECONDS.convert(serviceEndTime- serviceStartTime, TimeUnit.NANOSECONDS));
		logger.info("order service end Log statement time ");
		long serviceEndTimeLog = System.nanoTime();
		logger.info("LOG_Total_Taken_OC :- create Customer Time "+(serviceEndTimeLog-serviceEndTime));


		return Response.ok(customerOrder, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/log-test")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response logTest(@Context UriInfo ui,
			@Context HttpHeaders header) {
		
		
		return Response.ok( "pass", MediaType.APPLICATION_JSON).build();
		
	}

	public static Response generateErrorResponse(ErrorResponse errorResponse)

	{
		ThreadContext.clearAll();
		return Response.status(errorResponse.getStatusCode())
				.type(MediaType.APPLICATION_JSON).entity(errorResponse).build();
	}
}
