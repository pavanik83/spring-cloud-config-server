/**
 * WDPR
 * @copyright Disney 2014
 * @author archj009
 */
package com.wdpr.ee.authzfilter.servlet;

/**
 * A very simple servlet to test the WDPR Http Logging Filter's passing
 * of shared log entry values, e.g. correlation id
 *
 */

import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author RJF3
 *
 */
public class CallableTestService extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final static Logger LOG = LogManager.getLogger(CallableTestService.class);

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String servletMessage = "You have reached the Callable Service Test endpoint! @ "
				+ new Date();
		resp.getOutputStream().write(servletMessage.getBytes());

		double start = System.nanoTime();
		LOG.info("Logging from the Callable Service Test servlet");
		double end = System.nanoTime();
		double elapsed = end - start;

		String elapsedMessage = "Time for logging the Callable Service Test message @ INFO level: "
				+ Double.toString(elapsed / 1000000) + " milliseconds";

		LOG.info(elapsedMessage);

	}
}
