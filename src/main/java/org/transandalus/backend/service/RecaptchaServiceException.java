package org.transandalus.backend.service;

import org.springframework.web.client.RestClientException;

public class RecaptchaServiceException extends Exception {
	private static final long serialVersionUID = 3437769385559204638L;
	
	public RecaptchaServiceException(String message, RestClientException ex) {
		super(message, ex);
	}
}
