package org.transandalus.backend.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
	private static final long serialVersionUID = 7359715515916356106L;
	
	private String realAddress;

	public CustomWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		doPopulateRealAddress(request);
	}

	protected void doPopulateRealAddress(HttpServletRequest request) {
		realAddress =  request.getHeader("X-Real-IP");
	}

	public String getRealAddress() {
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