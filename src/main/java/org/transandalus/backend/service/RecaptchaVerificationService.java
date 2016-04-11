package org.transandalus.backend.service;

import java.util.Collection;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.transandalus.backend.config.JHipsterProperties;
import org.transandalus.backend.web.rest.dto.RecaptchaDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

@Service
public class RecaptchaVerificationService {
	private final Logger log = LoggerFactory.getLogger(MailService.class);

	private final RestTemplate restTemplate;

	private String recaptchaUrl = "https://www.google.com/recaptcha/api/siteverify";

	@Inject
	private JHipsterProperties jHipsterProperties;

	@Autowired
	public RecaptchaVerificationService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public boolean validate(RecaptchaDTO recaptcha){
		log.debug("Validating recaptcha '{}'", "" + recaptcha);

		String secret = jHipsterProperties.getRecaptcha().getSecret();

		RecaptchaResponse recaptchaResponse = new RecaptchaResponse();
		recaptchaResponse.success = false;
		
        try {
            recaptchaResponse = restTemplate.postForEntity(
                    recaptchaUrl, createBody(secret, recaptcha.getRemoteAddress(), recaptcha.getResponse()), RecaptchaResponse.class)
                    .getBody();
        } catch (RestClientException e) {
           log.error("Cannot validate recaptcha: '{}'", e.getMessage());
        }
        
        return recaptchaResponse.success;
	}

	private MultiValueMap<String, String> createBody(String secret, String remoteIp, String response) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("secret", secret);
        form.add("remoteip", remoteIp);
        form.add("response", response);
        return form;
    }
	
	private static class RecaptchaResponse {
		@JsonProperty("success")
		private boolean success;
		@JsonProperty("error-codes")
		private Collection<String> errorCodes;
	}
}
