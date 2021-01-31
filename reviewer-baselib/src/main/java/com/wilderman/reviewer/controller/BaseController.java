package com.wilderman.reviewer.controller;

import com.wilderman.reviewer.ApiAuthentication;
import com.wilderman.reviewer.config.Constants;
//import com.wilderman.reviewer.db.primary.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class BaseController {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());


	@Autowired
	private final CacheManager caching = null;

	protected void handleHtmlResponse(String responseData, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.setContentLength(responseData.length());
		response.getWriter().write(responseData);
	}

	protected void handleHtmlError(int code, String responseData, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.setContentLength(responseData.length());
		response.sendError(code, responseData);
	}

	protected void handleJsonResponse(String responseData, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setContentLength(responseData.length());
		response.getWriter().write(responseData);
	}

	protected ApiAuthentication getAuth(HttpServletRequest req) {
		return (ApiAuthentication)req.getAttribute(Constants.AUTH_ATTR_NAME);
	}

	public static void setAuth(HttpServletRequest req, ApiAuthentication auth) {
		req.setAttribute(Constants.AUTH_ATTR_NAME, auth);
	}

	protected String loadResourceFile(Resource r) throws IOException {
		return new BufferedReader(new InputStreamReader(r.getInputStream()))
				.lines().parallel().collect(Collectors.joining("\n"));
	}

//	protected void clearUserCache(User u) {
//		caching.getCache("loginUser").evict(u.getExternalId());
//	}


}
