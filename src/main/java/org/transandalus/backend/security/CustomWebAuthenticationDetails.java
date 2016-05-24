package org.transandalus.backend.security;

import java.util.Collections;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
	private static final long serialVersionUID = 7359715515916356106L;
	
	private final Logger log = LoggerFactory.getLogger(CustomWebAuthenticationDetails.class);
	
	private String realAddress;

	public CustomWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		doPopulateRealAddress(request);
	}

	protected void doPopulateRealAddress(HttpServletRequest request) {
		realAddress =  request.getHeader("X-Real-IP");
		String headers = Collections.list(request.getHeaderNames()).stream().map(s -> s + ":" + request.getHeader(s)).collect(Collectors.joining("\n"));
		log.debug("doPopulateRealAddress. Headers:\n{}", headers);
		log.debug("doPopulateRealAddress. realAddress: {}", realAddress);
		
	}

	public String getRealAddress() {
		log.debug("getRealAddress. realAddress: {}. remoteAddress: {}", realAddress, getRemoteAddress());
		return (realAddress != null)?realAddress:getRemoteAddress();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("; ");
		sb.append("RealAddress: ").append(this.getRealAddress());

		return sb.toString();
	}
}