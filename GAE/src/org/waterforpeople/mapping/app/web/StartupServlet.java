package org.waterforpeople.mapping.app.web;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.LifecycleManager;
import com.google.appengine.api.LifecycleManager.ShutdownHook;

/**
 * simple servlet to bind a shutdown hook
 * 
 * @author Christopher Fagiani
 * 
 */
public class StartupServlet extends HttpServlet {

	private static final long serialVersionUID = -5683590972260558060L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		executeRequest(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		executeRequest(req, resp);
	}

	private void executeRequest(HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setStatus(HttpServletResponse.SC_OK);
			LifecycleManager.getInstance().setShutdownHook(new ShutdownHook() {
				public void shutdown() {
					LifecycleManager.getInstance().interruptAllRequests();
				}
			});
		} catch (Throwable t) {
			System.out.println("Cannot register shutdown hook."
					+ t.getMessage());
			t.printStackTrace();
		}
	}
}
